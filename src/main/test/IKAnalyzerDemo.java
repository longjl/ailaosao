import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created by longjianlin on 14-8-6.
 * V 1.0
 * *********************************
 * Desc:
 * *********************************
 */
public class IKAnalyzerDemo {
    public static void main(String[] args) throws IOException {
        String fieldName = "text";
        String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";

        Analyzer analyzer = new IKAnalyzer();

        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
        IndexSearcher isearcher = null;

        try {
            //directory = new RAMDirectory();//建立内存索引对象

            File file = new File("/Users/longjianlin/Documents/longjl-doc/testindex");
            directory =  FSDirectory.open(file);

            //配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_4_9,analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory,iwConfig);

            for (int i=0;i<10;i++){
                //写入索引
                Document doc = new Document();
                doc.add(new Field("ID",String.valueOf(i),Field.Store.YES,Field.Index.NOT_ANALYZED));
                doc.add(new Field(fieldName,text,Field.Store.YES,Field.Index.ANALYZED));
                iwriter.addDocument(doc);
            }

            iwriter.close();


            //搜索过程
            //实例化搜索器
            ireader  = IndexReader.open(directory);
            isearcher = new IndexSearcher(ireader);

            String keywork ="开源";
            QueryParser qp = new QueryParser(Version.LUCENE_4_9,fieldName,analyzer);
            qp.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = qp.parse(keywork);

            //搜索相似度最高的5条记录
            TopDocs topDocs = isearcher.search(query,50);
            System.out.println("命中: "+topDocs.totalHits);

            int count = topDocs.totalHits;//命中数

            if(count > 50){
                count = 50;
            }


            //输出结果
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i=0;i<count;i++){
                Document targetDoc = isearcher.doc(scoreDocs[i].doc);
                System.out.println("content: "+targetDoc.get(fieldName));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finally {
            if (ireader != null){
                ireader.close();
            }
            if (directory != null){
                directory.close();
            }
        }

    }
}
