package com.ailaosao.controller;

import com.ailaosao.bean.LuceneBean;
import com.ailaosao.model.Music;
import com.ailaosao.util.LuceneUtil;
import com.ailaosao.util.PrintTime;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Created by longjianlin on 14-7-25.
 * V 1.0
 * *********************************
 * Desc: 牢骚控制类
 * *********************************
 */
public class LaosaoController extends Controller {
    private static Logger logger = Logger.getLogger(LaosaoController.class);

    public void index() {
        logger.info("music : getMusics()");
        List<Music> musics = Music.dao.getMusics();
        for (Music m : musics) {
            m.set("create_at", PrintTime.getNiceDate(m.getDate("create_at").toString()));
        }
        setAttr("musics", musics);
        render("/index.html");
    }

    /**
     * 搜索
     * Lucene + IKAnalyzer
     */
    public void search() {
        String keyword = getPara("keyword");
        if (keyword == null || keyword.length() == 0) {
            redirect("/");
            return;
        }
        try {
            List<LuceneBean> list = LuceneUtil.search(keyword);
            setAttr("lb", list);
            render("/music/search.html");
        } catch (IOException e) {
            logger.error("search:" + e.getMessage(), e);
            renderError(500);
        } catch (ParseException e) {
            logger.error("search:" + e.getMessage(), e);
            renderError(500);
        }
    }

    /**
     * 礼物
     */
    public void gifts(){
         render("/gifts.html");
    }
}
