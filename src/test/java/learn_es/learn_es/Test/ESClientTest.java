package learn_es.learn_es.Test;

import learn_es.learn_es.util.TransportClientFactory;
import org.elasticsearch.client.transport.TransportClient;

/**
 * Created by LearnDH on 2019/1/14.
 */
public class ESClientTest {

    public static void main(String[] args) throws Exception {

        //测试TransportClient 连接ES
        TransportClient client = TransportClientFactory.getInstance().getClient();
        System.out.println(client.toString());
    }
}
