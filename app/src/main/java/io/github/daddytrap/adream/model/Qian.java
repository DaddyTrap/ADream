package io.github.daddytrap.adream.model;

/**
 * Created by 74187 on 2018/1/7.
 */

public class Qian {
    private int qianId;
    private String title;
    private String content;

    public Qian(int qianId, String title, String content) {
        this.qianId = qianId;
        this.title = title;
        this.content = content;
    }

    public int getQianId() {
        return qianId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
