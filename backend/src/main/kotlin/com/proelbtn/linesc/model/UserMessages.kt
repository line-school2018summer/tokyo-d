package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.date

object UserMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val fid = reference("fid", Users.id).index()
    val tid = reference("tid", Users.id).index()
    val content = varchar("content", 1024)
    val createdAt = datetime("created_at")
}