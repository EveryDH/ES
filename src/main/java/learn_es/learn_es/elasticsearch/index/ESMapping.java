package learn_es.learn_es.elasticsearch.index;

import learn_es.learn_es.model.ContentBean;
import learn_es.learn_es.model.pdfFile;
import learn_es.learn_es.util.PageContentHandler;
import learn_es.learn_es.util.PdfFactory;
import org.apache.tika.parser.chm.accessor.ChmPmgiHeader;
import org.elasticsearch.common.xcontent.XContentBuilder;

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


    public static XContentBuilder getMapping() throws Exception {
        //时间日期
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        PdfFactory  pdfFactory = PdfFactory.getInstance();

        //原来获取map的方法 getPdf
//        Map<Object, Object> pdf = pdfFactory.getPdf();

        //获取pdf整个对象的方法
        pdfFile pdfPC = pdfFactory.getPdfPC();

        List<ContentBean> contentBeans = pdfPC.getContentBeans();

        for (ContentBean contentBean : contentBeans) {
            contentBean.getPages();
            contentBean.getContent();

        }

//        String pdfStr = pdf.toString();

        // 构建对象
        XContentBuilder mapping = null;
        try {
            mapping = jsonBuilder();
            mapping.startObject();
            mapping.field("pdfId","188");
            mapping.field("pages",contentBean.getPages());
            mapping.field("content", contentBean.getContent());
            mapping.field("date",  dateFormat.format(now));
            mapping.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 转成JSON格式
        return mapping;
    }
}

