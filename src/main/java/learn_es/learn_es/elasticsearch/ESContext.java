package learn_es.learn_es.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by LearnDH on 2019/1/13.
 */


/**
 * 获取ES 连接
 */
@Component
@Slf4j
public class ESContext {


    public TransportClient elasticClient() {

        //1 获取TransportClient 的连接
        TransportClient client = null;
        try {
        //2 设置ES集群名称 跟ES 嗅觉为了其他的集群能进行访问
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es")
                .put("client.transport.sniff", true)
                .build();
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.75.189"), 9300));
            return client;
        } catch (UnknownHostException e) {
            log.error("[ERROR] Error occurred in init a elasticsearch client !!! ", e);
        }
        System.out.println(client.toString());

        //关闭连接
//        client.close();
        return  null;
    }
}
