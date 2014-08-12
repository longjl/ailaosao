package com.ailaosao.util;

import com.ailaosao.bean.LuceneBean;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longjianlin on 14-8-7.
 * V 1.0
 * *********************************
 * Desc: Lucene 工具类
 * *********************************
 */
public class LuceneUtil {
    //lucene存放索引的地址
    private static String lucenePath = null;
    private static Analyzer analyzer = null;
    private static Directory directory = null;
    private static IndexWriter iwriter = null;
    private static IndexReader ireader = null;
    private static IndexSearcher isearcher = null;

    static {
        PropertiesContent pc = new PropertiesContent("lucene_path");
        lucenePath = pc.get("lucene_path");
    }

    /**
     * 创建索引(多个)
     *
     * @param list
     */
    public static void createIndex(List<LuceneBean> list) {
        File file = new File(lucenePath);//存放索引
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        if (analyzer == null)
            analyzer = new IKAnalyzer();

        try {
            //directory = new RAMDirectory();//建立内存索引对象
            directory = FSDirectory.open(file);//将索引存放在本地

            //配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            for (LuceneBean lb : list) {//循环创建索引
                //写入索引
                Document doc = new Document();
                /*
                Field.Store.YES / NO  ---  存储选项
                            设置为YES表示把这个域中的内容完全存储到文件中，方便进行文本的还原
                            设置为NO表示把这个域的内容不存储到文件中，但是可以被索引，此时内容无法完全还原

                Field.Index.  --- 索引选项
                            ANALYZED  进行分词和索引，适用于标题，内容等
                            NOT_ANALYZED  进行索引，但不进行分词，适用于精确搜索
                            ANALYZED_NOT_NORMS 进行分词但是不存储norms信息，这个norms中包括了创建索引的时间和全职等信息
                            NOT_ANALYZED_NOT_NORMS 既不进行分词也不存储norms信息
                            NO 不进行索引
                 */
                doc.add(new Field("id", String.valueOf(lb.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field("title", lb.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("singer", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("content", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("url", lb.getUrl(), Field.Store.YES, Field.Index.NOT_ANALYZED));
                iwriter.addDocument(doc);
            }
            iwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建索引(单个)
     *
     * @param lb
     */
    public static void createIndex(LuceneBean lb) {
        File file = new File(lucenePath);//存放索引
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        if (analyzer == null)
            analyzer = new IKAnalyzer();

        try {
            directory = FSDirectory.open(file);//将索引存放在本地

            //配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);

            //写入索引
            Document doc = new Document();
                /*
                Field.Store.YES / NO  ---  存储选项
                            设置为YES表示把这个域中的内容完全存储到文件中，方便进行文本的还原
                            设置为NO表示把这个域的内容不存储到文件中，但是可以被索引，此时内容无法完全还原

                Field.Index.  --- 索引选项
                            ANALYZED  进行分词和索引，适用于标题，内容等
                            NOT_ANALYZED  进行索引，但不进行分词，适用于精确搜索
                            ANALYZED_NOT_NORMS 进行分词但是不存储norms信息，这个norms中包括了创建索引的时间和全职等信息
                            NOT_ANALYZED_NOT_NORMS 既不进行分词也不存储norms信息
                            NO 不进行索引
                 */
            doc.add(new Field("id", String.valueOf(lb.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("title", lb.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("singer", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("url", lb.getUrl(), Field.Store.YES, Field.Index.ANALYZED));
            iwriter.addDocument(doc);
            iwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引(单个)
     *
     * @param lb
     */
    public static void updateIndex(LuceneBean lb) {
        File file = new File(lucenePath);//存放索引文件
        if (analyzer == null) analyzer = new IKAnalyzer();//创建IKAnalyzer分词
        try {
            directory = FSDirectory.open(file);//将索引存放在本地

            //配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);

            //写入索引
            Document doc = new Document();
             /*
                Field.Store.YES / NO  ---  存储选项
                            设置为YES表示把这个域中的内容完全存储到文件中，方便进行文本的还原
                            设置为NO表示把这个域的内容不存储到文件中，但是可以被索引，此时内容无法完全还原

                Field.Index.  --- 索引选项
                            ANALYZED  进行分词和索引，适用于标题，内容等
                            NOT_ANALYZED  进行索引，但不进行分词，适用于精确搜索
                            ANALYZED_NOT_NORMS 进行分词但是不存储norms信息，这个norms中包括了创建索引的时间和全职等信息
                            NOT_ANALYZED_NOT_NORMS 既不进行分词也不存储norms信息
                            NO 不进行索引
                 */
            doc.add(new Field("id", String.valueOf(lb.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("title", lb.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("singer", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("content", lb.getContent(), Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("url", lb.getUrl(), Field.Store.YES, Field.Index.NOT_ANALYZED));
            Term term = new Term("id", String.valueOf(lb.getId()));
            iwriter.updateDocument(term, doc);
            iwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 搜索
     *
     * @param keyword
     * @return
     */
    public static List<LuceneBean> search(String keyword) throws IOException, ParseException {
        List<LuceneBean> list = new ArrayList<LuceneBean>();
        File file = new File(lucenePath);//存放索引路径
        LuceneBean bean = null;

        if (directory == null) {
            directory = FSDirectory.open(file);//将索引存放在本地
        }
        ireader = IndexReader.open(directory);
        isearcher = new IndexSearcher(ireader);

        String[] queries = {keyword, keyword, keyword};
        String[] fields = {"title", "singer", "content"};

        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};

        if (analyzer == null)
            analyzer = new IKAnalyzer();

        Query query = MultiFieldQueryParser.parse(Version.LUCENE_4_9, queries, fields, analyzer);

        TopDocs topDocs = isearcher.search(query, 55);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        int count = topDocs.totalHits;//命中数
        if (count > 50) count = 50;

        for (int i = 0; i < count; i++) {
            Document doc = isearcher.doc(scoreDocs[i].doc);
            bean = new LuceneBean();
            bean.setId(Long.parseLong(doc.get("id")));
            bean.setTitle(doc.get("title"));
            bean.setSinger(doc.get("singer"));
            bean.setContent(doc.get("content"));
            bean.setUrl(doc.get("url"));
            list.add(bean);
        }

        if (ireader != null) {
            ireader.close();
        }

        return list;
    }
}
