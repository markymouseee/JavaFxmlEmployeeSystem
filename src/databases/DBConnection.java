package databases;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/employeesystem", "root", "");
            return connection;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
