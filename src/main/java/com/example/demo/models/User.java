package com.example.demo.models;

import com.example.demo.repositories.UserRepository;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Vector;


//implementing Serializable to be able to make it a 3 ID bean
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userID;

    @NotNull
    @Size(min = 2, max = 30)
    private String userFirstName;

    @NotNull
    @Size(min = 2, max = 40)
    private String userLastName;

    @NotNull
    @Email
    private String userMail;

    @NotNull
    @Size(min = 5, max = 20)
    private String userLogin;

    @NotNull
    @Size(min = 8, max = 30)
    private String userPassword;

    @Column(name="isConnected")
    private boolean isConnected=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(

            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "userID"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
        isConnected = false;
    }


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


    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public String getUserRole(){
        if(roles.size()!=1){
            return "None";
        }
        List<Role> l = (List<Role>) roles;
        return l.get(0).getName();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
