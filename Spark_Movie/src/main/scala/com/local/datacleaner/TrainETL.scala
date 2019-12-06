package com.local.datacleaner

import com.local.App
import org.apache.spark.mllib.recommendation.Rating

/**
  * 整理训练数据（保存到data/training）
  *
  */
object TrainETL extends App{

  def main(args: Array[String]) {

    val ratings=sc.textFile("data/ratings.dat").map(line=>{
      val fields=line.split("\t")
      val rating=Rating(fields(0).toInt,fields(1).toInt,fields(2).toDouble)
      val timestamp=fields(3).toLong%10
      (timestamp,rating)
    })

    // 用"\t"隔离
    val training=ratings.filter(x=>x._1<6).map(x=>{
      x._2.user+"\t"+x._2.product+"\t"+x._2.rating
    })

    // 保存数据
    training.saveAsTextFile("data/training")
  }
}
