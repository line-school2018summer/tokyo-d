package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val fid = (uuid("fid") references Users.id).index()
    val tid = (uuid("tid") references Users.id).index()
    val content = varchar("content", 1024)
}