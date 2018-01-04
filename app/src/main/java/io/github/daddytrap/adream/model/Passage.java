package io.github.daddytrap.adream.model;


/**
 * Created by 74187 on 2018/1/5.
 */

public class Passage {
    private int passageId;
    private String title;
    private String author;
    private String content;

    public Passage(int passageId, String title, String author, String content) {
        this.passageId = passageId;
        this.title = title;
        this.author = author;
        this.content = content;
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
}
