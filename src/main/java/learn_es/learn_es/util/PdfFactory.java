package learn_es.learn_es.util;

import learn_es.learn_es.model.ContentBean;
import learn_es.learn_es.model.PdfFile;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;


public class PdfFactory {

    private PdfFactory() {
    }

    private static class Inner {
        private static final PdfFactory PdfInstance = new PdfFactory();
    }

    public static PdfFactory getInstance() {
        return PdfFactory.Inner.PdfInstance;
    }


    //获取pdf 封装对象分页文本
    public PdfFile getPdfPC() throws Exception {

        //引用核心部件
        //内容分页解析器
        PageContentHandler handler = new PageContentHandler();

        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("D:/Work/Test/Elasitcsearch开发设计指南.pdf"));
        ParseContext pcontext = new ParseContext();

        //执行解析pdf
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata, pcontext);

        //pdf解析后的字符串
        System.out.println("Contents of the PDF :" + handler.getPages());

        List<String> pages = handler.getPages();

        List<ContentBean> list = new LinkedList<>();
        for (int i = 0; i < pages.size(); i++) {
            ContentBean contentBean = new ContentBean();
            contentBean.setPage(String.valueOf(i));
            contentBean.setContent(pages.get(i));
            list.add(contentBean);
        }
        PdfFile pdfFile = new PdfFile();
        pdfFile.setContentBeans(list);

        return pdfFile;
    }

    //获取pdf 分页文本
    public Map<Object, Object> getPdf() throws Exception {

        //引用核心部件
        //内容分页解析器
        PageContentHandler handler = new PageContentHandler();

        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("D:/Work/Test/Elasitcsearch开发设计指南.pdf"));
        ParseContext pcontext = new ParseContext();

        //执行解析pdf
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata, pcontext);

        //pdf解析后的字符串
        System.out.println("Contents of the PDF :" + handler.getPages());

        ContentBean content = new ContentBean();
        List<String> pages = handler.getPages();
        Map<Object, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < pages.size(); i++) {
            map.put("content" + i, pages.get(i));
        }
        System.out.println(map);
        return map;
    }


    //获取pdf的纯文本
    public String getPdfWord() throws Exception {

        //引用核心部件
        //内容解析器
        BodyContentHandler handler = new BodyContentHandler();

        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("D:/Work/Test/万科Ａ：关于股东A股股份解除质押的公告.PDF"));
        ParseContext pcontext = new ParseContext();

        //执行解析pdf
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata, pcontext);

        //pdf解析后的字符串
        System.out.println("Contents of the PDF :" + handler.toString());
        String pdf = handler.toString();
        return pdf;
    }

}

