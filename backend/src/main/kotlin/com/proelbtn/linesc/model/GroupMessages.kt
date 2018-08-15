package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object GroupMessages: Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val fromid = reference("fromid", Users.uid).index()
    val toid = reference("toid", Groups.gid).index()
    val content = varchar("content", 1024)
}