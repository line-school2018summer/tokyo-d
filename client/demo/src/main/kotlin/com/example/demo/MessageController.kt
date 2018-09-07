package com.example.demo

import com.pusher.rest.Pusher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/message")
class MessageController {
    private val pusher = Pusher("APP_ID", "KEY", "SECRET")

    init {
        pusher.setCluster("CLUSTER")
    }

    @PostMapping
    fun postMessage(@RequestBody message: Message) : ResponseEntity<Unit> {
        pusher.trigger("chat", "new_message", message)
        return ResponseEntity.ok().build()
    }
}