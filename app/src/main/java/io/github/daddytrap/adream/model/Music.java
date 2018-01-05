package io.github.daddytrap.adream.model;

/**
 * Created by 74187 on 2018/1/5.
 */

public class Music {
    private int id;
    private String localPath;
    private String href;

    public Music(int id, String localPath, String href) {
        this.id = id;
        this.localPath = localPath;
        this.href = href;
    }

    public int getId() {
        return id;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getHref() {
        return href;
    }
}
