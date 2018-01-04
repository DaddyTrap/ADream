package io.github.daddytrap.adream.model;

import java.util.Date;

/**
 * Created by 74187 on 2018/1/5.
 */

public class Comment {
    private int commentId;
    private User user;
    private String content;
    private Date date;

    public Comment(int commentId, User user, String content, Date date) {
        this.commentId = commentId;
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public int getCommentId() {
        return commentId;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }
}
