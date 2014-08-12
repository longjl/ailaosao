package com.ailaosao.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by longjianlin on 14-7-25.
 * V 1.0
 * *********************************
 * Desc: 消息
 * *********************************
 */
public class Music extends Model<Music> {
    public static final Music dao = new Music();


    /**
     * 获取所有Music
     * @return
     */
    public List<Music> getMusics(){
        return dao.find("select m.id,m.title,m.singer,m.url,m.create_at from music m order by m.create_at desc");
    }

    /**
     * 根据id查询Music
     * @param id
     * @return
     */
    public Music getMusic(long id){
        return dao.findFirst("select m.id,m.title,m.singer,m.content,m.url,m.create_at from music m where m.id =?",id);
    }
}
