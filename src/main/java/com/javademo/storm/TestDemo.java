package com.javademo.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.ArrayList;
import java.util.List;

//统计消息里面单词的出现次数，不区分大小写
public class TestDemo {

    private static final String spoutId = "spout_test";
    private static final String boltId = "bolt_test";
    private static final String countBoltId = "count_bolt_test";

    public static void main(String[] args) {

        //定义拓扑
        TopologyBuilder builder = new TopologyBuilder();
        //定义输出Spout对象，发送消息
        Spout spout = new Spout();
        List<String> messageList = new ArrayList<>();
        //陶渊明《归园田居》
        messageList.add("少无适俗韵，性本爱丘山。误落尘网中，一去三十年。羁鸟恋旧林，池鱼思故渊。开荒南野际，守拙归园田。方宅十余亩，草屋八九间。榆柳荫后檐，桃李罗堂前。暧暧远人村，依依墟里烟。狗吠深巷中，鸡鸣桑树颠。户庭无尘杂，虚室有余闲。久在樊笼里，复得返自然。");
        messageList.add("野外罕人事，穷巷寡轮鞅。白日掩荆扉，虚室绝尘想。时复墟曲中，披草共来往。相见无杂言，但道桑麻长。桑麻日已长，我土日已广。常恐霜霰至，零落同草莽。");
        messageList.add("种豆南山下，草盛豆苗稀。晨兴理荒秽，带月荷锄归。道狭草木长，夕露沾我衣。衣沾不足惜， 但使愿无违。");
        messageList.add("久去山泽游，浪莽林野娱。试携子侄辈，披榛步荒墟。徘徊丘垄间，依依昔人居。井灶有遗处，桑竹残杇株。借问采薪者，此人皆焉如。薪者向我言，死没无复余。一世异朝市，此语真不虚。人生似幻化，终当归空无。");
        messageList.add("怅恨独策还，崎岖历榛曲。山涧清且浅，可以濯吾足。漉我新熟酒，只鸡招近局。日入室中暗，荆薪代明烛。欢来苦夕短，已复至天旭。");
        messageList.add("种苗在东皋，苗生满阡陌。虽有荷锄倦，浊酒聊自适。日暮巾柴车，路暗光已夕。归人望烟火，稚子候檐隙。问君亦何为，百年会有役。但愿桑麻成，蚕月得纺绩。素心正如此，开径望三益。");
        spout.setMessage(messageList);
        //定义输入Bolt对象，处理消息内容分割，转发给CountBolt
        Bolt bolt = new Bolt();
        //定义输入CountBolt对象，处理单词出现次数统计
        CountBolt countBolt = new CountBolt();
        //配置拓扑,线程数为1
        builder.setSpout(spoutId, spout, 1);
        //该bolt对象只接收message字段的数据,设置线程数为1，任务数为1，负责分割消息内容
        builder.setBolt(boltId, bolt, 1).setNumTasks(1).fieldsGrouping(spoutId, new Fields("message"));
        //该bolt对象只接收word字段的数据,设置线程数为1，任务数为1，负责统计单词出现次数
        builder.setBolt(countBoltId, countBolt, 1).setNumTasks(1).fieldsGrouping(boltId, new Fields("word"));
        //配置信息
        Config config = new Config();
        config.put("test", "count word");
        //本地模式
        try {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("CountWord", config, builder.createTopology());
            Thread.sleep(3000);
            localCluster.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
