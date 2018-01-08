package io.github.daddytrap.adream.model;


import java.util.Date;

/**
 * Created by 74187 on 2018/1/5.
 */

public class Passage {
    private int passageId;
    private String title;
    private String author;
    private String content;
    private Date date;
    private String avatarBase64;
    private String type;

    public Passage(int passageId, String title, String author, String content, Date date, String avatarBase64) {
        this.passageId = passageId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = date;
        this.avatarBase64 = avatarBase64;
        this.type = "unknown";
    }

    public Passage(int passageId, String title, String author, String content, Date date, String avatarBase64, String type) {
        this.passageId = passageId;
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = date;
        this.avatarBase64 = avatarBase64;
        this.type = type;
    }

    public int getPassageId() {
        return passageId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public String getType() {
        return type;
    }
}
