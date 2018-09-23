package com.proelbtn.linesc.models.responses

import com.proelbtn.linesc.models.User

data class RelationResponse (val from: List<User>, val to: List<User>)
