package com.ailaosao.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by longjianlin on 14-7-25.
 * V 1.0
 * *********************************
 * Desc: 牢骚
 * *********************************
 */
public class Laosao extends Model<Laosao> {
    public static final Laosao dao = new Laosao();

    /**
     * 获取所有Laosao数据
     * @return
     */
    public  List<Laosao> getLaosaos(){
        return dao.find("select ls.id,ls.name,ls.avatar,ls.create_at from laosao ls");
    }
}
