package com.proelbtn.linesc.controller

import com.proelbtn.linesc.annotation.Authentication
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
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserRelationsController {
    @Authentication
    @PostMapping(
            "/relations/users"
    )
    @ApiOperation(
            value = "ユーザ間の関係の作成用",
            notes = "ユーザ間の関係を作成するときに使用するエンドポイント",
            response = RelationResponse::class
    )
    @ApiResponses(
            value = [
                (ApiResponse(code = 200, message = "正常にユーザ間の関係を作成できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun createUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "作成するユーザ間の関係の情報") @RequestBody req: CreateRelationRequest
                ): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!req.validate()) status = HttpStatus.BAD_REQUEST
        if (user != req.from) status = HttpStatus.BAD_REQUEST
        if (req.from == req.to) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(req.from)
            val tid = UUID.fromString(req.to)
            val now = DateTime.now()

            transaction {
                val query = UserRelations.select {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }.firstOrNull()

                if (query != null) status = HttpStatus.BAD_REQUEST

                if (status == HttpStatus.OK) {
                    UserRelations.insert {
                        it[from] = fid
                        it[to] = tid
                        it[createdAt] = now
                    }
                }
            }

            if (status == HttpStatus.OK) {
                res = RelationResponse(req.from, req.to, now.toString())
            }

        }

        return ResponseEntity(res, status)
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
                (ApiResponse( code = 200, message = "正常にユーザ間の関係を取得できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getUserRelations(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String
                ): ResponseEntity<List<RelationResponse>> {
        var res: List<RelationResponse>? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val uid = UUID.fromString(user)

            val rels = transaction { UserRelations.select {
                    (UserRelations.from eq uid) or (UserRelations.to eq uid)
                }.toList()
            }

            res = rels.map { RelationResponse(
                    it[UserRelations.from].toString(),
                    it[UserRelations.to].toString(),
                    it[UserRelations.createdAt].toString()
            ) }
        }

        return ResponseEntity(res, status)
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
                (ApiResponse( code = 200, message = "正常にユーザ間の関係を取得できた。")),
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。"))
            ]
    )
    fun getUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "関係先のユーザのID") @PathVariable("id") id: String
                ): ResponseEntity<RelationResponse> {
        var res: RelationResponse? = null
        var status: HttpStatus = HttpStatus.OK

        // validation
        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val rel = transaction { UserRelations.select {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }.firstOrNull()
            }

            if (rel == null) status = HttpStatus.NOT_FOUND
            else {
                res = RelationResponse(
                        rel[UserRelations.from].toString(),
                        rel[UserRelations.to].toString(),
                        rel[UserRelations.createdAt].toString()
                )
            }
        }

        return ResponseEntity(res, status)
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
                (ApiResponse( code = 400, message = "引数が足りない・正しくない。")),
                (ApiResponse( code = 404, message = "削除すべきユーザ間の関係がなかった。"))
            ]
    )
    fun deleteUserRelation(
            @ApiParam(value = "認証されたユーザのID（トークンに含まれる）") @RequestAttribute("user") user: String,
            @ApiParam(value = "関係先のユーザのID") @PathVariable id: String
                ): ResponseEntity<Unit> {
        var status = HttpStatus.OK

        if (!validate_id(user) || !validate_id(id)) status = HttpStatus.BAD_REQUEST

        if (status == HttpStatus.OK) {
            val fid = UUID.fromString(user)
            val tid = UUID.fromString(id)

            val count = transaction {
                UserRelations.deleteWhere {
                    (UserRelations.from eq fid) and (UserRelations.to eq tid)
                }
            }

            if (count == 0) status = HttpStatus.NOT_FOUND
        }

        return ResponseEntity(status)
    }
}