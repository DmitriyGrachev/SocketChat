package ServerSocket;
public class MessageSender implements Runnable {
    MessageQueue messageQueue;
    Message message;
    private String server = "Server";
    public MessageSender(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }
    private void checkTypeOfMesage() throws Exception {
        message = messageQueue.getMessageFromQueue();
        if (message.message.equals("exit")) {
            message.clientThread.sendMessageToPublicSession(server, message.clientThread.getUser().getLoginOfUser());
            message.clientThread.getBufferedWriter().append("exit").flush();
            message.clientThread.close();
            return;
        }

        if (message.message.isEmpty())
            return;
        if (message.message.indexOf('@') == -1) {
            for (ClientThread clientThread : message.clientThread.getClientThreads()) {
                if (clientThread.getUser().checkIfUserIsLogged())
                    clientThread.sendMessageToPublicSession(message.clientThread.getUser().getLoginOfUser(), message.message);
            }
        } else if(message.message.indexOf('@') != -1) {
            String target = message.message.substring(message.message.indexOf('@') + 1, message.message.indexOf(' '));
            for (ClientThread clientThread : message.clientThread.getClientThreads()) {
                if (clientThread.getUser().getLoginOfUser().equals(target)) {
                    clientThread.sendMessageToPrivateThread(message.clientThread, message.message.substring(message.message.indexOf(' ') + 1));
                    break;
                }
            }
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                checkTypeOfMesage();
            } catch (Exception e) {
                break;
            }
        }
    }
}

