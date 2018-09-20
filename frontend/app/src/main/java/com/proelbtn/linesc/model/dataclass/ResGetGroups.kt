package com.proelbtn.linesc.model.dataclass

data class ResGetGroups (
        var created_at: String,
        var id: String,
        var name: String,
        val owner: String,
        var sid: String,
        var updated_at: String
)