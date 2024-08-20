package model;

import java.sql.Date;

public class EmployeeData {
    private Integer employeeID;
    private String firstname;
    private String lastname;
    private String sex;
    private String email;
    private String position;
    private Date date;

    public EmployeeData(Integer employeeID, String firstname, String lastname, String sex, String email, String position,  Date date){
        this.employeeID = employeeID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.email = email;
        this.position = position;
        this.date = date;
    }

    public Integer getEmployeeID(){
        return employeeID;
    }

    public String getFirstname(){
        return firstname;
    }

    public String getLastname(){
        return lastname;
    }

    public String getSex(){
        return sex;
    }

    public String getEmail(){
        return email;
    }

    public String getPosition(){
        return position;
    }

    public Date getDate(){
        return date;
    }
}
