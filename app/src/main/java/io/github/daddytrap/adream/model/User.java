package io.github.daddytrap.adream.model;

/**
 * Created by 74187 on 2018/1/5.
 */

public class User {
    private int userId;
    private String userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
