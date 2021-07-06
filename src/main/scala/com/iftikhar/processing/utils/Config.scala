package com.iftikhar.processing.utils

object Config {

  val APP_NAME = "GitHubDataAnalyses-IftikharLiaquat"
  val MASTER = "local[*]"

  object PATH {
    val ACTORS_DATA = "src/main/resources/actors.csv"
    val COMMITS_DATA = "src/main/resources/commits.csv"
    val EVENTS_DATA = "src/main/resources/events.csv"
    val REPOS_DATA = "src/main/resources/repos.csv"
  }
}
