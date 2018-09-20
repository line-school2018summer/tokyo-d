package com.proelbtn.linesc.validator

fun validate_id(id: String): Boolean {
    return true
}

fun validate_sid(sid: String): Boolean {
    return sid.isNotEmpty()
}

fun validate_name(name: String): Boolean {
    return name.isNotEmpty()
}

fun validate_pass(pass: String): Boolean {
    return pass.isNotEmpty()
}

