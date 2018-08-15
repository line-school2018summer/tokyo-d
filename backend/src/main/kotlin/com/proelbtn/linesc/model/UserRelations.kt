package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserRelations: Table() {
    val from = (uuid("from") references Users.id).index()
    val to = (uuid("to") references Users.id).index()
    val createdAt = UserMessages.datetime("created_at")
}