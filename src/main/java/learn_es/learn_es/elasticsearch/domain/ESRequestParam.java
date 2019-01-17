package learn_es.learn_es.elasticsearch.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ESRequestParam {

    private String index;
    private String type;
    private int from = 0;
    private int size;
}
