package com.javademo.paxos;

import org.assertj.core.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PaxosDemo {

    //根据paxos分布一致性算法原理，解决如下案例：
    //班级一共有11个人，他们在进行班长选举活动，其中小明发起提议让A做班长，
    //小红发起提议让B做班长，小张提议让C做班长，班级10个人都对这三个提议分别投票，当提议达成半数以上同意则选举成功

    //paxos流程：
    //1.proposer发起提案给所有acceptor，提案编号为递增N
    //2.acceptor收到提案，如果之前没有接收过提案则接收该提案并响应，如果之前已接收过提案则判断提案编号N是否大于之前接收过的编号，是则接收并返回已接收的提案value，否则不接收
    //3.proposer收到acceptor对提案的响应，如果超过半数则提案N选举成功，当有响应value时提案value为响应值，没有响应value则为提案原来的value，没有超过半数则继续生成新提案选举
    //4.经过不断选举最终多个proposer都达成一致的结果，proposer就把结果发送给响应它结果的acceptor，acceptor判断结果为最新的则通知给Listener
    //5.Listener获得所有acceptor的通知，按照少数服从多数的原则取最多acceptor同意的value
    public static void paxosTest(){
        //生成三个提案者，分别是小明，小红，小张
        Proposer xiaoming = new Proposer();
        xiaoming.name = "小明";

        Proposer xiaohong = new Proposer();
        xiaohong.name = "小红";

        Proposer xiaozhang = new Proposer();
        xiaozhang.name = "小张";

        //班集体11个人代表有11个Acceptor，同时也代表有11个Lisenter，因为超过半数的原则所以把班集体人数设定为单数
        int classSize = 11;
        List<Acceptor> accepterList = new ArrayList<>();
        List<Listener> listenerList = new ArrayList<>();
        for (int i = 0; i < classSize; i++) {
            Acceptor accepter = new Acceptor();
            accepter.name = "投票同学-"+(i+1)+"";
            accepterList.add(accepter);

            Listener listener = new Listener();
            listener.name = "计票同学-"+(i+1)+"";
            listenerList.add(listener);
        }

        //三位提案者同时发起投票活动
        //小明的投票活动
        CompletableFuture<Vote> xiaomingVote = CompletableFuture.supplyAsync(() -> {
            System.out.println("小明投票活动开始："+ DateUtil.now());
            //当没有获得半数响应则不断发起提案
            Vote vote = null;
            Integer voteNum = 0;
            while (true){
                voteNum++;
                System.out.println("小明发起投票序号："+voteNum);
                //小明不断发起投票让A做班长，直至得出超过半数的结果
                Vote tmpVote = new Vote();
                tmpVote.setProposerName("小明");
                tmpVote.setVoteNum(voteNum);
                tmpVote.setVoteValue("A");
                vote = xiaoming.sendVote(tmpVote, accepterList);
                if (vote == null){
                    //当没得到共识结果，继续投票
                    continue;
                }else {
                    //得到共识结果，结束投票
                    break;
                }
            }

            //通知所有投票者共识结果
            for (Acceptor acceptor: accepterList) {
                acceptor.accept(vote, listenerList, accepterList.size());
            }

            return vote;
        });
        xiaomingVote.thenAccept((vote) -> {
            System.out.println("小明投票活动完成,得出共识结果："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
        });
        xiaomingVote.exceptionally((e) -> {
            System.out.println("小明投票活动出现异常：" + e.getMessage());
            return null;
        });

        //小红的投票活动
        CompletableFuture<Vote> xiaohongVote = CompletableFuture.supplyAsync(() -> {
            System.out.println("小红投票活动开始："+ DateUtil.now());
            //当没有获得半数响应则不断发起提案
            Vote vote = null;
            Integer voteNum = 0;
            while (true){
                voteNum++;
                System.out.println("小红发起投票序号："+voteNum);
                //小明不断发起投票让A做班长，直至得出超过半数的结果
                Vote tmpVote = new Vote();
                tmpVote.setProposerName("小红");
                tmpVote.setVoteNum(voteNum);
                tmpVote.setVoteValue("B");
                vote = xiaohong.sendVote(tmpVote, accepterList);
                if (vote == null){
                    //当没得到共识结果，继续投票
                    continue;
                }else {
                    //得到共识结果，结束投票
                    break;
                }
            }

            //通知所有投票者共识结果
            for (Acceptor acceptor: accepterList) {
                acceptor.accept(vote, listenerList, accepterList.size());
            }

            return vote;
        });
        xiaohongVote.thenAccept((vote) -> {
            System.out.println("小红投票活动完成,得出共识结果："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
        });
        xiaohongVote.exceptionally((e) -> {
            System.out.println("小红投票活动出现异常：" + e.getMessage());
            return null;
        });

        //小张的投票活动
        CompletableFuture<Vote> xiaozhangVote = CompletableFuture.supplyAsync(() -> {
            System.out.println("小张投票活动开始："+ DateUtil.now());
            //当没有获得半数响应则不断发起提案
            Vote vote = null;
            Integer voteNum = 0;
            while (true){
                voteNum++;
                System.out.println("小张发起投票序号："+voteNum);
                //小明不断发起投票让A做班长，直至得出超过半数的结果
                Vote tmpVote = new Vote();
                tmpVote.setProposerName("小张");
                tmpVote.setVoteNum(voteNum);
                tmpVote.setVoteValue("C");
                vote = xiaozhang.sendVote(tmpVote, accepterList);
                if (vote == null){
                    //当没得到共识结果，继续投票
                    continue;
                }else {
                    //得到共识结果，结束投票
                    break;
                }
            }

            //通知所有投票者共识结果
            for (Acceptor acceptor: accepterList) {
                acceptor.accept(vote, listenerList, accepterList.size());
            }

            return vote;
        });
        xiaozhangVote.thenAccept((vote) -> {
            System.out.println("小张投票活动完成,得出共识结果："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
        });
        xiaozhangVote.exceptionally((e) -> {
            System.out.println("小张投票活动出现异常：" + e.getMessage());
            return null;
        });
    }

    public static void main(String[] args) throws InterruptedException {
        //发起测试，观察输出
        paxosTest();
        Thread.sleep(5000);
    }

}
