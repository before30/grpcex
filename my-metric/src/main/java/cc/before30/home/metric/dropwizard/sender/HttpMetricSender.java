package cc.before30.home.metric.dropwizard.sender;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * HttpMetricSender
 *
 * @author before30
 * @since 2019-07-16
 */
public class HttpMetricSender implements MetricSender {

    private final String url;
    private CloseableHttpClient client;

    public HttpMetricSender(@Nonnull String url) {
        this.url = url;
    }

    @Override
    public void connect() {
        client = HttpClientBuilder.create().build();
    }

    @Override
    public void send(@Nonnull List<String> records) {
        HttpPost request = new HttpPost(url);
        request.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        String body = records.stream().collect(Collectors.joining(",", "[", "]"));
        try {
            request.setEntity(new StringEntity(body));
            client.execute(request);
        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
        }
    }
}
