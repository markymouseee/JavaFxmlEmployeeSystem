package model;

public class UserInfo {
    private Integer userID;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private String role;

    public UserInfo(Integer userID, String fullname, String username, String email, String password, String role){
        this.userID = userID;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Integer getUserID(){
        return userID;
    }

    public String getFullname(){
        return fullname;
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

    public String getRole(){
        return role;
    }
}
