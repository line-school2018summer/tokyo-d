package com.proelbtn.linesc.models.dataclass

data class ResGetGroups (
        var created_at: String,
        var id: String,
        var name: String,
        val owner: String,
        var sid: String,
        var updated_at: String
)