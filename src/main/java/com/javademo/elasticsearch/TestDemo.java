package com.javademo.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class TestDemo {

    //ES服务地址
    private static final String HOST = "127.0.0.1";
    //ES端口
    private static final int PORT = 9200;
    //协议头
    private static final String HEAD = "http";

    public static void main(String[] args) throws IOException {

        //连接客户端
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(HOST, PORT, HEAD))
        );
        //构建请求
        GetRequest getRequest = new GetRequest("book", "3");
        //执行请求
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        //输出结果
        if (getResponse.isExists()){
            String sourceString = getResponse.getSourceAsString();
            System.out.println(sourceString);
        }
    }
}
