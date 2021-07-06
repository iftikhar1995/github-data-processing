package com.iftikhar.processing.processor


import com.iftikhar.processing.model.{Actor, Commit, Event, Repo}
import com.iftikhar.processing.utils.Config
import com.iftikhar.processing.utils.Constant.{COLUMN, EVENT_TYPE, JOIN}
import org.apache.spark.sql.functions.{col, count, desc, when}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}


class GitHubDataProcessor(sparkSession: SparkSession) {

  val actors: DataFrame = readData(Config.PATH.ACTORS_DATA, Actor.getSchema())
  val commits: DataFrame = readData(Config.PATH.COMMITS_DATA, Commit.getSchema())
  val events: DataFrame = readData(Config.PATH.EVENTS_DATA, Event.getSchema())
  val repos: DataFrame = readData(Config.PATH.REPOS_DATA, Repo.getSchema())


  /***
   * A helper function to read data from csv and create a dataframe from it.
   *
   * @param path The path of the file containing data.
   * @param schema The schema associated with the data.
   * @param header Header is present or not in the data file. It can be either "true" and "false"
   * @param format The format of the data file.
   *
   * @return Returns the dataframe containing the data
   */
  private def readData(path: String, schema: StructType,
                       header: String = "true", format: String = "csv"): DataFrame = {

    sparkSession.
      read.
      format(format).
      option("header", header).
      schema(schema).
      load(path)
  }


  /**
   * A function that will returns the top ten users by pull request count and commit counts.
   *
   */
  def getTopTenUsersByPullRequestAndCommits(): DataFrame = {

    // Extracting the Pull Request events and counting the Pull Request events for each actor.
    val pullRequestEvents = events.
      where(events(COLUMN.EVENT.EVENT_TYPE) === EVENT_TYPE.PULL_REQUEST_EVENT).
      groupBy(events(COLUMN.EVENT.ACTOR_ID)).agg(count(COLUMN.EVENT.EVENT_TYPE).alias("PullRequestCount"))

    // Extracting the commit events and counting the commit events for each actor.
    val commitEvents = events.
      where(events(COLUMN.EVENT.EVENT_TYPE) === EVENT_TYPE.COMMIT_COMMENT_EVENT).
      groupBy(events(COLUMN.EVENT.ACTOR_ID)).agg(count(COLUMN.EVENT.EVENT_TYPE).alias("CommitCount"))

    // Joining the pull request data with commit data. Then converting null values to 0 for count of commit.
    val topTenUsers = pullRequestEvents.join(
      commitEvents,
      pullRequestEvents(COLUMN.EVENT.ACTOR_ID) === commitEvents(COLUMN.EVENT.ACTOR_ID),
      JOIN.LEFT_OUTER
    ).withColumn("NonNullCommitCount",
      when(commitEvents("CommitCount").isNull, 0).
        otherwise(commitEvents("CommitCount"))
    ).select(
      pullRequestEvents(COLUMN.EVENT.ACTOR_ID),
      col("NonNullCommitCount").alias("CommitCount"),
      pullRequestEvents("PullRequestCount")
    ).orderBy(desc("PullRequestCount"), desc("CommitCount") ).limit(10)

    // Joining the top ten user data with the actor id to get the actor name.
    actors.distinct.join(
      topTenUsers,
      actors(COLUMN.ACTOR.ID) === topTenUsers(COLUMN.EVENT.ACTOR_ID),
      JOIN.INNER
    ).select(
      "userName",
      "PullRequestCount",
      "CommitCount"
    ).orderBy(desc("PullRequestCount"), desc("CommitCount") )

  }


  def getTopTenReposByCommits(): DataFrame = {

    val commitEventsByRepoId = events.
      where(events(COLUMN.EVENT.EVENT_TYPE) === EVENT_TYPE.COMMIT_COMMENT_EVENT).
      groupBy(events(COLUMN.EVENT.REPO_ID)).agg(count(COLUMN.EVENT.EVENT_TYPE).alias("CommitCount")).
      orderBy(desc("CommitCount")).limit(10)

    repos.distinct().
      join(
        commitEventsByRepoId,
        repos(COLUMN.REPO.ID) === commitEventsByRepoId(COLUMN.EVENT.REPO_ID),
        JOIN.INNER
      ).select(
      "name",
      "CommitCount"
    ).orderBy(desc("CommitCount"))
  }


  def getTopTenReposByWatchEvents(): DataFrame = {

    val commitEventsByRepoId = events.
      where(events(COLUMN.EVENT.EVENT_TYPE) === EVENT_TYPE.WATCH_EVENT).
      groupBy(events(COLUMN.EVENT.REPO_ID)).agg(count(COLUMN.EVENT.EVENT_TYPE).alias("WatchCount")).
      orderBy(desc("WatchCount")).limit(10)

    repos.distinct().
      join(
        commitEventsByRepoId,
        repos(COLUMN.REPO.ID) === commitEventsByRepoId(COLUMN.EVENT.REPO_ID),
        JOIN.INNER
      ).select(
      "name",
      "WatchCount"
    ).orderBy(desc("WatchCount"))
  }

}
