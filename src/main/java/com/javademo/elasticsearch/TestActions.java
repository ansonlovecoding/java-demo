package com.javademo.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestActions {

    @Autowired
    private RestHighLevelClient client;

    //单个查询方法 get
    public void get(String index, String id){
        try {
            GetRequest getRequest = new GetRequest(index, id);
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()){
                System.out.println(getResponse.getSourceAsString());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    //单个添加方法 put
    public void add(String index, String id, Map<String, Object> data){
        try {
            IndexRequest indexRequest = new IndexRequest(index);
            indexRequest.id(id);
            String jsonString = JSON.toJSONString(data);
            indexRequest.source(jsonString, XContentType.JSON);
            //使用ES-7.12.0的版本和JAVA8执行下面的语句会报错：Unable to parse response body for Response IndexResponse
            //将ES换成7.3.0后正常
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

            DocWriteResponse.Result result = indexResponse.getResult();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED){
                //首次创建
                System.out.println("创建操作:"+result);
            }else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED){
                //再次更新
                System.out.println("更新操作:"+result);
            }else {
                System.out.println("未知操作:"+result);
            }

            //分片信息
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getSuccessful() < shardInfo.getTotal()){
                System.out.println("成功分片数小于总分片数");
            }
            //输出错误分片的信息
            if (shardInfo.getFailed() > 0){
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()){
                    String nodeId = failure.nodeId();
                    String reason = failure.reason();
                    System.out.println("节点ID【"+nodeId+"】出错原因"+reason);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //单个修改方法 _update
    public void update(String index, String id, Map<String, Object> data){
        try {
            //封装请求
            UpdateRequest updateRequest = new UpdateRequest(index, id);
            updateRequest.doc(data);
            updateRequest.timeout("1s");
            updateRequest.retryOnConflict(3);
            //执行请求
            UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
            //判断结果
            DocWriteResponse.Result result = response.getResult();
            if (result == DocWriteResponse.Result.CREATED){
                //创建操作
                System.out.println("创建操作成功："+result);
            }else if (result == DocWriteResponse.Result.UPDATED){
                //更新操作
                System.out.println("更新操作成功："+result);
            }else if (result == DocWriteResponse.Result.DELETED){
                //删除操作
                System.out.println("删除操作成功："+result);
            }else {
                //无操作
                System.out.println("无操作："+result);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //单个删除方法 delete
    public void delete(String index, String id){
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            DocWriteResponse.Result result = deleteResponse.getResult();
            System.out.println("删除操作成功："+result);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //批量查询 _mget
    public void mget(List<Map<String, String>> list){
        try {
            MultiGetRequest multiGetRequest = new MultiGetRequest();
            for (Map<String, String> item : list) {
                String index = item.get("index");
                String id = item.get("id");
                multiGetRequest.add(index,id);
            }
            MultiGetResponse multiGetResponse = client.mget(multiGetRequest, RequestOptions.DEFAULT);
            MultiGetItemResponse[] responses = multiGetResponse.getResponses();
            for (MultiGetItemResponse item : responses) {
                if (item.getResponse().isExists()){
                    System.out.println("请求结果："+item.getResponse().getSourceAsString());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //批量操作 _bulk
    public void bulk(List<DocWriteRequest> actionRequests){
        try {
            BulkRequest bulkRequest = new BulkRequest();
            for (DocWriteRequest actionRequest : actionRequests) {
                bulkRequest.add(actionRequest);
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            for (BulkItemResponse itemResponse:bulkResponse) {
                DocWriteResponse response = itemResponse.getResponse();
                DocWriteResponse.Result result = response.getResult();
                switch (itemResponse.getOpType()){
                    case INDEX:
                        System.out.println("创建操作："+result);
                        break;
                    case CREATE:
                        System.out.println("创建操作："+result);
                        break;
                    case DELETE:
                        System.out.println("删除操作："+result);
                        break;
                    case UPDATE:
                        System.out.println("更新操作："+result);
                        break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //创建索引指定映射
    public void createIndex(String index) {
        try {
            //创建索引请求
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            //设置索引的主分片数和副本分配数,2+1 表示2个主分片各配1个副本分片，所以需要有4个机器
            createIndexRequest.settings(Settings.builder().put("number_of_shards", 2).put("number_of_replicas", 1));
            //指定映射
            Map<String,Object> name = new HashMap<>();
            name.put("type", "text");
            Map<String,Object> price = new HashMap<>();
            price.put("type", "long");

            Map<String,Object> properties = new HashMap<>();
            properties.put("name",name);
            properties.put("price",price);

            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("properties",properties);
            createIndexRequest.mapping(objectMap);

            //额外参数，可以依需配置
//            //设置别名
//            createIndexRequest.alias(new Alias(index + "_alias"));
//            //设置超时时间,2分钟
//            createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2));
//            //设置主节点超时时间,2分钟
//            createIndexRequest.setMasterTimeout(TimeValue.timeValueMinutes(2));
//            //设置操作前必须有多少等待的活动分片
//            createIndexRequest.waitForActiveShards(ActiveShardCount.from(2));

            //操作索引的客户端
            IndicesClient indices = client.indices();
            CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged()){
                System.out.println("创建索引成功");
            }else {
                System.out.println("创建索引失败");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //删除索引
    public void deleteIndex(String index){
        try {
            //删除索引请求
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);

            //操作索引的客户端
            IndicesClient indices = client.indices();
            AcknowledgedResponse delete = indices.delete(deleteIndexRequest, RequestOptions.DEFAULT);
            if (delete.isAcknowledged()){
                System.out.println("删除索引成功！");
            }else {
                System.out.println("删除索引失败！");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //异步删除索引,elastic的异步操作都是使用自带的监听器
    public void deleteIndexAsync(String index){
        try {
            //删除索引请求
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            //操作索引的客户端
            IndicesClient indices = client.indices();

            //使用监听
            ActionListener<AcknowledgedResponse> listener = new ActionListener<AcknowledgedResponse>() {

                @Override
                public void onResponse(AcknowledgedResponse acknowledgedResponse) {
                    if (acknowledgedResponse.isAcknowledged()){
                        System.out.println("删除索引成功！");
                    }else {
                        System.out.println("删除索引失败！");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(e.getMessage());
                }
            };
            indices.deleteAsync(deleteIndexRequest, RequestOptions.DEFAULT, listener);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //索引是否存在
    public void indexExist(String index){
        try {
            //生成请求对象
            GetIndexRequest getIndexRequest = new GetIndexRequest(index);

            //操作索引的客户端
            IndicesClient indices = client.indices();
            boolean existResult = indices.exists(getIndexRequest, RequestOptions.DEFAULT);
            if (existResult){
                System.out.println("索引存在");
            }else {
                System.out.println("索引不存在");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
