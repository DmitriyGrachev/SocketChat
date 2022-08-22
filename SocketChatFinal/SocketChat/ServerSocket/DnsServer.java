package ServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class DnsServer {
    public static final int finalNumberOfClients = 20;
    public static void main(String[] args)  {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            List<ClientThread> clientThreads = new ArrayList<>();
            UserSocketConf userSocketConf = new UserSocketConf();
            MessageQueue messageQueue = new MessageQueue();
            MessageSender messageSender = new MessageSender(messageQueue);
            Thread messageSenderThread = new Thread(messageSender);
            messageSenderThread.setName("send");
            messageSenderThread.setDaemon(true);
            messageSenderThread.start();

            while (true) {
                Socket socket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientThreads,socket,userSocketConf,messageQueue);
                Thread newUserThread = new Thread(clientThread);
                newUserThread.setDaemon(true);
                newUserThread.start();
                clientThreads.add(clientThread);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



