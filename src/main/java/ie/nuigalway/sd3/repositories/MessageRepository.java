package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.MessageView1;

import java.util.List;

public interface MessageRepository {

    List<MessageView1> getMessagesByThreadId(Long threadId);

    Long addMessageToThread(Long threadId, Long userId, String comment);
}
