package com.javademo.algorithm;

import scala.Array;

import java.util.ArrayList;

public class SkipList {

//    跳表具备以下特征：
//    1.跳表是可以实现二分查找的有序链表；
//    2.每个元素插入时随机生成它的level；
//    3.最底层包含所有的元素；
//    4.如果一个元素出现在level(x)，那么它肯定出现在x以下的level中；
//    5.每个索引节点包含前进指针和索引数组，参考Redis的zSet实现方式；
//    6.跳表查询、插入、删除的时间复杂度为O(log n)，与平衡二叉树接近；

    //跳跃表层级限制最大数量
    private static final int SKIPLISTMAXLEVEL = 32;

    //建立索引概率
    private static final double SKIPLIST_P = 0.25;

    //头节点
    public SkipListNode header;

    //尾节点
    public SkipListNode tail;

    //最大层级
    public int level;

    //跳跃表长度
    public int nodeLength;

    public SkipList(){
        super();

        //配置header对象
        this.header = createNode(0, null);
        this.header.backward = null;
        this.tail = null;
        this.level = 1;
        this.nodeLength = 0;

        //生成level数组
        SkipLevel[] skipLevels = new SkipLevel[SKIPLISTMAXLEVEL];
        for (int i = 0; i < SKIPLISTMAXLEVEL; i++) {
            SkipLevel skipLevel = new SkipLevel();
            skipLevel.forward = null;
            skipLevel.span = 0;
            skipLevels[i] = skipLevel;
        }
        this.header.levelArray = skipLevels;
    }

    //节点
    public class SkipListNode {

        //层级数组
        public SkipLevel[] levelArray;

        //后退指针
        public SkipListNode backward;

        //分值，用于排序
        public double zscore;

        //成员对象
        public String obj;

    }

    //层级
    public class SkipLevel {

        //前进指针
        private SkipListNode forward;

        //跨度
        private int span;
    }

    /**
     * 生成节点
     * @param score 分数，用于排序
     * @param obj 成员对象，节点储存的数据
     * @return
     */
    private SkipListNode createNode(double score, String obj){
        SkipListNode node = new SkipListNode();
        node.zscore = score;
        node.obj = obj;
        return node;
    }

    /**
     * 根据索引概率生成索引层级
     * @return
     */
    private int randomLevel(){
        int level = 1;
        while (Math.random() < SKIPLIST_P && level < SKIPLISTMAXLEVEL ){
            level += 1;
        }
        return level;
    }

    /**
     * 插入节点
     * @param score 分数，用于排序和决定插入位置
     * @param obj 节点存储的对象
     * @return
     */
    public SkipListNode insert(double score, String obj){
        //根据表的层级数从大到小遍历找到插入位置，存储每一层的节点和跨度
        SkipListNode x = this.header;
        SkipListNode[] updateNodes = new SkipListNode[SKIPLISTMAXLEVEL];
        int[] rank = new int[SKIPLISTMAXLEVEL];
        for (int i = this.level - 1; i >= 0; i--) {
            //先把层级跨度等于上一层跨度
            rank[i] = i == this.level - 1 ? 0 : rank[i+1];
            while ((x.levelArray[i].forward != null && x.levelArray[i].forward.zscore < score) ||
                    (x.levelArray[i].forward != null && x.levelArray[i].forward.zscore == score && !x.levelArray[i].forward.obj.equals(obj))){
                //将上一层跨度加上该层节点的跨度即为新的跨度
                rank[i] = rank[i] + x.levelArray[i].span;
                x = x.levelArray[i].forward;
            }
            updateNodes[i] = x;
        }

        //生成随机level，根据level更新节点
        int level = randomLevel();

        //如果随机level大于表的level，意味着表level以上这些层级都是header，先储存到数组
        if (level > this.level){
            for (int i = this.level; i < level; i++) {
                rank[i] = 0;
                updateNodes[i] = this.header;
                updateNodes[i].levelArray[i].span = this.nodeLength;
            }
            this.level = level;
        }

        //生成节点对象,更新各层级节点
        SkipListNode node = createNode(score, obj);
        node.levelArray = new SkipLevel[level];
        for (int i = 0; i < level; i++) {
            //插入节点
            SkipLevel tmpLevel = new SkipLevel();
            tmpLevel.forward = updateNodes[i].levelArray[i].forward;
            updateNodes[i].levelArray[i].forward = node;
            //更新节点跨度
            tmpLevel.span = updateNodes[i].levelArray[i].span - (rank[0] - rank[i]);
            updateNodes[i].levelArray[i].span = rank[0] - rank[i] + 1;
            node.levelArray[i] = tmpLevel;
        }

        //如果随机level小于表level，插入新节点以后随机level以上的节点的跨度要加一
        for (int i = level; i < this.level; i++) {
            updateNodes[i].levelArray[i].span = updateNodes[i].levelArray[i].span + 1;
        }

        //设置后退指针
        node.backward = updateNodes[0] == this.header ? null : updateNodes[0];
        if (node.levelArray[0].forward != null){
            node.levelArray[0].forward.backward = node;
        }else {
            this.tail = node;
        }

        //表长度加一
        this.nodeLength++;

        return node;

    }

    /**
     * 删除节点
     * @param score 分数，用于确定位置
     * @param obj 对象
     * @return
     */
    public boolean deleteNode(double score, String obj){
        //根据表的层级数从大到小遍历找到删除位置，存储每一层的节点
        SkipListNode x = this.header;
        SkipListNode[] updateNodes = new SkipListNode[SKIPLISTMAXLEVEL];
        for (int i = this.level - 1; i >= 0; i--) {
            while ((x.levelArray[i].forward != null && x.levelArray[i].forward.zscore < score) ||
                    (x.levelArray[i].forward != null && x.levelArray[i].forward.zscore == score && !x.levelArray[i].forward.obj.equals(obj))){
                x = x.levelArray[i].forward;
            }
            updateNodes[i] = x;
        }

        //如果x的前进指针等于要删除的对象，则删除
        x = x.levelArray[0].forward;
        if (x != null && x.zscore == score && x.obj.equals(obj)){
            //删除x并更新各层级节点
            for (int i = 0; i < level; i++) {
                //删除节点,跨度减一
                if (updateNodes[i].levelArray[i].forward == x){
                    updateNodes[i].levelArray[i].span = updateNodes[i].levelArray[i].span + x.levelArray[i].span - 1;
                    updateNodes[i].levelArray[i].forward = x.levelArray[i].forward;
                }else {
                    updateNodes[i].levelArray[i].span = updateNodes[i].levelArray[i].span - 1;
                }
            }

            //设置后退指针
            if (x.levelArray[0].forward != null){
                x.levelArray[0].forward.backward = x.backward;
            }else {
                this.tail = x.backward;
            }

            //更新层级
            while (this.level > 1 && this.header.levelArray[this.level-1].forward == null){
                this.level--;
            }

            //表长度减一
            this.nodeLength--;

            return true;
        }
        return false;
    }


}
