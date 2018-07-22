package net.sergey.kosov.communication.controllers

import net.sergey.kosov.communication.domains.Message
import net.sergey.kosov.communication.services.SseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import javax.validation.Valid


@RestController
class SseController @Autowired constructor(var sseService: SseService) {

    @RequestMapping(path = ["/chat"], method = [(RequestMethod.POST)])
    fun sendMessage(@Valid message: Message): Message {
        return sseService.sendMessage(message)
    }
}

