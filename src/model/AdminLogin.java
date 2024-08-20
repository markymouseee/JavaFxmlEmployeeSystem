package model;

public class AdminLogin {
    private String username;
    private String password;

    public AdminLogin(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
