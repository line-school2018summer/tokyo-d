package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.exceptions.ForbiddenException
import com.proelbtn.linesc.exceptions.NotFoundException
import com.proelbtn.linesc.request.CreateRelationRequest
import com.proelbtn.linesc.response.RelationResponse
import com.proelbtn.linesc.model.UserRelations
import com.proelbtn.linesc.validator.validate_id
import io.swagger.annotations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserRelationsController {
    @Authentication
    @PostMapping(
            value = "/relations/users",
            produces = [ APPLICATION_JSON_VALUE ]
    )
    @ApiOperation(
            value = "ユーザ間の関係の作成用",
            notes = "ユーザ間の関係を作成するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザ間の関係を作成できた。")),
                (ApiResponse(code = 403, message = "関係を作成することが出来ない。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun createUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "作成するユーザ間の関係の情報") @RequestBody req: CreateRelationRequest
                ): RelationResponse {
        var res: RelationResponse? = null

        // validation
        if (user != req.from) throw ForbiddenException()

        val from = req.from
        val to = req.to
        val now = DateTime.now()

        transaction {
            val query = UserRelations.select {
                (UserRelations.from eq from) and (UserRelations.to eq to)
            }.firstOrNull()

            if (query != null) throw ForbiddenException()

            UserRelations.insert {
                it[UserRelations.from] = from
                it[UserRelations.to] = to
                it[UserRelations.createdAt] = now
            }
        }

        return RelationResponse(from, to, now.toString())
    }

    @Authentication
    @GetMapping(
            "/relations/users"
    )
    @ApiOperation(
            value = "ユーザ間の関係の取得用",
            notes = "ユーザ間の関係を取得するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザ間の関係を取得できた。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getUserRelations(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID
                ): List<RelationResponse> {
        val rels = transaction { UserRelations.select {
                (UserRelations.from eq user) or (UserRelations.to eq user)
            }.toList()
        }

        val res = rels.map { RelationResponse(
                it[UserRelations.from],
                it[UserRelations.to],
                it[UserRelations.createdAt].toString()
        ) }

        return res
    }

    @Authentication
    @GetMapping(
            "/relations/users/{id}"
    )
    @ApiOperation(
            value = "ユーザ間の関係の取得用",
            notes = "ユーザ間の関係を取得するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザ間の関係を取得できた。")),
                (ApiResponse(code = 404, message = "ユーザが存在しない。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "関係先のユーザのID") @PathVariable("id") id: UUID
                ): List<RelationResponse> {
        val query = transaction { UserRelations.select {
            ((UserRelations.from eq user) and (UserRelations.to eq id)) or ((UserRelations.from eq id) and (UserRelations.to eq user))
            }
        }

        return query.map {
            RelationResponse(
                    it[UserRelations.from],
                    it[UserRelations.to],
                    it[UserRelations.createdAt].toString()
            )
        }
    }


    @Authentication
    @DeleteMapping(
            "/relations/users/{id}"
    )
    @ApiOperation(
            value = "ユーザ間の関係の削除用",
            notes = "ユーザ間の関係を削除するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザ間の関係を削除できた。")),
                (ApiResponse( code = 403, message = "削除すべきユーザ間の関係がなかった。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun deleteUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "関係先のユーザのID") @PathVariable id: UUID
                ) {
        val count = transaction {
            UserRelations.deleteWhere {
                (UserRelations.from eq user) and (UserRelations.to eq id)
            }
        }

        if (count == 0) throw ForbiddenException()
    }
}