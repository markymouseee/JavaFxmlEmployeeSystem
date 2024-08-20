package model;

public class SignUp {
    private String name;
    private String username;
    private String email;
    private String password;

    public SignUp(String name, String username, String email, String password){
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
