package com.javademo.paxos;

//提案类
public class Vote {

    //提案者姓名
    private String proposerName;

    //提案编号
    private Integer voteNum;

    //提案内容
    private String voteValue;

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteValue(String voteValue) {
        this.voteValue = voteValue;
    }

    public String getVoteValue() {
        return voteValue;
    }
}
