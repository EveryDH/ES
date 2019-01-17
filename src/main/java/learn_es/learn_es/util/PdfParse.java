package learn_es.learn_es.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class PdfParse    {

    public static void main(final String[] args) throws Exception {

        //内容分页解析器
        PageContentHandler handler = new PageContentHandler();
//        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("D:/Work/Test/Elasitcsearch开发设计指南.pdf"));
        ParseContext pcontext = new ParseContext();


        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata,pcontext);


        System.out.println("Contents of the PDF :" + handler.toString());


        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name+ " : " + metadata.get(name));

            String pdf = handler.toString();
            System.out.println(pdf);

        }
    }
}