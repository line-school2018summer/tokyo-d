package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserGroupRelations: Table() {
    val fid = (uuid("fid") references Users.id).index()
    val tid = (uuid("tid") references UserGroups.id).index()
    val createdAt = UserMessages.datetime("created_at")
}