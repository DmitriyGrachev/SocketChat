package ServerSocket;
import ServerSocket.ClientThread;
public class Message {
    ClientThread clientThread;
    String message;
    public Message() {
        this.clientThread = null;
        this.message = "";
    }
    public Message(ClientThread clientThread, String message) {
        this.clientThread = clientThread;
        this.message = message;
    }
}

