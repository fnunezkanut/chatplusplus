package ie.nuigalway.sd3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.nuigalway.sd3.ApplicationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.simp.stomp.StompHeaders;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.lang.reflect.Type;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static java.util.concurrent.TimeUnit.SECONDS;


//using a defined port here (8080)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("default")
public class ChatSubmitControllerTest {

    static final String WEBSOCKET_URI = "ws://localhost:8080/stomp";
    static final String WEBSOCKET_TOPIC = "/topic/chat";


    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;

    @Before
    public void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient( asList(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    public void should_response_with_invalid_input() throws Exception {

        long threadId = 1L;

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {} )
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        String message = "MESSAGE TEST";
        session.send( "/thread/" + threadId, message.getBytes());

        String response = blockingQueue.poll(1, SECONDS);
        assertEquals( true, response.equals("{\"status\":\"error\",\"message\":\"Invalid input\",\"payload\":{}}") );
    }


    @Test
    public void should_add_message_to_thread() throws Exception {

        long threadId = 1L;

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {} )
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        String message = "{ \"message\" : \"websocket_test\", \"user_id\" : \"1\" }";
        session.send( "/thread/" + threadId, message.getBytes());

        String response = blockingQueue.poll(1, SECONDS);

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response, ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("added"));
    }



    @Test
    public void should_fetch_messages_for_thread() throws Exception {

        long threadId = 1L;

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {} )
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        //notice empty message
        String message = "{ \"message\" : \"\", \"user_id\" : \"1\" }";
        session.send( "/thread/" + threadId, message.getBytes());

        String response = blockingQueue.poll(1, SECONDS);

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response, ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("ok"));
        assertEquals( true, ar.getMessage().toLowerCase().equals("retrieved"));
    }



    @Test
    public void should_fail_as_no_user_id() throws Exception {

        long threadId = 1L;

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {} )
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        //notice empty message
        String message = "{ \"message\" : \"\", \"blah\" : \"1\" }";
        session.send( "/thread/" + threadId, message.getBytes());

        String response = blockingQueue.poll(1, SECONDS);

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response, ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
    }


    @Test
    public void should_fail_as_no_such_thread() throws Exception {

        long threadId = 0L;

        StompSession session = stompClient
                .connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {} )
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        //notice empty message
        String message = "{ \"message\" : \"\", \"user_id\" : \"1\" }";
        session.send( "/thread/" + threadId, message.getBytes());

        String response = blockingQueue.poll(1, SECONDS);

        //map response to response object
        ObjectMapper mapper = new ObjectMapper();
        ApplicationResponse ar = mapper.readValue( response, ApplicationResponse.class );

        assertEquals( true, ar.getStatus().toLowerCase().equals("error"));
    }




    //frame handler for stomp socket
    class DefaultStompFrameHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }
}