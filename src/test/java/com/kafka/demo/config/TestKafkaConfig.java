package com.kafka.demo.config;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializerConfig;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@Configuration
public class TestKafkaConfig {
    @Value("${BOOTSTRAP_SERVERS_URL}")
    private String bootstrapServers;
    @Value("${SCHEMA_REGISTRY_URL}")
    private String schemaRegistry;

    @Bean
    public KafkaConsumer<String, String> testConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(bootstrapServers, "test-group", "true");
        return new KafkaConsumer<>(consumerProps, new StringDeserializer(), new StringDeserializer());
    }
}
