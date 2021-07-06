package com.iftikhar.processing.model

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

case class Actor(
                  id: Long,
                  userName: String
                )

object Actor{

  def getSchema():StructType = ScalaReflection.schemaFor[Actor].dataType.asInstanceOf[StructType]

}
