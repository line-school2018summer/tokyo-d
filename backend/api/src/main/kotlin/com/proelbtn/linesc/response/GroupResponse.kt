package com.proelbtn.linesc.response

import java.util.*;

class GroupResponse(
        val id: UUID,
        val sid: String,
        val name: String,
        val owner: UUID,
        val created_at: String,
        val updated_at: String
        )