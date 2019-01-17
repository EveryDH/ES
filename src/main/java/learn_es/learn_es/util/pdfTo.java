package learn_es.learn_es.util;

/**
 * Created by LearnDH on 2019/1/9.
 */
import learn_es.learn_es.model.ContentBean;
import lombok.Data;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
public class pdfTo {

    public static void main(final String[] args) throws IOException, TikaException {

        //选择要提取的文件
        File file = new File("D:/Work/Test/Elasitcsearch开发设计指南.pdf");

        //获取并打印文件内容
        Tika tika = new Tika();
        String filecontent = tika.parseToString(file);
//        System.out.println("Extracted Content: " + filecontent);

        BufferedReader in = new BufferedReader(new FileReader(filecontent));
        String line = null;
        List<ContentBean> packetList = new ArrayList<ContentBean>();
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            String[] item = line.split(" ");
            ContentBean contentBean = new ContentBean();
//            contentBean.setTitle(item[1]);
//            contentBean.setContent(item[2]);
            packetList.add(contentBean);

        }
    }

}