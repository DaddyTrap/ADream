package io.github.daddytrap.adream.model;

/**
 * Created by 74187 on 2018/1/5.
 */

public class Music {
    private int id;
    private String href;
    private String title;
    private String lyric;

    public Music(int id, String href, String title, String lyric) {
        this.id = id;
        this.href = href;
        this.title = title;
        this.lyric = lyric;
    }

    public int getId() {
        return id;
    }

    public String getHref() {
        return href;
    }

    public String getTitle() {
        return title;
    }

    public String getLyric() {
        return lyric;
    }
}
