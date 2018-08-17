package com.proelbtn.linesc

import com.proelbtn.linesc.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.joda.time.DateTime
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import redis.clients.jedis.Jedis
import java.util.*


@SpringBootApplication
class LineScApplication

fun main(args: Array<String>) {
    Database.connect("jdbc:mysql://localhost:3306/line-sc", driver = "com.mysql.jdbc.Driver", user = "root", password = "lineschool")

    transaction {
        create(UserGroupMessages, UserGroupRelations, UserGroups, UserMessages, UserRelations, Users)

        var query = Users.select { Users.sid eq "UserA" }
        if (query.count() == 0) {
            val now = DateTime.now()
            Users.insert {
                it[id] = UUID.fromString("cb4a5ccd-e30c-4bb6-bcce-2dbb35056cd3")
                it[sid] = "UserA"
                it[name] = "UserA"
                it[createdAt] = now
                it[updatedAt] = now
            }
        }

        query = Users.select { Users.sid eq "UserB" }
        if (query.count() == 0) {
            val now = DateTime.now()
            Users.insert {
                it[id] = UUID.fromString("c437149e-a918-4442-a533-99596bed2624")
                it[sid] = "UserB"
                it[name] = "UserB"
                it[createdAt] = now
                it[updatedAt] = now
            }
        }
    }



    runApplication<LineScApplication>(*args)
}
