package ru.stqa.pft.mantis.models;

/**
 * Created by DELL on 07.09.16.
 */
public class UserData {

    private String username;
    private String email;

    public String getUserName() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public UserData withUsername(String name) {
        this.username = name;
        return this;
    }

    public UserData withEmail(String email) {
        this.email = email;
        return this;
    }

}
