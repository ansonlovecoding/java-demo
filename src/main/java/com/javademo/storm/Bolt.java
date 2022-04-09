package com.javademo.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Date;
import java.util.Map;

/**
 * 接收消息类
 * 对单词进行分割
 */
public class Bolt extends BaseRichBolt {

    //输出组件
    private OutputCollector collector;
    //输出信息数据格式字段
    private static final String fieldName = "word";

    /**
     * 初始化bolt
     * @param map 配置信息
     * @param topologyContext 任务信息
     * @param outputCollector 输出信息组件
     */
    @Override
    public void prepare(Map<String, Object> map, TopologyContext topologyContext, OutputCollector outputCollector) {
        System.out.println("CountBolt初始化完成");
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        //获取第一阶段输出的信息，在spout定义的消息格式
       String message = tuple.getStringByField("message");
        System.out.println("分割消息："+message + "，时间戳："+(new Date().getTime()));
       //对消息进行分割，每个单词发送给下一个bolt进行单词统计
       String[] words = message.toLowerCase().split("");
       if (words != null && words.length > 0){
           for (String word : words){
               this.collector.emit(new Values(word));
           }
       }

    }

    /**
     * 声明输出数据格式
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        System.out.println("second declareOutputFields");
        outputFieldsDeclarer.declare(new Fields(fieldName));
    }

    //任务结束了释放资源
    @Override
    public void cleanup() {
        super.cleanup();
        System.out.println("message bolt cleanup");
    }
}
