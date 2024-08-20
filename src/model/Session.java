package model;

public class Session {
    private static int userId;
    private static String role;
    private static String username;
    private static String email;

    public static int getUserID(){
        return userId;
    }

    public static String getRole(){
        return role;
    }

    public static String getUsername(){
        return username;
    }

    public static String getEmail(){
        return email;
    }

    public static void setRole(String role){
        Session.role = role;
    }

    public static void setUserID(int userID) {
        Session.userId = userID;
    }

    public static void setUsername(String username){
        Session.username = username;
    }

    public static void setEmail(String email){
        Session.email = email;
    }

    public static void clearSession(){
        userId = 0;
        role = "";
        email = "";
        username = "";
    }
}
