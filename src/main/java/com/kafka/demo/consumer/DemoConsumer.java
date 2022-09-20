package com.kafka.demo.consumer;

import com.kafka.demo.proto.AppointmentProtos;
import java.time.Duration;
import java.util.Collections;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemoConsumer {

    @Value("${TOPIC_STRING}") String topicString;
    @Value("${TOPIC_PROTO}") String topicProto;

    @Autowired
    private KafkaConsumer<String,  AppointmentProtos.Appointment> kafkaConsumer;

    public void readMessages() {
        kafkaConsumer.subscribe(Collections.singleton(topicProto));

        while (true) {
            ConsumerRecords<String,  AppointmentProtos.Appointment> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String,  AppointmentProtos.Appointment> record : records) {
                System.out.println("Appointment Id: " + record.value().getAppointmentId());
                System.out.println("Username: " + record.value().getUserName());
                System.out.println("Weekday: " + record.value().getWeekday());
            }
            kafkaConsumer.commitAsync();
        }
    }
}
