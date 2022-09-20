package com.kafka.demo.producer;

import com.kafka.demo.proto.AppointmentProtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class DemoProducer {

    @Value("${TOPIC_STRING}") String topicString;
    @Value("${TOPIC_PROTO}") String topicProto;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String,  AppointmentProtos.Appointment> kafkaTemplateProto;

    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicString, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendMessage( AppointmentProtos.Appointment message) {
        ListenableFuture<SendResult<String,  AppointmentProtos.Appointment>> future = kafkaTemplateProto.send(topicProto, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String,  AppointmentProtos.Appointment>>() {

            @Override
            public void onSuccess(SendResult<String,  AppointmentProtos.Appointment> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
