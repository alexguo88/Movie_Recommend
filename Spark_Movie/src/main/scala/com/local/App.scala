package com.local

import com.local.utils.LoggerLevels
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

trait App {

  //去除提示信息
  LoggerLevels.setStreamingLogLevels()

  val conf = new SparkConf().setAppName("AppConf").setMaster("local")
  val sc = new SparkContext(conf)

  val sqlContext = new SQLContext(sc)
}
