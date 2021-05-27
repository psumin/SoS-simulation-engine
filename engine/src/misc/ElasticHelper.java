package misc;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;

public class ElasticHelper {
    private ElasticHelper(){}
    private static ElasticHelper elasticHelperInstance = new ElasticHelper();
    public static ElasticHelper getElasticHelperInstance(){return elasticHelperInstance;}
    private static Logger LOGGER = LoggerFactory.getLogger(ElasticHelper.class);
    private Settings settingsInstance = Settings.getSettingsInstance();
    private RestHighLevelClient elasticClient;
    private String INDEXNAME = settingsInstance.getINDEXNAME();

    public boolean initElasticClient(String HOST, int PORT) throws IOException {
        INDEXNAME = settingsInstance.getINDEXNAME();
        elasticClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST, PORT, "http")
                ));

        try {
            boolean result = elasticClient.ping(RequestOptions.DEFAULT);
            LOGGER.info("Started elastic-client: " + result + ". client: " + elasticClient.toString());
            return result;
        } catch (Exception e) {
            LOGGER.warn("Elastic endpoint unavailable.");
            return false;
        }
    }

    public void indexLogs(Class classname, LinkedHashMap<String, String> arguments) {
        if (settingsInstance.getUseElastic()){
            try {
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
                xContentBuilder.startObject();
                {
                    xContentBuilder.field("class_name", classname.getSimpleName());
                    xContentBuilder.timeField("timestamp", new Date());
                    Set<String> keys = arguments.keySet();
                    for (String key:keys) {
                        xContentBuilder.field(key, arguments.get(key));
                    }

                }
                xContentBuilder.endObject();
                IndexRequest indexRequest = new IndexRequest(INDEXNAME).source(xContentBuilder);

                ActionListener listener = new ActionListener<IndexResponse>() {
                    @Override
                    public void onResponse(IndexResponse indexResponse) {
                        LOGGER.info(classname.getSimpleName() + " logged to elasticsearch with arguments: " + arguments.toString());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        LOGGER.warn("Failed to log to elasticsearch:");
                        e.printStackTrace();
                    }
                };

                elasticClient.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.debug("Attempted to use elastic logging. Not using elastic logging.");
        }

    }

}
