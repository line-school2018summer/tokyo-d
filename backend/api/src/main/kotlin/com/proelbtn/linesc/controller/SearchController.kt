package com.proelbtn.linesc.controller

import com.proelbtn.linesc.response.GroupResponse
import com.proelbtn.linesc.response.UserResponse
import com.proelbtn.linesc.model.UserGroups
import com.proelbtn.linesc.model.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController {
    @GetMapping("/search/users/{sid}")
    fun searchUserInformation(@PathVariable("sid") sid: String): ResponseEntity<UserResponse> {
        var message: UserResponse? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = Users.select { Users.sid eq sid }

            if (query.count() == 1) {
                var user = query.first()

                message = UserResponse(
                        user[Users.id].toString(),
                        user[Users.sid],
                        user[Users.name],
                        user[Users.createdAt].toString(),
                        user[Users.updatedAt].toString()
                )
                status = HttpStatus.OK
            }
            else if (query.count() == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }

    @GetMapping("/search/groups/{sid}")
    fun searchGroupInformation(@PathVariable("sid") sid: String): ResponseEntity<GroupResponse> {
        var message: GroupResponse? = null
        var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        transaction {
            val query = UserGroups.select { UserGroups.sid eq sid }

            if (query.count() == 1) {
                var group = query.first()

                message = GroupResponse(
                        group[UserGroups.id].toString(),
                        group[UserGroups.sid],
                        group[UserGroups.name],
                        group[UserGroups.owner].toString(),
                        group[UserGroups.createdAt].toString(),
                        group[UserGroups.updatedAt].toString()
                )
                status = HttpStatus.OK
            }
            else if (query.count() == 0) status = HttpStatus.NOT_FOUND
            else status = HttpStatus.INTERNAL_SERVER_ERROR
        }

        return ResponseEntity(message, status)
    }
}
