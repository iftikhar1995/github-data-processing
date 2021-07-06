package com.iftikhar.processing.model

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

case class Commit(
                   sha: String,
                   message: String,
                   eventId: Long
                 )

object Commit{
  def getSchema():StructType = ScalaReflection.schemaFor[Commit].dataType.asInstanceOf[StructType]
}
