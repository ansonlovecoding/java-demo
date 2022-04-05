package com.javademo.paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//告知者，即学习者
public class Listener {

    //计票者姓名
    public String name;

    //计票数组
    public List<Vote> voteList;

    public Listener(){
        super();
        if (this.voteList == null){
            this.voteList = new ArrayList<>();
        }
    }

    /**
     * 接收每一位投票者收到的投票结果，进行计票并返回
     * @param vote 投票结果
     * @param acceptorSize 投票者数量
     */
    public void receiveVoteResult(Vote vote, int acceptorSize){
        //计票，超过半数的投票成立，输出投票结果
        if (this.voteList != null){
            this.voteList.add(vote);
//            System.out.println(this.name+"收到投票结果:"+vote.getProposerName()+"-"+vote.getVoteValue());

            ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<String, Integer>();
            if (this.voteList.size() == acceptorSize){
                for (Vote tmpVote:this.voteList) {
                    String key = tmpVote.getProposerName() + "-" + tmpVote.getVoteValue();
                    if (countMap.containsKey(key)){
                        Integer num = countMap.get(key);
                        num++;
                        countMap.put(key, num);
                        if (num > acceptorSize/2){
                            //超过半数的投票成立，输出投票结果
                            System.out.println(this.name + "得出提案结果：" + tmpVote.getVoteValue());
                            break;
                        }
                    }else {
                        countMap.put(key, 1);
                    }
                }
            }
        }


    }
}

