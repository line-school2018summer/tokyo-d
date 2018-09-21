package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
import com.proelbtn.linesc.exceptions.BadRequestException
import com.proelbtn.linesc.exceptions.NotFoundException
import com.proelbtn.linesc.request.CreateRelationRequest
import com.proelbtn.linesc.response.RelationResponse
import com.proelbtn.linesc.model.UserGroupRelations
import com.proelbtn.linesc.validator.validate_id
import io.swagger.annotations.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class GroupRelationsController {
    @Authentication
    @PostMapping(
            "/relations/groups"
    )
    @ApiOperation(
            value = "ユーザとグループの関係の作成用",
            notes = "ユーザとグループの関係を作成するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を作成できた。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun createGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "作成するユーザとグループの関係の情報") @RequestBody req: CreateRelationRequest
                ): RelationResponse? {
        val fid = req.from
        val tid = req.to
        val now = DateTime.now()

        transaction {
            val query = UserGroupRelations.select {
                (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
            }.firstOrNull()

            if (query != null) throw BadRequestException()

            UserGroupRelations.insert {
                it[from] = fid
                it[to] = tid
                it[createdAt] = now
            }
        }

        return null //RelationResponse(req.from, req.to, now.toString())

    }

    @Authentication
    @GetMapping(
            "/relations/groups"
    )
    @ApiOperation(
            value = "ユーザとグループの関係の取得用",
            notes = "ユーザとグループの関係を取得するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を取得できた。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getGroupRelations(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID
                ): List<RelationResponse>? {
        val rels = transaction { UserGroupRelations.select { UserGroupRelations.from eq user }.toList() }

        return null

        /*rels.map { RelationResponse(
                it[UserGroupRelations.from],
                it[UserGroupRelations.to],
                it[UserGroupRelations.createdAt].toString()
        ) }*/

    }

    @Authentication
    @GetMapping(
            "/relations/groups/{id}"
    )
    @ApiOperation(
            value = "ユーザとグループの関係の取得用",
            notes = "ユーザとグループの関係を取得するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を取得できた。")),
                (ApiResponse( code = 404, message = "ユーザとグループの関係が存在しなかった。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun getGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "関係先のグループのID") @PathVariable("id") id: UUID
                ): RelationResponse? {
        val rel = transaction { UserGroupRelations.select {
                (UserGroupRelations.from eq user) and (UserGroupRelations.to eq id)
            }.firstOrNull()
        }

        if (rel == null) throw NotFoundException()

        return null /*RelationResponse(
                rel[UserGroupRelations.from],
                rel[UserGroupRelations.to],
                rel[UserGroupRelations.createdAt].toString()
        )*/
    }

    @Authentication
    @DeleteMapping(
            "/relations/groups/{id}"
    )
    @ApiOperation(
            value = "ユーザとグループの関係の削除用",
            notes = "ユーザとグループの関係を削除するときに使用するエンドポイント",
            response = Unit::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を削除できた。")),
                (ApiResponse( code = 403, message = "削除すべきユーザとグループの関係がなかった。"))
            ]
    )
    @ResponseStatus(HttpStatus.OK)
    fun deleteGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: UUID,
            @ApiParam(value = "関係先のグループのID") @PathVariable id: UUID
                ) {
        val count = transaction {
            UserGroupRelations.deleteWhere {
                (UserGroupRelations.from eq user) and (UserGroupRelations.to eq id)
            }
        }

        if (count == 0) NotFoundException()
    }
}