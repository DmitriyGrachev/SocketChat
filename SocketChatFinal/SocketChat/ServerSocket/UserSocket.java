package ServerSocket;

public class UserSocket {
    private String login;
    private String password;
    private boolean ifUserIsLogged;

    public UserSocket()  {
        this.ifUserIsLogged = false;
        this.login = "";
        this.password = "";
    }

    public UserSocket(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLoginOfUser() {
        return login;
    }
    public String getPasswordOfUser() {
        return password;
    }
    public void setLoginOfUser(String login) {
        this.login = login;
    }
    public void setPasswordOfUser(String password) {
        this.password = password;
    }

    public boolean checkIfUserIsLogged() {
        return ifUserIsLogged;
    }
    public void setIfUserIsLogged(boolean ifUserIsLogged) {
        this.ifUserIsLogged = ifUserIsLogged;
    }

}

