package com.proelbtn.linesc.models.containers

import com.proelbtn.linesc.models.Message
import java.util.*

class ChatAdapterDataContainer {
    val deque: LinkedList<Message> = LinkedList()

    fun getItemCount(): Int {
        return deque.size
    }

    fun getFirst(): Message {
        return deque.first
    }

    fun getLast(): Message {
        return deque.last
    }

    fun get(pos: Int): Message {
        return deque.get(pos)
    }

    fun addFirst(message: Message) {
        deque.addFirst(message)
    }

    fun addLast(message: Message) {
        deque.addLast(message)
    }
}