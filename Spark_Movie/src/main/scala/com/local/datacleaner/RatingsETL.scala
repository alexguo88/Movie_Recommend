package com.local.datacleaner

import com.local.App
import com.local.caseclass.Ratings
import com.local.utils.ToMySQLUtils
import org.apache.spark.sql.SaveMode

/**
  * 评分ETL
  *
  */
object RatingsETL extends App{

  def main(args: Array[String]) {

    // 把每行变成一个评分对象
    val ratingsRDD=sc.textFile("traindata/training.dat").map(line=>{
      val fields=line.split("\t")
      Ratings(fields(0).toInt,fields(1).toInt,fields(2).toDouble)
    })

    import sqlContext.implicits._

    //
    val ratingsDF=ratingsRDD.toDF

    // jdbc保存到mysql
    ToMySQLUtils.toMySQL(ratingsDF,"rating",SaveMode.Append)
  }
}
