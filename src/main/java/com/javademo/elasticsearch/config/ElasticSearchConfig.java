package com.javademo.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String[] hostList;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient(){
        HttpHost[] hosts = new HttpHost[hostList.length];
        for (int i = 0; i < hostList.length; i++) {
            String hostString = hostList[i];
            String hostAddress = hostString.split(":")[0];
            Integer port = Integer.parseInt(hostString.split(":")[1]);
            hosts[i] = new HttpHost(hostAddress, port, "http");
        }
        return new RestHighLevelClient(RestClient.builder(hosts));
    }
}
