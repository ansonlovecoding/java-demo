package com.javademo.paxos;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//附议者，即投票者
public class Acceptor {

    //投票者姓名
    public String name;

    //最新提案编号
    public Integer latestVoteNum;

    //赞成的提案
    public Vote acceptVote;

    //多数赞成的提案
    public Vote  latestAcceptVote;

    /**
     * 接收提案者发过来的提案并返回响应内容
     * @param vote 提案
     * @return
     */
    public Vote receiveVote(Vote vote){
        //使用锁防止多个线程修改同一对象，造成投票者改动其投票结果
        synchronized (this){
            //判断自己是否接收过提案，有则需判断接收的提案是否最新
            //对最新提案做出响应，响应包括接收到提案编号，以及自己锁赞成的提案内容
            if (this.latestVoteNum != null){
                if (vote.getVoteNum() > this.latestVoteNum){
                    this.latestVoteNum = vote.getVoteNum();
                    vote.setVoteValue(acceptVote.getVoteValue());
                    System.out.println(this.name+"-收到新提案，返回已赞成提案："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
                    return vote;
                }
                System.out.println(this.name+"-提案编号小于已接收提案被拒："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
                return null;

            }else {
                //默认赞成第一份接收到的提案，加入成功率，现实中可更改其逻辑
                boolean result = false;
                if (Math.random() > 0.5){
                    result = true;
                }

                if (result){
                    this.acceptVote = vote;
                    this.latestVoteNum = vote.getVoteNum();
                    System.out.println(this.name+"-赞成第一份提案："+vote.getProposerName()+"-"+vote.getVoteNum()+"-"+vote.getVoteValue());
                    return vote;
                }else {
                    System.out.println(this.name+"未投票");
                    return null;
                }

            }
        }
    }

    /**
     * 接收提案者发送过来的超过半数同意的提案内容
     * @param vote 提案
     * @param listenerList 计票者数组
     * @param acceptorSize 投票者数量
     */
    public void accept(Vote vote, List<Listener> listenerList, Integer acceptorSize){
        //判断提案是否最新提案，并将投票结果通知给计票者
        if (vote.getVoteNum() >= this.latestVoteNum){
            this.latestAcceptVote = vote;

            for (Listener listener:listenerList) {
                listener.receiveVoteResult(vote, acceptorSize);
            }
        }
    }
}
