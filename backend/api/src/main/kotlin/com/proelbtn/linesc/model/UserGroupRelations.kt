package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserGroupRelations: Table() {
    val from = (uuid("from") references Users.id).index()
    val to = (uuid("to") references UserGroups.id).index()
    val createdAt = datetime("created_at")
}