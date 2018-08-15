package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object GroupRelations: Table() {
    val from = reference("from", Users.uid).index()
    val to = reference("to", Groups.gid).index()
}