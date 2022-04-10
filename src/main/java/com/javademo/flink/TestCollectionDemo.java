package com.javademo.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestCollectionDemo {

    //flink-1.11.6
    //从kafka读取数据经过分割统计字符出现次数并返回给kafka
    //Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/flink/api/common/serialization/DeserializationSchema
    //Debug出现上面的错误提示，需将flink目录下lib的jar添加进项目的dependencies
    public static void main(String[] args) throws Exception {

        //获取一个执行程序
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //定义数据源
        List<String> messageList = new ArrayList<>();
        //陶渊明《归园田居》
        messageList.add("少无适俗韵，性本爱丘山。误落尘网中，一去三十年。羁鸟恋旧林，池鱼思故渊。开荒南野际，守拙归园田。方宅十余亩，草屋八九间。榆柳荫后檐，桃李罗堂前。暧暧远人村，依依墟里烟。狗吠深巷中，鸡鸣桑树颠。户庭无尘杂，虚室有余闲。久在樊笼里，复得返自然。");
        messageList.add("野外罕人事，穷巷寡轮鞅。白日掩荆扉，虚室绝尘想。时复墟曲中，披草共来往。相见无杂言，但道桑麻长。桑麻日已长，我土日已广。常恐霜霰至，零落同草莽。");
        messageList.add("种豆南山下，草盛豆苗稀。晨兴理荒秽，带月荷锄归。道狭草木长，夕露沾我衣。衣沾不足惜， 但使愿无违。");
        messageList.add("久去山泽游，浪莽林野娱。试携子侄辈，披榛步荒墟。徘徊丘垄间，依依昔人居。井灶有遗处，桑竹残杇株。借问采薪者，此人皆焉如。薪者向我言，死没无复余。一世异朝市，此语真不虚。人生似幻化，终当归空无。");
        messageList.add("怅恨独策还，崎岖历榛曲。山涧清且浅，可以濯吾足。漉我新熟酒，只鸡招近局。日入室中暗，荆薪代明烛。欢来苦夕短，已复至天旭。");
        messageList.add("种苗在东皋，苗生满阡陌。虽有荷锄倦，浊酒聊自适。日暮巾柴车，路暗光已夕。归人望烟火，稚子候檐隙。问君亦何为，百年会有役。但愿桑麻成，蚕月得纺绩。素心正如此，开径望三益。");
        DataStream<String> dataStreamSource = env.fromCollection(messageList);
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


        //输出转换后数据
        tuple2DataStream.print();

        //执行
        env.execute("TestDemo");
    }
}
