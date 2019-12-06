package com.zxl.streaming

import java.sql.Connection

import com.zxl.caseclass.{Result, UserRating}
import com.zxl.utils.{DBLocalUtils, DBUtils}
import kafka.serializer.StringDecoder
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Duration, StreamingContext}

import scala.collection.mutable.ArrayBuffer


/**
  * 根据评分记录，利用推荐模型，产生推荐数据
  *
  * 接收kafka产生的数据，进行处理
  *
  * Created by ZXL on 2018/3/11.
  */
object SparkDrStreamALS extends Serializable {

  def main(args: Array[String]) {
    //    val conf = new SparkConf().setAppName("SparkDrStream").setMaster("spark://movie1:7077")

    val conf = new SparkConf().setAppName("SparkDrStream").setMaster("local[2]")
    val sc = new SparkContext(conf)
    // Duration对象中封装了时间的一个对象，它的单位是ms.
    val batchDuration = new Duration(5000)
    // batchDuration为时间间隔
    val ssc = new StreamingContext(sc, batchDuration)
    ssc.checkpoint("F:/movieck2")


    val topics = Set("test1")
    val kafkaParams = Map("metadata.broker.list" -> "movie1:9092,movie2:9092,movie3:9092")
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)


    //转换成 UserRating
    val messagesDStream = messages.map(_._2).map(line => {
      var rating: UserRating = null
      if (line != null) {
        try {
          val fields = line.split(",")
          if (fields.length == 3) {
            rating = UserRating(fields(0).toInt, fields(1).toInt, fields(2).toDouble)
          }
        } catch {
          case e => e.printStackTrace()
        }
      }
      rating
    })

    // 去除重复数据（就是该用户对某一个电影的评分发生改变）
    // updateStateByKey: 以DStream中的数据进行按key做reduce操作，然后对各个批次的数据进行累加
    val ratings = messagesDStream
      .map {
        case UserRating(user, movie, rating) => {
          ((user, movie), rating)
        }
      }
      .updateStateByKey {
        (values: Seq[Double], now: Option[Double]) => { //now:是当前的评分数据， //values:是历史评分数据
          var latest: Double = now.getOrElse(0)
          if (values.size > 0) {
            latest = values(0)
          }
          Some(latest)
        }
      }
      .map {
        case ((user, movie), rating) =>
          UserRating(user, movie, rating)
      }

    // 打印
    ratings.print()


    //推荐模型
    val modelpath = "model" //模型存储路径
    ratings.foreachRDD(rdd => {

      val model = MatrixFactorizationModel.load(sc, modelpath) //加载模型数据
      if (!rdd.isEmpty()) {
        val ratings = rdd.map { case UserRating(user, movie, rating) => (user.toInt, movie.toInt) }.distinct().toLocalIterator

        while (ratings.hasNext) { // 如果userId相同则不存入数据库直接更新即可

          //
          // println(1)
          val use_movie = ratings.next()

          val user = use_movie._1.toInt //用户编号
          val movie = use_movie._2.toInt //电影编号

          //1、利用模型给用户推荐5个电影（通过）
          val rec = model.recommendProducts(user, 5)
          val uidString = rec.map(x => x.user.toString() + "," + x.product.toString() + "," + x.rating.toString())
          val uidDFArray = sc.parallelize(uidString) //推荐结果转换为DF

          //得到推荐的电影编号数组
          val movieIds = uidDFArray
            .map(_.split(","))
            .filter(x => x(2).trim().toDouble >= 4.0) //大于4分
            .map(x => x(1).trim().toInt)
            .toArray() // 得到电影编号

          //2、 根据电影相似度推荐5部电影
          val similarMovies = get5SimilarMovies(movie)

          //3、 合并结果
          val recMovies = movieIds ++ similarMovies

          //4、 将推荐结果写入数据库
          val recMoviesStr = recMovies.toList.distinct
          resultInsertIntoMysql(user, recMoviesStr.mkString(","))


        }
      }
    })


    ssc.start()
    ssc.awaitTermination()
  }

  // 获取与某部电影最相似的5部电影（数据库查询）
  def get5SimilarMovies(movieId: Int): Array[Int] = {
    var movies = ArrayBuffer[Int]()
    var connection: Connection = null
    try {
      connection = DBUtils.getConnection()
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("select itemid2 from similartab where itemid1 = " + movieId + " order by similar limit 5")

      while (resultSet.next()) {
        movies += resultSet.getInt("itemid2")
      }
    } catch {
      case e => e.printStackTrace
    }
    DBUtils.close(connection)
    movies.toArray
  }

  // 将推荐结果写入数据库中
  def resultInsertIntoMysql(userId: Int, movieIds: String): Unit = {
    var connection: Connection = null
    try {
      connection = DBLocalUtils.getConnection()

      // 检查数据库中该id是否存在
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("select count(1) from recTab where userId = " + userId)
      resultSet.next()
      val isHaving = resultSet.getInt(1)
      println(isHaving)

      if (isHaving == 0) {
        val sql = "insert into recTab values(?,?)"
        val pst = connection.prepareStatement(sql)
        pst.setInt(1, userId)
        pst.setString(2, movieIds)
        pst.execute()
      } else {
        val sql = "update recTab set movieIds=? where userId=?"
        val pst = connection.prepareStatement(sql)
        pst.setString(1, movieIds)
        pst.setInt(2, userId)
        pst.execute()
      }

    } catch {
      case e => e.printStackTrace
    }
    DBLocalUtils.close(connection)
  }
}

