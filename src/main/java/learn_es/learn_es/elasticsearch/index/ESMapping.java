package learn_es.learn_es.elasticsearch.index;

import learn_es.learn_es.config.ESConstant;
import learn_es.learn_es.model.ContentBean;
import learn_es.learn_es.model.PdfFile;
import learn_es.learn_es.util.PageContentHandler;
import learn_es.learn_es.util.PdfFactory;
import org.apache.tika.parser.chm.accessor.ChmPmgiHeader;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by LearnDH on 2019/1/14.
 */
public class ESMapping {
    PageContentHandler pageContentHandler;


    public static XContentBuilder getMappings() throws Exception {
        //时间日期
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        PdfFactory  pdfFactory = PdfFactory.getInstance();

        //原来获取map的方法 getPdf
//        Map<Object, Object> pdf = pdfFactory.getPdf();



//        String pdfStr = pdf.toString();
        TransportClient client = ESIndex.addBulk();



        // 构建对象
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder()
               .startObject()
                    .field("fileName","科技")
                    .field("pages", "")
                    .field("content", "")
                    .field("date", dateFormat.format(now))
               .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 转成JSON格式
        return mapping;
    }

    public static XContentBuilder getMapping() throws Exception {
        //时间日期
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        PdfFactory  pdfFactory = PdfFactory.getInstance();

        //原来获取map的方法 getPdf
//        Map<Object, Object> pdf = pdfFactory.getPdf();


        TransportClient client = ESIndex.addBulk();

        // 构建对象
        XContentBuilder mapping = null;
            mapping = jsonBuilder()
                .startObject()
                    .startObject("properties") //设置之定义字段
                        .startObject("id")
                            .field("type","long") //设置数据类型
                        .endObject()
                    .startObject("fileName")
                         .field("type","text")
                         .field("analyzer","ik_smart")
                    .endObject()
                    .startObject("date")
                        .field("type","date")  //设置Date类型
                        .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
                    .endObject()
                        .startObject("ContentBean")
                              .field("type","nested")
                        .endObject()
                        .startObject("properties")
                            .startObject("pages")
                                 .field("type","long")
                            .endObject()
                            .startObject("content")
                                 .field("type","text")
                            .endObject()
                    .endObject();
        // 转成JSON格式
        return mapping;
    }
}

