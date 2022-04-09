package com.javademo.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接收消息类
 * 对单词进行分割
 */
public class CountBolt extends BaseRichBolt {

    //输出组件
    private OutputCollector collector;
    //记录单词出现次数
    private ConcurrentHashMap<String, Integer> countMap;

    /**
     * 初始化bolt
     * @param map 配置信息
     * @param topologyContext 任务信息
     * @param outputCollector 输出信息组件
     */
    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        System.out.println("Bolt初始化完成");
        this.collector = outputCollector;
        this.countMap = new ConcurrentHashMap<>();
    }

    @Override
    public void execute(Tuple tuple) {
        //获取第二阶段输出的信息，在Bolt类定义的消息格式
       String word = tuple.getStringByField("word");
        System.out.println("统计字符："+word + "，时间戳："+(new Date().getTime()));
       if (this.countMap.containsKey(word)){
           Integer count = this.countMap.get(word);
           count++;
           this.countMap.put(word, count);
       }else {
           this.countMap.put(word, 1);
       }
    }

    /**
     * 声明输出数据格式
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    //任务结束了释放资源
    @Override
    public void cleanup() {
        super.cleanup();
        System.out.println("word bolt cleanup");
        System.out.println("输出统计信息" + "，时间戳："+(new Date().getTime()));
        //完成之后输出统计信息
        for (Map.Entry<String, Integer> entry : countMap.entrySet()){
            System.out.println(entry.getKey()+"出现次数："+entry.getValue());
        }
    }
}
