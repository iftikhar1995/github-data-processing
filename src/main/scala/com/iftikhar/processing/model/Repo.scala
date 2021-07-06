package com.iftikhar.processing.model

import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType

case class Repo(
                  id: Long,
                  name: String
                )

object Repo{

  def getSchema():StructType = ScalaReflection.schemaFor[Repo].dataType.asInstanceOf[StructType]

}
