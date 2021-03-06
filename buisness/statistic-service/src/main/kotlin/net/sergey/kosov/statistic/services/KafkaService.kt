package net.sergey.kosov.statistic.services

import net.sergey.kosov.statistic.companions.KafkaProperties
import net.sergey.kosov.statistic.domains.KafkaData
import net.sergey.kosov.statistic.domains.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import javax.servlet.http.HttpSession

@Service
class KafkaService @Autowired constructor(private val kafkaTemplate: KafkaTemplate<String, KafkaData>,
                                          private val kafkaProperties: KafkaProperties) {

    fun send(session: HttpSession, login: String?, product: Product) {
        val id = login ?: session.id
        kafkaProperties.procurerTopics.forEach { kafkaTemplate.send(it, id, KafkaData(id, product)) }
    }
}