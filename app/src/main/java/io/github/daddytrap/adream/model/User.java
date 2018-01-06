package io.github.daddytrap.adream.model;

/**
 * Created by 74187 on 2018/1/5.
 */

public class User {
    private int userId;
    private String userName;
    private String avatarBase64;

    public User(int userId, String userName, String avatarBase64) {
        this.userId = userId;
        this.userName = userName;
        this.avatarBase64 = avatarBase64;
    }

    public int getId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }
}
