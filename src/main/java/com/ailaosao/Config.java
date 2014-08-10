package com.ailaosao;

import com.ailaosao.controller.*;
import com.ailaosao.model.Laosao;
import com.ailaosao.model.Love;
import com.ailaosao.model.Music;
import com.ailaosao.util.PropertiesContent;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * Created by longjianlin on 14-7-23.
 * V 1.0
 * *********************************
 * Desc: 配置
 * *********************************
 */
public class Config extends JFinalConfig {
    /**
     * 常量配置
     *
     * @param me
     */

    @Override
    public void configConstant(Constants me) {
        me.setError404View("/404.html");
        me.setError500View("/500.html");
    }

    /**
     * 路由配置
     *
     * @param me
     */
    @Override
    public void configRoute(Routes me) {
        me.add("/", LaosaoController.class);
        me.add("/music", MusicController.class);
        me.add("/love", LoveController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        PropertiesContent pc = new PropertiesContent("db");
        //配置数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(pc.get("jdbc.url"),
                pc.get("jdbc.username"),
                pc.get("jdbc.password"),
                pc.get("jdbc.driver"));
        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        arp.setShowSql(true);
        arp.addMapping("laosao", Laosao.class);
        arp.addMapping("music", Music.class);
        arp.addMapping("love", Love.class);

        me.add(arp);
    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }

    @Override
    public void afterJFinalStart() {
//        try {
//            //在拦截器或Controller中通过 setAttrs("print_time", new PrintTime())也一样可以
//            //${print_time.call(time)}
//            FreeMarkerRender.getConfiguration().setSharedVariable("print_time", new PrintTime());
//        } catch (TemplateModelException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) throws Exception {
        JFinal.start("/Users/longjianlin/Documents/longjl-work/ailaosao/src/main/webapp", 8080, "/", 5);
    }
}
