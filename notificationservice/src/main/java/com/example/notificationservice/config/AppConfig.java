package com.example.notificationservice.config;

import java.util.Collections;
import javax.servlet.Filter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
@Order(2)
public class AppConfig {

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String SPAN_ID = "X-B3-SpanId";
    private static final String PARENT_ID = "X-B3-ParentSpanId";
    private static final String IS_SAMPLED = "X-B3-Sampled";

    private final CloseableHttpClient httpClient;


    public AppConfig(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }


//
//    @Bean
//    public FilterRegistrationBean requestFilterRegistration() {
//        FilterRegistrationBean result = new FilterRegistrationBean();
////        result.setUrlPatterns(Lists.newArrayList("/api/*"));
//        result.setName("Request Header Filter");
//        result.setOrder(1);
//        return result;
//    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setInterceptors(Collections.singletonList((httpRequest, body, execution) -> {
            httpRequest.getHeaders().add(TRACE_ID, MDC.get(TRACE_ID));
            httpRequest.getHeaders().add(SPAN_ID, MDC.get(SPAN_ID));
            httpRequest.getHeaders().add(PARENT_ID, MDC.get(PARENT_ID));
            httpRequest.getHeaders().add(IS_SAMPLED, System.getenv("SAMPLING_REQUIRED"));

            return execution.execute(httpRequest, body);
        }));

        return restTemplate;
    }

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    /**
     * Register and configure message source bean.
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}









