package com.javademo.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Producer {

    static Logger logger = LogManager.getLogger(Producer.class);

    private static Producer instance = null;

    private static final String TOPIC = "test";
    private static final String SERVER_LIST = "127.0.0.1:9092";
    private KafkaProducer<String,String> producer = null;

    public static Producer getInstance() {
        if (instance == null){
            instance = new Producer();
            Properties properties = initConfig();
            instance.producer = new KafkaProducer<String, String>(properties);
        }
        return instance;
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,SERVER_LIST);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public void sendMessage(String message){
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, message);
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null){
                    System.out.println("发送消息异常："+e.getMessage());
                    logger.error("发送消息异常："+e.getMessage());
                }else {
                    System.out.println(String.format("offset:%s,partition:%s",recordMetadata.offset(), recordMetadata.partition()));
                }
            }
        });
    }
}
