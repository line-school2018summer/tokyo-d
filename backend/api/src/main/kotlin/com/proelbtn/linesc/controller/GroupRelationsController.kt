package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
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
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を作成できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun createGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するユーザとグループの関係の情報") @RequestBody req: CreateRelationRequest
                ): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(req.from)
            val tid = UUID.fromString(req.to)
            val now = DateTime.now()

            transaction {
                if (status == HttpStatus.OK) {
                    val query = UserGroupRelations.select {
                        (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                    }.firstOrNull()

                    if (query != null) status = HttpStatus.BAD_REQUEST
                }

                if (status == HttpStatus.OK) {
                    UserGroupRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK)
                res = RelationResponse(req.from, req.to, now.toString())


        }

        return ResponseEntity(res, status)
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
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を取得できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getGroupRelations(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String
                ): ResponseEntity<List<RelationResponse>> {
        var res: List<RelationResponse>? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val uid = UUID.fromString(user)

            val rels = transaction { UserGroupRelations.select { UserGroupRelations.from eq uid }.toList() }

            res = rels.map { RelationResponse(
                    it[UserGroupRelations.from].toString(),
                    it[UserGroupRelations.to].toString(),
                    it[UserGroupRelations.createdAt].toString()
            ) }
        }

        return ResponseEntity(res, status)
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
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "関係先のグループのID") @PathVariable("id") id: String
                ): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val rel = transaction { UserGroupRelations.select {
                    (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                }.firstOrNull()
            }

            if (rel == null) status = HttpStatus.NOT_FOUND
            else {
                res = RelationResponse(
                        rel[UserGroupRelations.from].toString(),
                        rel[UserGroupRelations.to].toString(),
                        rel[UserGroupRelations.createdAt].toString()
                )
            }
        }

        return ResponseEntity(res, status)
    }

    @Authentication
    @DeleteMapping(
            "/relations/groups/{id}"
    )
    @ApiOperation(
            value = "ユーザとグループの関係の削除用",
            notes = "ユーザとグループの関係を削除するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse( code = 200, message = "正常にユーザとグループの関係を削除できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。")),
                (ApiResponse( code = 404, message = "削除すべきユーザとグループの関係がなかった。"))
            ]
    )
    fun deleteGroupRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "関係先のグループのID") @PathVariable id: String
                ): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val count = transaction {
                UserGroupRelations.deleteWhere {
                    (UserGroupRelations.from eq fid) and (UserGroupRelations.to eq tid)
                }
            }

            if (count == 0) status = HttpStatus.NOT_FOUND
        }

        return ResponseEntity(status)
    }
}