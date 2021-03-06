package net.sergey.kosov.communication.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigurationMQ {
    @Value("\${rabbit.hostname}")
    lateinit var hostname: String

    //настраиваем соединение с RabbitMQ
    @Bean
    fun connectionFactory(): ConnectionFactory {
        return CachingConnectionFactory(hostname)
    }

    @Bean
    fun amqpAdmin(): AmqpAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory())
        template.messageConverter = jsonMessageConverter()
        return template
    }

    //объявляем очередь с именем telegram
    @Bean
    fun telegramQueue(): Queue {
        return Queue("telegram")
    }

    @Bean
    fun internalQueue(): Queue {
        return Queue("internal")
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        val mapper = ObjectMapper().findAndRegisterModules()
        return Jackson2JsonMessageConverter(mapper)
    }
}