package ServerSocket;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
public class UserSocketConf  {
    private final File file;
    private List<UserSocket> userSockets;

    public UserSocketConf() throws IOException {
        this.file = new File("UserList.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    public String getAppended() {
        StringBuilder sb = new StringBuilder();
        for (UserSocket u : userSockets) {
            sb.append(u.getLoginOfUser()).append(' ').append(u.getPasswordOfUser()).append('\n');
        }
        return sb.toString();
    }
    public List<UserSocket> getUserSockets() {
        if (!file.exists())
            return new ArrayList<>();

        this.userSockets = new ArrayList<>();
        List<String> ofUser = getConfigurationOfUser();
        for (String s : ofUser) {
            String[] words = s.split(" ");
            userSockets.add(new UserSocket(words[0], words[1]));
        }
        return userSockets;
    }
    private List<String> getConfigurationOfUser() {
        List<String> list = new ArrayList<>();
        String str;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while ((str = bufferedReader.readLine()) != null) {
                list.add(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public void saveUsersInData()  {
        try (OutputStream writer = new FileOutputStream(file, false)) {
            writer.write(getAppended().getBytes(StandardCharsets.UTF_8));
            writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

