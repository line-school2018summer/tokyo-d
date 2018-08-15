package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object UserGroupRelations: Table() {
    val fid = reference("fid", Users.id).index()
    val tid = reference("tid", UserGroups.id).index()
    val createdAt = UserMessages.datetime("created_at")
}