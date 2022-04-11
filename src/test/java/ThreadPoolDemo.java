import com.alibaba.fastjson.JSON;
import com.javademo.DemoApplication;
import com.javademo.elasticsearch.TestActions;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DemoApplication.class)
public class ThreadPoolDemo {

    @Autowired
    TestActions testActions;

    @Test
    public void testEsGet(){
        testActions.get("book", "3");
    }

    @Test
    public void testEsAdd() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "测试书籍2");
        data.put("price", 12.00);
        testActions.add("book","8",data);
    }

    @Test
    public void testUpdate(){
        Map<String, Object> data = new HashMap<>();
        data.put("name", "测试书籍3");
        data.put("price", 12.00);
        testActions.update("book", "8", data);
    }

    @Test
    public void testDelete(){
        testActions.delete("book", "8");
    }

    @Test
    public void testMget(){
        List<Map<String, String>> list = new ArrayList();
        Map<String, String> map = new HashMap<>();
        map.put("index", "book");
        map.put("id", "6");
        list.add(map);

        Map<String, String> map1 = new HashMap<>();
        map1.put("index", "book");
        map1.put("id", "7QXb1H8BGbzEOEZ4selI");
        list.add(map1);
        testActions.mget(list);
    }

    @Test
    public void testBulk(){
        List<DocWriteRequest> list = new ArrayList<>();

        String index = "book";
        String id = "7";
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        Map<String, Object> data = new HashMap<>();
        data.put("name", "测试书籍3");
        data.put("price", 12.00);
        String jsonString = JSON.toJSONString(data);
        indexRequest.source(jsonString, XContentType.JSON);

        UpdateRequest updateRequest = new UpdateRequest(index, id);
        updateRequest.doc(data);
        updateRequest.timeout("1s");
        updateRequest.retryOnConflict(3);

        DeleteRequest deleteRequest = new DeleteRequest(index, id);

        list.add(indexRequest);
        list.add(updateRequest);
        list.add(deleteRequest);
        testActions.bulk(list);
    }

    @Test
    public void testIndex() throws InterruptedException {
        String index = "food";
//        testActions.createIndex(index);
//        testActions.deleteIndex(index);
        testActions.deleteIndexAsync(index);
        Thread.sleep(1000);
    }
}
