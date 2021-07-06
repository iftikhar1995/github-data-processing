package com.iftikhar.processing.utils

object Constant {

  object EVENT_TYPE {
    val PULL_REQUEST_EVENT = "PullRequestEvent"
    val COMMIT_COMMENT_EVENT = "CommitCommentEvent"
    val WATCH_EVENT = "WatchEvent"
  }

  object JOIN {

    val INNER = "inner"
    val LEFT_OUTER = "left_outer"

  }

  object COLUMN {

    object ACTOR {
      val ID = "id"
      val USER_NAME = "userName"
    }

    object EVENT {
      val ACTOR_ID = "actorId"
      val EVENT_TYPE = "eventType"
      val REPO_ID = "repoId"
    }

    object REPO {
      val ID = "id"
    }

  }

}
