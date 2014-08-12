package com.ailaosao.bean;

/**
 * Created by longjianlin on 14-8-7.
 * V 1.0
 * *********************************
 * Desc: lucene 实体类
 * *********************************
 */
public class LuceneBean {
    private long id;//编号
    private String title;//标题
    private String content;//内容

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    private String singer;//歌手

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;//资源地址

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
