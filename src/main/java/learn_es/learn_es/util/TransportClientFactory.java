package learn_es.learn_es.util;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by LearnDH on 2019/1/14.
 */
public class TransportClientFactory {

    //获取es 的连接
    private TransportClientFactory(){}

    private static class Inner{
        private static final TransportClientFactory instance = new TransportClientFactory();
    }

    public static TransportClientFactory getInstance(){
        return Inner.instance;
    }

    public TransportClient getClient() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es")
                .put("client.transport.sniff", true)
                .build();
        return new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.75.189"), 9300));
    }
}
