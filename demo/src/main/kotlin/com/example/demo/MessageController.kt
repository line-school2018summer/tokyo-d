package com.example.demo

import com.pusher.rest.Pusher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/message")
class MessageController {
    private val pusher = Pusher("584320", "6792826229b059405caf", "2b1094484f0bb4018e79")

    init {
        pusher.setCluster("ap1")
    }

    @PostMapping
    fun postMessage(@RequestBody message: Message) : ResponseEntity<Unit> {
        pusher.trigger("chat", "new_message", message)
        return ResponseEntity.ok().build()
    }
}