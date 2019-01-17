package learn_es.learn_es.model;

import lombok.Data;

import java.util.List;

/**
 * Created by LearnDH on 2019/1/17.
 */
@Data
public class pdfFile {

    private Integer id;

    private String fileName = "科技";

    private List<ContentBean> contentBeans;
}
