package com.ailaosao.controller;

import com.ailaosao.bean.LuceneBean;
import com.ailaosao.model.Music;
import com.ailaosao.util.LuceneUtil;
import com.ailaosao.util.PrintTime;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by longjianlin on 14-7-25.
 * V 1.0
 * *********************************
 * Desc:
 * *********************************
 */
public class MusicController extends Controller {
    private static Logger logger = Logger.getLogger(MusicController.class);

    public void index() {
        try {
            long id = getParaToLong(0);
            Music music = Music.dao.getMusic(id);
            if (music != null) {
                music.set("create_at", PrintTime.getNiceDate(music.getDate("create_at").toString()));
                setAttr("music", music);
                render("/music/music.html");
            } else {
                renderError(404);
            }
        } catch (Exception e) {
            logger.error("no music : " + e.getMessage());
            renderError(404);
        }
    }



    public void add() {
        render("/music/add.html");
    }

    /**
     * save
     */
    public void save() {
        String title = getPara("title");
        String content = getPara("content");
        String url = getPara("url");
        String singer = getPara("singer");
        Music music = new Music();
        music.set("title", title);
        music.set("singer",singer);
        music.set("content", content);
        music.set("status", 1);//状态: 1-音乐 2-文章 3-电影
        music.set("url", url);
        music.set("create_at", new Date());

        boolean b = music.save();
        if (b) {
            LuceneBean bean = new LuceneBean();
            bean.setId(music.getLong("id"));
            bean.setTitle(music.getStr("title"));
            bean.setSinger(music.getStr("singer"));
            bean.setContent(music.getStr("content"));
            bean.setUrl(music.getStr("url"));
            LuceneUtil.createIndex(bean);//创建索引
        }
        redirect("/");
    }

    /**
     * 编辑
     */
    public void edit() {
        try {
            long id = getParaToLong(0);
            Music music = Music.dao.getMusic(id);
            setAttr("music", music);
            render("/music/edit.html");
        } catch (Exception e) {
            logger.error(e.getMessage());
            redirect("/music");
        }
    }

    /**
     * update
     */
    public void update() {
        try {
            long id = getParaToLong("id");
            String title = getPara("title");
            String singer = getPara("singer");
            String content = getPara("content");
            String url = getPara("url");

            Music music = new Music();
            music.set("id", id);
            music.set("title", title);
            music.set("singer",singer);
            music.set("content", content);
            music.set("url", url);
            boolean b = music.update();
            if (b) {
                LuceneBean bean = new LuceneBean();
                bean.setId(music.getLong("id"));
                bean.setTitle(music.getStr("title"));
                bean.setSinger(music.getStr("singer"));
                bean.setContent(music.getStr("content"));
                bean.setUrl(music.getStr("url"));
                LuceneUtil.updateIndex(bean);//更新索引
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        redirect("/");
    }
}
