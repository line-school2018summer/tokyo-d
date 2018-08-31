package com.proelbtn.linesc.request

import com.proelbtn.linesc.validator.validate_pass
import com.proelbtn.linesc.validator.validate_sid

class GetTokenRequest (val sid: String?, val pass: String?){
    fun validate(): Boolean {
        if (sid == null || pass == null) return false
        return validate_sid(sid) && validate_pass(pass)
    }
}