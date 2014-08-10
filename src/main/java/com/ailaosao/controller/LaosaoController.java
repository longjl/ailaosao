package com.ailaosao.controller;

import com.ailaosao.model.Laosao;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

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
        setAttr("laosaos", Laosao.dao.getLaosaos());//获取所有牢骚数据
        render("/index.html");
    }

    /**
     * 搜索
     * Lucene + IKAnalyzer
     */
    public void search(){
         String keyword = getPara("keyword");

    }
}
