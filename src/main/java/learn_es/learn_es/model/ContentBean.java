package learn_es.learn_es.model;

import lombok.Data;

import java.util.List;

@Data
public class ContentBean {

    private Integer id;

    //页码
    private int pages;

    // 内容
    private String content;

}
