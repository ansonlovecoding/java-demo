package com.javademo.algorithm;

import com.sun.corba.se.spi.ior.ObjectKey;

import java.util.*;

public class BTree {

//    B树也称B-树,它是一颗多路平衡查找树。我们描述一颗B树时需要指定它的阶数，阶数表示了一个结点最多有多少个孩子结点，一般用字母m表示阶数。当m取2时，就是我们常见的二叉搜索树。
//    一颗m阶的B树定义如下：
//            1）每个结点最多有m-1个关键字。
//            2）根结点最少可以只有1个关键字。
//            3）非根结点至少有Math.ceil(m/2)-1个关键字。
//            4）每个结点中的关键字都按照从小到大的顺序排列，每个关键字的左子树中的所有关键字都小于它，而右子树中的所有关键字都大于它。
//            5）所有叶子结点都位于同一层，或者说根结点到每个叶子结点的长度都相同。

    public class BTNode{

        //关键字数组
        public Map<Integer, Object> keys;

        //子节点列表
        public List<BTNode> childs;

        //父节点，方便回溯
        public BTNode father;

        /**
         * 构造函数
         */
        public BTNode(){
            //每个结点最多有m-1个关键字,但预留多一个方便分裂
            this.keys = new HashMap<>(m);
            //阶数代表子节点数
            this.childs = new ArrayList<>(m);
        }
    }

    //根节点
    public BTNode root;

    //阶数
    private int m;

    /**
     * 构造函数
     * @param m 阶数
     */
    public BTree(int m){
        this.root = new BTNode();
        this.m = m;
    }

    public BTNode addNode(BTNode node, Integer key, Object value){

        if (node == null){
            node = new BTNode();
            node.keys.put(key, value);
            return node;
        }

        if (node.keys.size() < this.m){
            //未超过关键字数量
            node.keys.put(key, value);

        }else {
            //已超过关键字数量
            node.keys.put(key, value);

            int centerNum = this.m/2;
            Integer[] keyArray = node.keys.keySet().toArray(new Integer[node.keys.size()]);
            Map<Integer, Object> oldData = node.keys;
            List<BTNode> oldChilds = node.childs;

            Integer centerKey = keyArray[centerNum];
            Object centerData = node.keys.get(centerKey);

            BTNode father = node.father;
            if (father == null){
                father = new BTNode();
                father.keys.put(centerKey, centerData);
                node.father = father;
            }else {
                father = addNode(father, centerKey, centerData);
                node.father = father;
            }

            BTNode anotherNode = new BTNode();
            for (int i = 0; i < keyArray.length; i++) {
                //更新关键字
                Integer tmpKey = keyArray[i];
                Object tmpData = oldData.get(tmpKey);
                if (tmpKey > centerKey){
                    anotherNode.keys.put(tmpKey, tmpData);
                    node.keys.remove(tmpKey);
                }
            }

            for (BTNode tmpNode:oldChilds
            ) {
                //更新子节点
                Integer[] tmpKeys = tmpNode.keys.keySet().toArray(new Integer[tmpNode.keys.keySet().size()]);
                if (tmpNode.keys.size() > 0 && tmpKeys[0] > centerKey){
                    anotherNode.childs.add(tmpNode);
                    node.childs.remove(tmpNode);
                }
            }

            //把新节点添加到父节点的子节点列表
            node.father.childs.add(anotherNode);
        }

        //对关键字和子节点排序
        return sortKeysAndChilds(node);
    }

    /**
     * 给节点的关键字和子节点排序
     * @param node
     * @return
     */
    private BTNode sortKeysAndChilds(BTNode node){
        if (node.keys.size() > 0){
            ArrayList<Integer> list = new ArrayList<>(node.keys.keySet());
            Collections.sort(list, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1 > o2 ? 1 : -1;
                }
            });

            Map<Integer, Object> newKeys = new HashMap<>(node.keys.size());
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()){
                Integer tmpKey = iterator.next();
                Object tmpValue = node.keys.get(tmpKey);
                newKeys.put(tmpKey, tmpValue);
            }
            node.keys = newKeys;
        }

        if (node.childs.size() > 0){
            node.childs.sort(new Comparator<BTNode>() {
                @Override
                public int compare(BTNode o1, BTNode o2) {
                    Integer[] o1Keys = o1.keys.keySet().toArray(new Integer[o1.keys.size()]);
                    Integer[] o2Keys = o2.keys.keySet().toArray(new Integer[o2.keys.size()]);
                    return o1Keys[0] > o2Keys[0] ? 1 : -1;
                }
            });
        }
        return node;
    }
}
