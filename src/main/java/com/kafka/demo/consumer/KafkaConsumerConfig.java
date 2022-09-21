package com.kafka.demo.consumer;

import com.kafka.demo.proto.AppointmentProtos;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfig {
    @Value("${BOOTSTRAP_SERVERS_URL}")
    private String bootstrapServers;
    @Value("${SCHEMA_REGISTRY_URL}")
    private String schemaRegistryUrl;
    @Value("${CONSUMER_GROUP_ID}")
    private String consumerGroupId;

    @Bean
    public Properties kafkaConsumerPropertiesProto() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        properties.put(KafkaProtobufDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        properties.put(KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE,  AppointmentProtos.Appointment.class.getName());

        return properties;
    }

    @Bean
    public KafkaConsumer<String, AppointmentProtos.Appointment> kafkaConsumerProto() {
        return new KafkaConsumer<>(kafkaConsumerPropertiesProto());
    }
}
