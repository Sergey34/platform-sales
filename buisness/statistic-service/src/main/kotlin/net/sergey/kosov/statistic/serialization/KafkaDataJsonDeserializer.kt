package net.sergey.kosov.statistic.serialization


import net.sergey.kosov.statistic.domains.KafkaData
import org.springframework.kafka.support.serializer.JsonDeserializer

class KafkaDataJsonDeserializer : JsonDeserializer<KafkaData>()