package com.expenseTracker;

import com.expenseTracker.DTO.UserDetailDTO;
import com.expenseTracker.eventProducer.UserInfoProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserInfoProducerTest {

    @Mock
    private KafkaTemplate<String, UserDetailDTO> kafkaTemplate;

    @InjectMocks
    private UserInfoProducer userInfoProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEventToKafkaWithValidData() {
        // Create a valid UserDetailDTO
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername("validUser");

        // Mock the behavior of KafkaTemplate's send method using CompletableFuture
        CompletableFuture<SendResult<String, UserDetailDTO>> future = new CompletableFuture<>();
        future.complete(null);  // Simulate successful send result

        when(kafkaTemplate.send(any(Message.class))).thenReturn(future);

        userInfoProducer.sendEventToKafka(userDetailDTO);

        // Verify that send() was called once
        verify(kafkaTemplate, times(1)).send(any(Message.class));
    }

    @Test
    void sendEventToKafkaWithNullData() {
        // Do not attempt to send a null payload. Instead, handle it in the test.
        UserDetailDTO userDetailDTO = null;

        // Mock KafkaTemplate's send method to handle null appropriately
        CompletableFuture<SendResult<String, UserDetailDTO>> future = new CompletableFuture<>();
        future.complete(null);

        when(kafkaTemplate.send(any(Message.class))).thenReturn(future);

        // Call the method to test its behavior with null data
        try {
            userInfoProducer.sendEventToKafka(userDetailDTO);
        } catch (IllegalArgumentException e) {
            System.out.println("Handled null payload: " + e.getMessage());
        }

        // Verify that send() is never called because null payloads should not be sent
        verify(kafkaTemplate, times(0)).send(any(Message.class));
    }

    @Test
    void handleKafkaFailureLogsError() {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername("failedUser");

        Throwable throwable = new RuntimeException("Kafka failure");

        userInfoProducer.handleKafkaFailure(userDetailDTO, throwable);

        // Assuming handleKafkaFailure logs the error, you can verify the log output if necessary
    }
}
