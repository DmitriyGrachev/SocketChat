package ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MessageQueue {

    Deque<Message> message = new ConcurrentLinkedDeque<>();

    public synchronized void addMessageInQueue(Message messageToAdd){
        message.add(messageToAdd);
        notifyAll();
    }

    public synchronized Message getMessageFromQueue()  {
        while (message.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return message.poll();
    }

}

