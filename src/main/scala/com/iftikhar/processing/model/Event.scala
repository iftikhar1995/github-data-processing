package com.iftikhar.processing.model

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

case class Event(
                  id: Long,
                  eventType: String,
                  actorId: Long,
                  repoId:Long
                )

object Event{
  def getSchema():StructType = ScalaReflection.schemaFor[Event].dataType.asInstanceOf[StructType]
}
