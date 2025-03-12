package com.expenseTracker.eventProducer;


import com.expenseTracker.DTO.UserDetailDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoProducer {

    private final KafkaTemplate<String, UserDetailDTO> kafkaTemplate;

    private static final String CIRCUIT_BREAKER_NAME = "kafkaCircuitBreaker";


    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    @Autowired
    UserInfoProducer(KafkaTemplate<String,UserDetailDTO> kafkaTemplate){

        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "handleKafkaFailure")
    public void sendEventToKafka(UserDetailDTO UserDetailDTO){
        Message<UserDetailDTO> message= MessageBuilder.withPayload(UserDetailDTO)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();

        kafkaTemplate.send(message);
    }

    public void handleKafkaFailure(UserDetailDTO userDetailDTO, Throwable t) {
        System.err.println("Kafka is down! Could not send event: " + userDetailDTO);
        // Log the failure or store the event in a database for retry
    }

//    UserInfoProducer userInfoProducer =new UserInfoProducer(Inject);
}
