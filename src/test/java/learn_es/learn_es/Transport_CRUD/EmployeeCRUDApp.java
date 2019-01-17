package learn_es.learn_es.Transport_CRUD;

import learn_es.learn_es.model.ContentBean;
import learn_es.learn_es.util.TransportClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by LearnDH on 2019/1/12.
 */

@Slf4j
public class EmployeeCRUDApp {
    @Autowired
     private static ContentBean contentBean;




    public static void main(String[] args) throws Exception {




        //2 设置ES集群名称 跟ES 嗅觉为了其他的集群能进行访问
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es")
//                .put("client.transport.sniff", true)
                .build();
        //1 获取TransportClient 的连接
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.75.189"), 9300));

        System.out.println(client.toString());

        prepareData(client);
//          ExecuteSearch(client);
//        GetById(client);
//        DeleteById(client);
//          UpdateById(client);
//        multiGetDoc(client);
//          bulkContent(client);
//        DeletByQueryContent(client);
//        BulkProcessorRe(client);

        search(client);
        //关闭连接
        client.close();
    }

//    private static void search(TransportClient client) {
//
//        SearchResponse response = client.prepareSearch("learn_index")
//                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
//                .setScroll(new TimeValue(6000))
//                .setQuery(QueryBuilders.wildcardQuery("content", "耗时半年"))
//                .setPostFilter(QueryBuilders.rangeQuery("pdfId").from(16).to(16))
//                .setFrom(0).setSize(100).setExplain(true).addSort("data", SortOrder.DESC)
//                .get();
//
//        response.getHits().forEach(e -> {
//            System.out.println(e.getSourceAsString());
//        });
//    }
    /**
     * 简单查询【通配符查询，筛选价格范围，设定返回数量，排序】
     * @throws UnknownHostException
     */
    public static TransportClient search(TransportClient client) throws Exception {
        SearchResponse response = client.prepareSearch("learn_index")    // index,可以多个
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.wildcardQuery("content", "*耗时半年*"))          // Query
                .setPostFilter(QueryBuilders.rangeQuery("pdfId").from(16).to(16))     // Filter
                .setFrom(0).setSize(100).setExplain(true).addSort("data", SortOrder.DESC)
                .get();
        response.getHits().forEach(e ->{
            System.out.println(e.getSourceAsString());
        });
        return client;
    }

    //    可以根据请求的数量或大小或给定时间段后自动刷新大容量操作
    private static void BulkProcessorRe(TransportClient client) throws IOException {

        BulkProcessor builder = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest bulkRequest) {
                System.out.println("beforeBulk-----" + bulkRequest.getDescription());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                System.out.println("afterBulk------" + bulkRequest.getDescription() + ",是否有错误：" + bulkResponse.hasFailures());
            }

            @Override
            public void afterBulk(long executionId, BulkRequest bulkRequest, Throwable throwable) {
                //bulk 失败
                System.out.println("报错-----" + bulkRequest.getDescription() + "," + throwable.getMessage());
            }
        })      .setBulkActions(10000)//我们希望每10 000次执行一次批量请求。
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))//每5MB刷新一次
                .setFlushInterval(TimeValue.timeValueSeconds(5))// 我们希望每5秒刷新一次大容量，不管请求的数量如何。
                //设置并发请求的数量。值为0意味着只允许执行单个请求。值1意味着在累积新的批量请求时允许执行1个并发请求。
                .setConcurrentRequests(1)
//        设置一个自定义退避策略，该策略最初将等待100 ms，以指数方式增加，重试次数最多可达三次
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100),3)
                )
                .build();
        Random random = new Random();
        for (int i = 1; i <= 1000; i++){
           builder.add(new IndexRequest("learn_index", "lean", i+"11").source(jsonBuilder()
                        .startObject()
                    .field("user", "bob" +i)
                    .field("age", "18")
                    .field("position", "technique")
                    .field("country", "BulkProcessorRe的多条")
                    .field("join_date", new Date())
                    .field("salary", 12000)
                    .endObject()));
        }
        builder.flush();
        builder.close();
    }

    //API允许在一个请求中索引和删除多个文档
    private static void bulkContent(TransportClient client) {

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        try {
            BulkResponse bulkItemResponses = bulkRequest.add(client.prepareIndex("learn_index", "lean", "2")
                    .setSource(jsonBuilder()
                                    .startObject()
                                    .field("user", "bob")
                                    .field("age", "18")
                                    .field("position", "technique")
                                    .field("country", "bulk3的多条")
                                    .field("join_date", "2017-01-01")
                                    .field("salary", 12000)
                                    .endObject()
                    )
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "bob")
                            .field("age", "18")
                            .field("position", "technique")
                            .field("country", "bulk2的多条")
                            .field("join_date", "2017-01-01")
                            .field("salary", 12000)
                            .endObject()
                    )

            ).get();
            if (bulkItemResponses.hasFailures()){
                log.info(String.valueOf(bulkItemResponses));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量获取
     * @param client
     */
    private static void multiGetDoc(TransportClient client) {

        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("learn_index", "lean", "1")
                .add("song_index", "song", "3")
                .get();

        for (MultiGetItemResponse multiGetItemRespons : multiGetItemResponses) {
            GetResponse response = multiGetItemRespons.getResponse();
            if (response.isExists()){
                String json = response.getSourceAsString();
                log.info(json);
            }
        }
    }

    /**
     * 根据条件删除
     * @param client
     */
    private static void DeletByQueryContent(TransportClient client) {
//异步版
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("user","bob"))
                .source("learn_index")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse response ) {
                        long responseDeleted = response.getDeleted();
                        log.info(String.valueOf(responseDeleted));
                    }

                    @Override
                    public void onFailure(Exception e) {

                        log.info("DeletByQueryContent : 异常错误");
                    }
                });

        //节俭版
//        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
//                .filter(QueryBuilders.matchQuery("user", "ooo"))
//                .source("learn_index")
//                .get();
//        long deleted = response.getDeleted();
    }





    private static void UpdateById(TransportClient client) {

        try {
            UpdateResponse updateResponse = client.prepareUpdate("learn_index", "lean", "1")
                    .setDoc(jsonBuilder()
                            .startObject()
                            .field("user", "bob")
                            .field("age", "18")
                            .field("position", "technique")
                            .field("country", "这是更新内222容")
                            .field("join_date", "2017-01-01")
                            .field("salary", 12000)
                            .endObject())
                            .get();
            System.out.println("这是更新的内容");
        } catch (IOException e) {
//            log.info("UpdateById" + e);
//            System.out.println();
        }
    }




    //根据id删除具体对象
    private static void DeleteById(TransportClient client) {
        DeleteResponse deleteResponse = client.prepareDelete("learn_index", "lean","2").get();
        System.out.println(deleteResponse);
    }

    //根据id获取具体对象
    private static void GetById(TransportClient client) {
        GetResponse response  = client.prepareGet("learn_index", "lean","1").get();

        log.info(String.valueOf(response));
//        System.out.println(response);
    }

    //搜索功能
    private static void ExecuteSearch(TransportClient client) {

        client.prepareSearch();
    }


    //建立索引
    private static void prepareData(TransportClient client) throws IOException {
        IndexResponse response  =
                client.prepareIndex("learn_index", "lean","3")
                .setSource(jsonBuilder()
                        .startObject()
//                                .field("pdfId",contentBean.getPdfId())
//                                .field("content",contentBean.getContent())
                                .field("data",new Date())
                        .endObject()
                ).get();
//        client.prepareIndex("learn_index", "lean","2")
//                .setSource(jsonBuilder()
//                        .startObject()
//                        .field("user","ooo")
//                        .field("age","18")
//                        .field("position", "technique")
//                        .field("country", "china")
//                        .field("join_date", "2017-01-01")
//                        .field("salary", 13000)
//                        .endObject()
//                ).get();
//        client.prepareIndex("learn_index", "lean","5")
//                .setSource(jsonBuilder()
//                        .startObject()
//                        .field("user","aaa")
//                        .field("age","18")
//                        .field("position", "technique")
//                        .field("country", "china")
//                        .field("join_date", "2017-01-01")
//                        .field("salary", 18000)
//                        .endObject()
//                ).get();
    }



}
