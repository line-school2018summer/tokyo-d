package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val uid = uuid("uid").primaryKey()
    val suid = varchar("suid", 32).uniqueIndex()
    val name = varchar("name", 32)
}