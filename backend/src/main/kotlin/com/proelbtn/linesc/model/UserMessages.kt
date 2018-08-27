package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val from = (uuid("from") references Users.id).index()
    val to = (uuid("to") references Users.id).index()
    val content = varchar("content", 1024)
    val createdAt = datetime("created_at")
}