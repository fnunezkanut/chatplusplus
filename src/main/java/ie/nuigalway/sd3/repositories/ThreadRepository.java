package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.Thread;

import java.util.List;

public interface ThreadRepository {

    Thread getThread(Long id);

    List<Thread> getThreads();

    List<Thread> getThreadsByCustomerId(Long customerId);

    Long createThread(String title, Long customerId);

    void updateDtUpdated(Long id);
}
