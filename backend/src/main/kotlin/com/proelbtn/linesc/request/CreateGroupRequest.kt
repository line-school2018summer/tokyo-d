package com.proelbtn.linesc.request

import com.proelbtn.linesc.validator.validate_name
import com.proelbtn.linesc.validator.validate_sid

class CreateGroupRequest(val sid: String, val name: String) {
    fun validate(): Boolean {
        return validate_sid(sid) && validate_name(name)
    }
}