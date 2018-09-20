package com.proelbtn.linesc.response

import java.util.*

class MessageResponse (val id: UUID, val from: UUID, val to: UUID, val content: String, val created_at: String)