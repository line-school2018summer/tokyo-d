package com.proelbtn.linesc.models.containers

import com.proelbtn.linesc.models.User

class HomeAdapterDataContainer {
    val data: HashMap<String, Pair<User, Int>> = HashMap()

    fun addFromRelation(user: User) {
        data[user.id] = Pair((data[user.id]?.first ?: user), (data[user.id]?.second ?: 0) or TYPE_KATAOMOI)
    }

    fun addToRelation(user: User) {
        data[user.id] = Pair((data[user.id]?.first ?: user), (data[user.id]?.second ?: 0) or TYPE_KATAOMOI)
    }

    fun getKeys(): Set<String> {
        return data.keys
    }

    fun getUserFromId(uid: String): User? {
        return data[uid]?.first
    }

    fun getRelationFromId(uid: String): Int? {
        return data[uid]?.second
    }

    fun getRelationFromUser(user: User): Int? {
        return data[user.id]?.second
    }

    fun getItemCount(): Int {
        return data.size
    }

    companion object {
        val TYPE_KATAOMOI: Int = 0x0F
        val TYPE_KATAOMOWARE: Int = 0xF0
        val TYPE_RYOOMOI: Int = TYPE_KATAOMOI or TYPE_KATAOMOWARE
    }
}
