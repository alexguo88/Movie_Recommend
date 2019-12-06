package com.zxl.modeltrain

import com.zxl.App
import org.apache.spark.sql.SaveMode

/**
  * 根据ratings.txt，格式为(userId,movieId,rating)生成电影相似度表
  *
  * Created by ZXL on 2018/3/14.
  */
object ItemCF extends App{

  /**
    *
    *
    * @param args
    */
  def main(args: Array[String]) {

    import sqlContext.implicits._

    // 2 读取样本数据
    val data_path = "hdfs://movie1:9000/movie/data/ratings.txt"
    val data = sc.textFile(data_path, 8)
    val userdata = data.map(_.split(",")).map(f => (ItemPref(f(0), f(1), f(2).toDouble))).cache()

    // 3 建立模型(相似度计算)
    val mysimil = new ItemSimilarity()
    val simil_rdd1 = mysimil.Similarity(userdata, "cooccurrence")

    //val recommd = new RecommendedItem
    //val recommd_rdd1 = recommd.Recommend(simil_rdd1, userdata, 5)

    // 4 存储结果
    //simil_rdd1.map(item => item.itemid1 + "," + item.itemid2 + "," + item.similar).saveAsTextFile("/tmp/similar")
    //recommd_rdd1.map(urecomm => urecomm.userid + "," + urecomm.itemid + "," + urecomm.pref).saveAsTextFile("data/recomm")

    val simiDF = simil_rdd1.filter(x => x.similar >= 0.4).toDF()

    // 存储结果至mysql数据库
    simiDF.write.mode(SaveMode.Append).jdbc(jdbcURL, recResultTable, prop)
  }

}
