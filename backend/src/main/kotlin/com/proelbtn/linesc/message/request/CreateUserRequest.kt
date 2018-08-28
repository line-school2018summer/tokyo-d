package com.proelbtn.linesc.message.request

import com.proelbtn.linesc.validator.*

class CreateUserRequest (val sid: String, val name: String, val pass: String) {
    fun validate(): Boolean {
        return validate_sid(sid) && validate_name(name) && validate_pass(pass)
    }
}
