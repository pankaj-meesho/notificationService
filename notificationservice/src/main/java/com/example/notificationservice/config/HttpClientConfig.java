package com.example.notificationservice.config;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class HttpClientConfig {

//    @Value("${payment-aggregator.http.pool.max-connections}")
    private static Integer maxConnections = 10;

//    @Value("${payment-aggregator.http.pool.default-max-connections-per-route}")
    private static  Integer defaultMaxConnectionsPerRoute = 2;

//    @Value("${payment-aggregator.http.pool.connect-timeout}")
    private static  Integer connectTimeout = 30000;

//    @Value("${payment-aggregator.http.pool.socket-timeout}")
    private static  Integer socketTimeout=30000;

//    @Value("${payment-aggregator.http.pool.connect-request-timeout}")
    private static  Integer connectRequestTimeout = 30000;

    @Bean
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(maxConnections);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxConnectionsPerRoute);
        return poolingHttpClientConnectionManager;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectRequestTimeout)
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(getPoolingHttpClientConnectionManager())
                .build();
    }
}





