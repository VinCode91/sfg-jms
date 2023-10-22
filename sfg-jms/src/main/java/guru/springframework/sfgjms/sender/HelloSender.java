package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 1000)
    public void sendMessage() {
//        System.out.println("Sending message...");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello World Sunshine")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
//        System.out.println("Message sent.");
    }

    @Scheduled(fixedRate = 4000)
    public void sendAndReceiveMessage() throws JMSException {
        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello World.")
                .build();

        Message receivedMsg = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RCV_QUEUE, session -> {
            try {
                Message helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
                return helloMessage;
            } catch (JsonProcessingException e) {
                throw new JMSException("boom!");
            }
        });
        System.out.println(receivedMsg.getBody(String.class));
    }
}
