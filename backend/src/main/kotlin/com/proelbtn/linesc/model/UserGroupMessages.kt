package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserGroupMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val fid = (uuid("fid") references Users.id).index()
    val tid = (uuid("tid") references UserGroups.id).index()
    val content = varchar("content", 1024)
    val createdAt = datetime("created_at")
}