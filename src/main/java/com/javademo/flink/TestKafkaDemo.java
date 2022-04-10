package com.javademo.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class TestKafkaDemo {

    private static final String TOPIC = "test";
    private static final String TOPICOUT = "test_out";
    private static final String GROUP = "0";
    private static final String SERVER_LIST = "127.0.0.1:9092";

    //flink-1.11.6
    //从kafka读取数据经过分割统计字符出现次数并返回给kafka
    //Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/flink/api/common/serialization/DeserializationSchema
    //Debug出现上面的错误提示，需将flink目录下lib的jar添加进项目的dependencies
    public static void main(String[] args) throws Exception {

        //获取一个执行程序
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //定义数据源
        //连接kafka的配置信息
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", SERVER_LIST);
        properties.setProperty("group.id", GROUP);

        //生成kafka消费者对象
        FlinkKafkaConsumer010<String> kafkaConsumer09 = new FlinkKafkaConsumer010<String>(TOPIC, new SimpleStringSchema(), properties);
        kafkaConsumer09.setStartFromEarliest(); //从最早的记录开始读取
        DataStream<String> dataStreamSource = env.addSource(kafkaConsumer09);
        dataStreamSource.print();

        //做数据转换
        //先把字符串做单个分割,然后统计次数
        DataStream<Tuple2<String, Integer>> tuple2DataStream = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                for (String word : s.split("")){
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        }).keyBy(0).sum(1);
        tuple2DataStream.print();

        //输出转换后数据kafka
        FlinkKafkaProducer010 kafkaProducer09 = new FlinkKafkaProducer010(TOPICOUT, new SimpleStringSchema(), properties);
        tuple2DataStream.addSink(kafkaProducer09);

        //执行
        env.execute("TestDemo");
    }
}
