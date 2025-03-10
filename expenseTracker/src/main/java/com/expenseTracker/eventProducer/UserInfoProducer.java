package com.expenseTracker.eventProducer;


import com.expenseTracker.DTO.UserDetailDTO;
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


    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;
    @Autowired
    UserInfoProducer(KafkaTemplate<String,UserDetailDTO> kafkaTemplate){

        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEventToKafka(UserDetailDTO UserDetailDTO){
        Message<UserDetailDTO> message= MessageBuilder.withPayload(UserDetailDTO)
                .setHeader(KafkaHeaders.TOPIC,TOPIC_NAME).build();

        kafkaTemplate.send(message);
    }

//    UserInfoProducer userInfoProducer =new UserInfoProducer(Inject);
}
