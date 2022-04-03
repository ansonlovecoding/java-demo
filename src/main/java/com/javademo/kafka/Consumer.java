package com.javademo.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Properties;

public class Consumer {

    static Logger logger = LogManager.getLogger(Consumer.class);

    private static Consumer instance = null;

    private static final String TOPIC = "test";
    private static final String GROUP = "0";
    private static final String SERVER_LIST = "127.0.0.1:9092";
    private KafkaConsumer<String,String> consumer = null;

    public static Consumer getInstance() {
        if (instance == null){
            instance = new Consumer();
            Properties properties = initConfig();
            instance.consumer = new KafkaConsumer<String, String>(properties);
            instance.consumer.subscribe(Collections.singletonList(TOPIC));
        }
        return instance;
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }

    public void start(){
        try {
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(10);
                for (ConsumerRecord<String, String> record : records){
                    System.out.println("收到消息："+record.value());
                }
            }
        }catch (Exception e){
            consumer.close();
        }finally {
            consumer.close();
        }
    }
}
