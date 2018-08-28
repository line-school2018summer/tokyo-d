package com.proelbtn.linesc.request

import com.proelbtn.linesc.validator.validate_id

class CreateRelationRequest(val from: String, val to: String) {
    fun validate(): Boolean {
        return validate_id(from) && validate_id(to)
    }
}