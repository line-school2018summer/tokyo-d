package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = uuid("id").primaryKey()
    val sid = varchar("sid", 32).uniqueIndex()
    val name = varchar("name", 32)
    val createdAt = UserMessages.datetime("created_at")
    val updatedAt = UserMessages.datetime("updated_at")
}