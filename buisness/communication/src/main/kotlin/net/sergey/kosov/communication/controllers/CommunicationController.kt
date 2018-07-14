package net.sergey.kosov.communication.controllers

import net.sergey.kosov.communication.services.CommunicationService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CommunicationController @Autowired constructor(var service: CommunicationService) {

    @PostMapping("/send")
    fun send(message: String, protocol: String, to: String) {
        service.createAndSend(message, protocol, to)
    }

    @GetMapping(path = ["/completed/{messageId}"], consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @PreAuthorize("#oauth2.hasScope('server')")
    fun completedMessage(@PathVariable("messageId") messageId: String) {
        service.completeMessage(ObjectId(messageId))
    }
}