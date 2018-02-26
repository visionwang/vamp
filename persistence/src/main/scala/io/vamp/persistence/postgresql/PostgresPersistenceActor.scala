package io.vamp.persistence.postgresql

import io.vamp.common.ClassMapper
import io.vamp.persistence.{ SqlPersistenceActor, SqlStatementProvider }

class PostgresPersistenceActorMapper extends ClassMapper {
  val name = "postgres"
  val clazz: Class[_] = classOf[PostgresPersistenceActor]
}

class PostgresPersistenceActor extends SqlPersistenceActor with SqlStatementProvider {

  def selectStatement(lastId: Long): String = s"""SELECT "ID", "Record" FROM "$table" WHERE "ID" > $lastId ORDER BY "ID" ASC"""

  def insertStatement(): String = s"""insert into "$table" ("Record") values (?)"""
}
