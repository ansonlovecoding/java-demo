package com.javademo.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//storm的消息发送类
public class Spout extends BaseRichSpout {

    //发送消息组件
    private SpoutOutputCollector collector;
    //消息内容
    private List<String> message;
    //输出的数据格式字段
    private static final String fieldName = "message";
    //保存发送的消息体
    private static List<String> stordMessages;

    /**
     * 初始化spout组件
     * @param map 配置信息
     * @param topologyContext 任务线程信息
     * @param spoutOutputCollector 发送消息组件
     */
    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        System.out.println("Spout初始化完成");
        this.collector = spoutOutputCollector;
        stordMessages = new ArrayList<>();
    }

    /**
     * spout的核心，通过该方法发送消息，消息内容被分割为list数据流形式
     */
    @Override
    public void nextTuple() {
        for (String tmpString : message){
            if (!stordMessages.contains(tmpString)){
                System.out.println("发送消息："+tmpString + "，时间戳："+(new Date().getTime()));
                this.collector.emit(new Values(tmpString));
                stordMessages.add(tmpString);
            }
        }
    }

    /**
     * 定义输出数据格式
     * 通过定义输出格式可以实现多阶段输出
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        System.out.println("first declareOutputFields");
        outputFieldsDeclarer.declare(new Fields(fieldName));
    }

    /**
     * 当一个tuple处理成功时调用此方法
     * @param msgId
     */
    @Override
    public void ack(Object msgId) {
        super.ack(msgId);
        System.out.println("消息流发送成功:"+msgId);
    }

    /**
     * 当一个tuple处理失败时调用此方法
     * @param msgId
     */
    @Override
    public void fail(Object msgId) {
        super.fail(msgId);
        System.out.println("消息流发送失败:"+msgId);
    }

    /**
     * 当任务活跃时
     */
    @Override
    public void activate() {
        super.activate();
        System.out.println("任务开始！");
    }

    /**
     * 当任务不活跃时
     */
    @Override
    public void deactivate() {
        super.deactivate();
        System.out.println("任务挂起！");
    }

    /**
     * 当任务结束时调用此方法
     */
    @Override
    public void close() {
        super.close();
        System.out.println("任务结束！");
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
