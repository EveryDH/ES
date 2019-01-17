package learn_es.learn_es.config;

/**
 * Created by LearnDH on 2019/1/13.
 */
public class ESConstant {
    /** Elasticsearch Meta-Fields 系统参数 */
    public static final String ALL = "_all";
    public static final String ENABLE = "enabled";

    /** Elasticsearch 映射参数 */
    public static final String PROPERTIES = "properties";
    public static final String TYPE = "type";
    public static final String ANALYZER = "analyzer";
    public static final String SEARCH_ANALYZER = "search_analyzer";

    /** Field datatype 数据类型 */
    public static final String TEXT = "text";
    public static final String KEYWORD = "keyword";
    public static final String INT = "integer";

    /** 分词器相关 */
    public static final String WHITESPACE= "whitespace";
    public static final String IK_SMART = "ik_smart";
    public static final String IK_MAX_WORD = "ik_max_word";

    /** Index paragraph mapping 索引字段 */
    // 这里的ID 都是根据 UUID 生成的
    public static final String DOCUMENT_ID = "documentId";
    public static final String DOCUMENT_TITLE = "documentTitle";
    public static final String DOCUMENT_CONTENT = "document_content";
    /** Elasticsearch Index 索引相关 */
    public static final String INDEX_PARAGRAPH = "paragraph";
    public static final String INDEX_PARAGRAPH_TYPE = "paragraph";

}
