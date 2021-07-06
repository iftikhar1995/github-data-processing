package com.iftikhar.processing


import com.iftikhar.processing.processor.GitHubDataProcessor
import com.iftikhar.processing.utils.Config
import org.apache.spark.sql.SparkSession


object Driver {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder
      .appName(Config.APP_NAME)
      .master(Config.MASTER)
      .getOrCreate

    val processor = new GitHubDataProcessor(spark)

    processor.getTopTenUsersByPullRequestAndCommits().show(truncate = false)
    processor.getTopTenReposByCommits().show(truncate = false)
    processor.getTopTenReposByWatchEvents().show(truncate = false)

  }
}
