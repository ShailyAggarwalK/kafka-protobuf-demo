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

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {DemoApplication.class}
)
//@EmbeddedKafka(partitions = 1, ports = {9092}, topics = {"embedded-test-topic"})
class DemoProducerIT {
    @Autowired
    KafkaConsumer<String, String> testConsumer;

    @Autowired
    KafkaConsumer<String,  AppointmentProtos.Appointment> testConsumerProto;

    @Autowired
    DemoProducer demoProducer;

    @Autowired
    DemoConsumer demoConsumer;

    @Value("${TOPIC_STRING}") String topicString;
    @Value("${TOPIC_PROTO}") String topicProto;

    @Test
    public void should_send_kafka_messages() {
        String data = "KAFKA TEST DATA";
        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(topicString, 0));

        demoProducer.sendMessage(data);
        testConsumer.assign(topicPartitions);

        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(testConsumer);

        records.forEach(it -> System.out.println(it.value()));
    }

    @Test
    void should_send_kafka_proto_message() {
         AppointmentProtos.Appointment appointment =  AppointmentProtos.Appointment.newBuilder().setAppointmentId("A12").setUserName("Ram").setWeekday(AppointmentProtos.Weekday.MONDAY).build();

        List<TopicPartition> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartition(topicProto, 0));
        testConsumerProto.assign(topicPartitions);

        demoProducer.sendMessage(appointment);
        ConsumerRecords<String,  AppointmentProtos.Appointment> records = KafkaTestUtils.getRecords(testConsumerProto);
        System.out.println("Total records " + records.count());
        records.forEach(it -> System.out.println(it.value()));
    }

   // ! ENTERS INFINITE LOOP !
    @Test
    void docker_kafka_test_demo_consumer() {
         AppointmentProtos.Appointment appointment =  AppointmentProtos.Appointment.newBuilder().setAppointmentId("A12").setUserName("Ram").setWeekday(AppointmentProtos.Weekday.MONDAY).build();

        demoProducer.sendMessage(appointment);

        demoConsumer.readMessages();
    }
}
