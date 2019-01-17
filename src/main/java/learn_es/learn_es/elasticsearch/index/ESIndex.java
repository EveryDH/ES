package learn_es.learn_es.elasticsearch.index;

import learn_es.learn_es.util.TransportClientFactory;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.UUIDs;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.UUID;

/**
 * Created by LearnDH on 2019/1/14.
 */
public class ESIndex {

    public static void main(String[] args) throws Exception {
        addDoc1();
    }

    public static TransportClient addDoc1() throws Exception {
        TransportClient client = TransportClientFactory.getInstance().getClient();
        XContentBuilder mapping = ESMapping.getMapping();
        String json = Strings.toString(mapping);

        IndexRequestBuilder indexRequestBuilder = client.prepareIndex("learn_index", "lean");
        IndexResponse response = indexRequestBuilder.setSource(json, XContentType.JSON).get();

        return client;
    }

}
