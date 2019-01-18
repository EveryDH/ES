package learn_es.learn_es.elasticsearch.index;

import learn_es.learn_es.model.ContentBean;
import learn_es.learn_es.model.PdfFile;
import learn_es.learn_es.util.PdfFactory;
import learn_es.learn_es.util.TransportClientFactory;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by LearnDH on 2019/1/14.
 */
public class ESIndex {

    public static void main(String[] args) throws Exception {

        addBulk();

//        addDoc1();
    }


    public static TransportClient addBulk() throws Exception {

        //时间日期
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        TransportClient client = TransportClientFactory.getInstance().getClient();

        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                // bulk 执行之前
                System.out.println("beforeBulk-----" + request.getDescription());
            }

            @Override
            public void afterBulk(long executionId,
                                  BulkRequest request,
                                  BulkResponse response) {
                // bulk 执行之后
                System.out.println("afterBulk------" + request.getDescription() + ",是否有错误：" + response.hasFailures());
            }

            @Override
            public void afterBulk(long executionId,
                                  BulkRequest request,
                                  Throwable failure) {
                //bulk 失败
                System.out.println("报错-----" + request.getDescription() + "," + failure.getMessage());
            }
        })      .setBulkActions(100)
                .setConcurrentRequests(0)
                .build();
/*
        抽出来的方法使用
        XContentBuilder mapping = ESMapping.getMapping();
        String json = Strings.toString(mapping);

*/

        //获取pdf整个对象的方法
        PdfFactory pdfFactory = PdfFactory.getInstance();

        PdfFile pdfPC = pdfFactory.getPdfPC();

        List<ContentBean> contentBean = pdfPC.getContentBeans();
        for (ContentBean bean : contentBean) {
        bulkProcessor.add(new IndexRequest("pdf_index", "learn").source(jsonBuilder()
                .startObject()
                    .field("id","1")
                    .field("fileName","科技")
                    .field("pages",  bean.getPage())
                    .field("content",  bean.getContent())
//                    .field("date", dateFormat.format(now))
                .endObject()));
        }
        bulkProcessor.flush();
        bulkProcessor.close();
        client.admin().indices().prepareRefresh().get();
        return client;
        }




    //单个索引存储
/*
    public static TransportClient addDoc1() throws Exception {
        TransportClient client = TransportClientFactory.getInstance().getClient();
        XContentBuilder mapping = ESMapping.getMapping();
        String json = Strings.toString(mapping);

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("learn_index", "lean");
        IndexResponse response = indexRequestBuilder.setSource(json, XContentType.JSON).get();

        return client;
    }
*/




}
