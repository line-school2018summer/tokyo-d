package com.proelbtn.linesc.request

// _d は必要がない。だが、これがないと上手くSpring Bootが認識してくれない。
class PostMessageRequest (val content: String, val _d: Unit?) {
    fun validate(): Boolean {
        return true
    }
}