package com.example.demo.models;

import com.example.demo.validators.UniqueEmail;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


//implementing Serializable to be able to make it a 3 ID bean
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;

    @NotNull
    @Size(min = 2, max = 30)
    private String userFirstName;

    @NotNull
    @Size(min = 2, max = 40)
    private String userLastName;

    @UniqueEmail
    @NotNull
    @Email
    private String userMail;

    @NotNull
    @Size(min = 5, max = 20)
    private String userLogin;

    @NotNull
    @Size(min = 8, max = 30)
    private String userPassword;


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}
