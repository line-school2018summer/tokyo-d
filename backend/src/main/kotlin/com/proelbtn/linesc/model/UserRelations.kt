package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserRelations: Table() {
    val from = reference("from", Users.id).index()
    val to = reference("to", Users.id).index()
    val createdAt = UserMessages.datetime("created_at")
}