package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JmsConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    /**
     * When a JMS message isn't properly received, the transaction is considered incomplete => JMS will send it again
     * Can test this feature in debug mode while throwing exception and looking into headers variable
     */
    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message) {

        System.out.println("Received a new message : \"" + helloWorldMessage + "\"");
        //throw new RuntimeException("foo");
    }

    /**
     * In debug mode, peek on springMessage variable to see its content
     * Spring message abstraction is preferable for maintenance when evolving from JMS to other message API
     * see {@link org.springframework.messaging.support.GenericMessage}
     */
    @JmsListener(destination = JmsConfig.MY_SEND_RCV_QUEUE)
    public void listenAndGreetLuffy(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message, org.springframework.messaging.Message<HelloWorldMessage> springMsg) throws JMSException {
        HelloWorldMessage payloadMsg = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message(helloWorldMessage.getMessage() + " I will become the Pirate King!")
                .build();

        //jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMsg);

        jmsTemplate.convertAndSend((Destination) springMsg.getHeaders().get("jms_replyTo"), payloadMsg);
    }
}
