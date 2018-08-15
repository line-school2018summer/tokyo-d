package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserGroupMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val fid = reference("fid", Users.id).index()
    val tid = reference("tid", UserGroups.id).index()
    val content = varchar("content", 1024)
    val createdAt = UserMessages.datetime("created_at")
}