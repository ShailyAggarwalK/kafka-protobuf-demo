package com.kafka.demo.producer;

import com.kafka.demo.DemoApplication;
import com.kafka.demo.consumer.DemoConsumer;
import com.kafka.demo.proto.AppointmentProtos;
import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {DemoApplication.class}
)
@EmbeddedKafka(partitions = 1, ports = {9092}, topics = {"test-appointment"})
class DemoProducerIT {
    @Autowired
    KafkaConsumer<String,  AppointmentProtos.Appointment> testConsumerProto;

    @Autowired
    DemoProducer demoProducer;
    @Autowired
    DemoConsumer demoConsumer;

    @Value("${TOPIC_PROTO}") String topicProto;

    @Test
    void should_send_kafka_proto_message() {
         AppointmentProtos.Appointment appointment =  AppointmentProtos.Appointment.newBuilder().setAppointmentId("A12").setUserName("Ram").setWeekday(AppointmentProtos.Weekday.MONDAY).build();

        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(topicProto, 0));
        testConsumerProto.assign(topicPartitions);

        demoProducer.sendMessage(appointment);
        ConsumerRecords<String,  AppointmentProtos.Appointment> records = KafkaTestUtils.getRecords(testConsumerProto);

        assertEquals(1, records.count());
        records.forEach(it -> assertEquals(it.value(), appointment));
    }

   // ! ENTERS INFINITE LOOP !
    @Test
    void docker_kafka_test_demo_consumer() {
         AppointmentProtos.Appointment appointment =  AppointmentProtos.Appointment.newBuilder().setAppointmentId("A12").setUserName("Ram").setWeekday(AppointmentProtos.Weekday.MONDAY).build();

        demoProducer.sendMessage(appointment);
        demoConsumer.readMessages();
    }
}
