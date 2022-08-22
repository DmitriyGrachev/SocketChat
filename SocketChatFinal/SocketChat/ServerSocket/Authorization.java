package ServerSocket;

import java.io.IOException;
public class Authorization {
    ClientThread clientThread;
    private String server = "Server";
    public Authorization(ClientThread clientThread) {
        this.clientThread = clientThread;
    }
    private void userInitialRegistration() throws IOException {
        while (true) {
            clientThread.sendMessageToPublicSession(server, "Введите логи");
            String log = clientThread.getBufferedReader().readLine();
            log = log.trim();

            if (log.isEmpty()) {
                clientThread.sendMessageToPublicSession(server, "Неверный логин");
                continue;
            }
            try {
                if(ifUserIsAlreadyLogged(log)){
                    clientThread.sendMessageToPublicSession(server, "Запущенна другая сессия");
                    continue;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (UserSocket u : clientThread.getUsers()) {
                if (u.getLoginOfUser().equals(log)) {
                    clientThread.sendMessageToPublicSession(server, "Введите пороль");
                    String pass = clientThread.getBufferedReader().readLine();
                    pass = pass.trim();
                    if (u.getPasswordOfUser().equals(pass)) {
                        clientThread.sendMessageToPublicSession(server, "Вы авторизировались " + u.getLoginOfUser());
                        clientThread.sendMessageToPublicSession(server, u.getLoginOfUser() + " удачной сесси");
                        clientThread.getUser().setLoginOfUser(log);
                        clientThread.getUser().setPasswordOfUser(pass);
                        clientThread.getUser().setIfUserIsLogged(true);
                        return;
                    } else {
                        clientThread.sendMessageToPublicSession(server, "Неправельный пароль либо логин");
                        break;
                    }
                }
            }
            clientThread.sendMessageToPublicSession(server, "Такого юзера не ссущесствует");
        }
    }
    private void initialRegistartion() throws IOException {
        while (true) {
            clientThread.sendMessageToPublicSession(server, "Введите логин");
            String log = clientThread.getBufferedReader().readLine();
            log = log.trim();

            if (log.isEmpty()) {
                clientThread.sendMessageToPublicSession(server, "Неправельный логин");
                continue;
            }
            if (ifUserIsAlreadyRegistrated(log)) {
                clientThread.sendMessageToPublicSession(server, "Такой логин уже существует");
                continue;
            }
            clientThread.sendMessageToPublicSession(server, "Введите пароль");
            String pass = clientThread.getBufferedReader().readLine();
            pass = pass.trim();
            clientThread.getUser().setLoginOfUser(log);
            clientThread.getUser().setPasswordOfUser(pass);
            clientThread.getUser().setIfUserIsLogged(true);
            clientThread.getUsers().add(clientThread.getUser());
            clientThread.getUserSocketConf().saveUsersInData();
            clientThread.sendMessageToPublicSession(server, "Регистрация прошла успешно" + clientThread.getUser().getLoginOfUser());
            return;
        }
    }
    private boolean ifUserIsAlreadyRegistrated(String log) {
        for (UserSocket u : clientThread.getUsers()) {
            if (u.getLoginOfUser().equals(log) || u.getLoginOfUser() == log) {
                return true;
            }
        }
        return false;
    }
    private boolean ifUserIsAlreadyLogged(String log) throws Exception {
        for (ClientThread cT : clientThread.getClientThreads()) {
            if (cT.getUser().getLoginOfUser().equals(log) || cT.getUser().getLoginOfUser() == log) {
                return true;
            }
        }
        return false;
    }
    public void comparisonUser() throws IOException {
        while (true) {
            clientThread.sendMessageToPublicSession(server, "Вы зарегестрирован?");
            String answer = clientThread.getBufferedReader().readLine();
            if (answer.equals("Да")) {
                userInitialRegistration();
                break;
            } else if (answer.equals("Нет")) {
                initialRegistartion();
                break;
            } else {
                clientThread.sendMessageToPublicSession(server, "Incorrect answer...");
            }
        }
    }
    public boolean ifServerIsFull()  {
        if(clientThread.getClientThreads().size() > DnsServer.finalNumberOfClients){
            try {
                clientThread.sendMessageToPublicSession(server, "Сервер заполнен");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                clientThread.getBufferedWriter().append("exit").append('\n').flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                clientThread.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return  false;
    }
}


