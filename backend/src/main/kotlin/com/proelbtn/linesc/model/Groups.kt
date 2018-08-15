package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object Groups: Table() {
    val gid = uuid("gid").primaryKey()
    val sgid = varchar("sgid", 32).uniqueIndex()
    val name = varchar("name", 32)
}