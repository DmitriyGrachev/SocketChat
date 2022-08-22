package ServerSocket;

import java.io.*;
import java.net.Socket;
import java.util.List;
public class ClientThread implements Runnable, AutoCloseable {
    private final UserSocket user;
    public UserSocket getUser() {
        return user;
    }
    private final List<UserSocket> users;
    public List<UserSocket> getUsers() {
        return users;
    }
    private final List<ClientThread> clientThreads;

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }
    private final BufferedWriter bufferedWriter;
    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }
    private final BufferedReader bufferedReader;
    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }
    private final UserSocketConf userSocketConf;
    public UserSocketConf getUserSocketConf() {
        return userSocketConf;
    }
    private final Authorization authorization;
    private final MessageQueue messageQueue;
    public ClientThread(List<ClientThread> clientThreads, Socket socket, UserSocketConf userSocketConf, MessageQueue messageQueue) {
        this.clientThreads = clientThreads;
        this.userSocketConf = userSocketConf;
        this.messageQueue = messageQueue;
        this.users = userSocketConf.getUserSockets();
        this.user = new UserSocket();
        this.authorization = new Authorization(this);
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessageToPublicSession(String login, String text) throws IOException {
        this.bufferedWriter.append(login).append(" : ").append(text + '\n').flush();
    }
    public void sendMessageToPrivateThread(ClientThread clientThread, String text) throws IOException {
        this.bufferedWriter.append("Сообщение отправлено :").append(clientThread.user.getLoginOfUser()).append(" : ").append(text + '\n').flush();
        clientThread.bufferedWriter.append("Сообщение для ").append(this.user.getLoginOfUser()).append(" : ").append(text + '\n').flush();
    }
    @Override
    public void close() throws Exception {
        clientThreads.remove(this);
        bufferedWriter.close();
        bufferedReader.close();
    }
    @Override
    public void run() {
        try {
            if (!authorization.ifServerIsFull()){
                authorization.comparisonUser();
                while (true) {
                    String inputText = bufferedReader.readLine();
                    messageQueue.addMessageInQueue(new Message(this,inputText));
                }
            }
        } catch (Exception e) {
            try {
                close();
            } catch (Exception ignored) {
            }
            throw new RuntimeException("ошибка 404");
        }
    }
}
