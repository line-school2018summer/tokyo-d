package com.proelbtn.linesc.model

import org.jetbrains.exposed.sql.Table

object PersonalRelations: Table() {
    val from = reference("from", Users.uid).index()
    val to = reference("to", Users.uid).index()
}