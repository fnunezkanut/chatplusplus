package ie.nuigalway.sd3.repositories;

import ie.nuigalway.sd3.entities.Thread;

import java.util.List;

public interface ThreadRepository {

    List<Thread> getThreads();

    Thread getThread(Long id);

    Long createThread(String title, Long customerId );

    void updateDtUpdated(Long id);
}
