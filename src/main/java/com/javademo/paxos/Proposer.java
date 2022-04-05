package com.javademo.paxos;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//提案者
public class Proposer {

    //提案者姓名
    public String name;

    //提案编号
    public String voteNum;

    //提案内容
    public String voteValue;

    /**
     * 发送提案给所有投票者，统计投票者返回得出超过半数以上的结果
     * @param vote 提案
     * @param acceptorList 投票者
     * @return
     */
    public Vote sendVote(Vote vote, List<Acceptor> acceptorList){
        //统计提案内容返回的结果得出超过半数的提案内容

        ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<String, Integer>();
        for (int i = 0; i < acceptorList.size(); i++) {
            Acceptor acceptor = acceptorList.get(i);
            Vote receiveVote = acceptor.receiveVote(vote);
            if (receiveVote != null){
//                System.out.println(acceptor.name + ":" + receiveVote.getVoteValue());
                if (countMap.containsKey(receiveVote.getVoteValue())){
                    Integer num = countMap.get(receiveVote.getVoteValue());
                    num++;
                    countMap.put(receiveVote.getVoteValue(), num);
                }else {
                    countMap.put(receiveVote.getVoteValue(), 1);
                }
            }else {
//                System.out.println(acceptor.name + ":null" );
            }
        }

        //根据投票结果统计超过半数的结果并返回投票内容
        Vote newVote = null;
        for (String key:countMap.keySet()) {
            Integer num = countMap.get(key);
            if (num != null && num > acceptorList.size()/2){
                newVote = new Vote();
                newVote.setProposerName(vote.getProposerName());
                newVote.setVoteNum(vote.getVoteNum());
                newVote.setVoteValue(key);
//                System.out.println("统计超过半数！");
                break;
            }
        }

        return newVote;
    }

}
