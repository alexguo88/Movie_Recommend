package com.zxl

import java.util.Properties

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 用于创建spark程序入口，供其他程序继承
  * Created by ZXL on 2018/3/4.
  */
trait App {

  val localClusterURL = "local[2]"
  val clusterMasterURL = "spark://movie1:7077"

  val conf = new SparkConf().setMaster(clusterMasterURL)

  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)
  val hc = new HiveContext(sc)

  //jdbc连接
  val jdbcURL = "jdbc:mysql://movie2:3306/recommend?useUnicode=true&characterEncoding=utf-8"

  val alsTable = "recommend.alsTab"
  val recResultTable = "recommend.similarTab" // 电影相似度表
  val top5Table = "recommend.top5Result" //top 5 推荐表
  val userTable = "recommend.user" // 会员表
  val ratingTable = "recommend.rating" //打分表


  // 数据库连接配置
  val prop = new Properties
  val mysqlusername = "root"
  val mysqlpassword = "root"
  prop.put("driver", "com.mysql.jdbc.Driver")
  prop.put("user", mysqlusername)
  prop.put("password", mysqlpassword)
}
