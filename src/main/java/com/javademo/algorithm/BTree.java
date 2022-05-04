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
        public List<Map<Integer, Object>> keys;

        //子节点列表
        public List<BTNode> childs;

        //父节点，方便回溯
        public BTNode father;

        /**
         * 构造函数
         */
        public BTNode(){
            //每个结点最多有m-1个关键字,但预留多一个方便分裂
            this.keys = new ArrayList<>(m);
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

    /**
     * 给树的根节点添加节点
     * @param key 关键字key
     * @param value 关键字value
     */
    public void add(Integer key, Object value){
        BTNode node = this.addNode(this.root, key, value, true);
        BTNode father = node.father;
        while (father != null && father.father != null){
            father = father.father;
        }
        if (father != null){
            this.root = father;
        }else {
            this.root = node;
        }
    }

    /**
     * 添加节点
     * @param node B树
     * @param key 关键字key
     * @param value 关键字value
     * @param isDeep 是否深入子节点添加
     * @return
     */
    private BTNode addNode(BTNode node, Integer key, Object value, boolean isDeep){

        //是否分裂父节点
        boolean hasNewFather = false;


        Map<Integer, Object> data = new HashMap<>();
        data.put(key, value);

        //树为空时生成新节点
        if (node == null){
            node = new BTNode();
            node.keys.add(data);
            return node;
        }

        //如果有子节点则插入子节点
        if (node.childs.size()>0 && isDeep){
            int childNum = node.childs.size();
            for (int i = 0; i < childNum; i++) {
                if (i == 0){
                    //获取第一个关键字，看是否小于它，小于它则存进第一个节点
                    Integer tmpKeys[] = node.keys.get(0).keySet().toArray(new Integer[1]);
                    if (tmpKeys.length > 0 && key < tmpKeys[0]){
                        BTNode tmpNode = node.childs.get(0);
                        tmpNode = addNode(tmpNode, key, value, isDeep);
                        break;
                    }
                }else if (i == childNum-1){
                    //获取最后一个关键字，看是否大于它
                    Integer tmpKeys[] = node.keys.get(node.keys.size()-1).keySet().toArray(new Integer[1]);
                    if (tmpKeys.length > 0 && key > tmpKeys[0]){
                        BTNode tmpNode = node.childs.get(node.childs.size()-1);
                        tmpNode = addNode(tmpNode, key, value, isDeep);
                        break;
                    }
                }else {
                    //获取上一个关键字和i对应的关键字，看是否在二者之间
                    Integer tmpKeys1[] = node.keys.get(i-1).keySet().toArray(new Integer[1]);
                    Integer tmpKeys2[] = node.keys.get(i).keySet().toArray(new Integer[1]);
                    if (tmpKeys1.length > 0 && tmpKeys2.length > 0 && key > tmpKeys1[0] && key < tmpKeys2[0]){
                        BTNode tmpNode = node.childs.get(i);
                        tmpNode = addNode(tmpNode, key, value, isDeep);
                        break;
                    }
                }
            }
        }else {

            //当已存在相同的关键字则直接返回
            for (Map<Integer, Object> obj : node.keys
                 ) {
                Integer[] tmpKeys = obj.keySet().toArray(new Integer[1]);
                if (tmpKeys[0] == key){
                    return node;
                }
            }

            //关键字最多为m-1，超过m-1需要进行分裂
            if (node.keys.size() < this.m-1){
                //未超过关键字数量
                node.keys.add(data);

            }else {
                //已超过关键字数量
                node.keys.add(data);

                node.keys.sort(new Comparator<Map<Integer, Object>>() {
                    @Override
                    public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                        Integer[] o1Key = o1.keySet().toArray(new Integer[o1.keySet().size()]);
                        Integer[] o2Key = o2.keySet().toArray(new Integer[o2.keySet().size()]);
                        return  o1Key[0] > o2Key[0] ? 1 : -1;
                    }
                });

                //取出中间的关键字
                int centerNum = this.m/2;
                List<Map<Integer, Object>> oldData = new ArrayList<>(node.keys);
                List<BTNode> oldChilds = new ArrayList<>(node.childs);

                Map<Integer, Object> centerData = node.keys.get(centerNum);
                Integer centerKey = centerData.keySet().toArray(new Integer[centerData.keySet().size()])[0];
                Object centerValue = centerData.get(centerKey);

                //将中间关键字存到父节点
                BTNode father = node.father;
                if (father == null){
                    father = new BTNode();
                    father.keys.add(centerData);
                    node.father = father;
                    hasNewFather = true;
                }else {
                    father = addNode(father, centerKey, centerValue, false);
                    node.father = father;
                }

                //新分裂的兄弟节点，将原来的关键字和子节点重新分配
                BTNode anotherNode = new BTNode();
                anotherNode.father = node.father;
                node.keys.remove(centerData);
                for (int i = 0; i < oldData.size(); i++) {
                    //更新关键字
                    Map<Integer, Object> tmpData = oldData.get(i);
                    if (i > centerNum){
                        anotherNode.keys.add(tmpData);
                        node.keys.remove(tmpData);
                    }
                }

                for (BTNode tmpNode:oldChilds
                ) {
                    //更新子节点
                    Map<Integer,Object> firstKey = tmpNode.keys.get(0);
                    Integer[] tmpKeys = firstKey.keySet().toArray(new Integer[firstKey.keySet().size()]);
                    if (tmpNode.keys.size() > 0 && tmpKeys[0] > centerKey){
                        anotherNode.childs.add(tmpNode);
                        node.childs.remove(tmpNode);
                    }
                }

                //把新节点添加到父节点的子节点列表
                anotherNode = sortKeysAndChilds(anotherNode);
                if (hasNewFather){
                    node.father.childs.add(node);
                }
                node.father.childs.add(anotherNode);
            }

            //对关键字和子节点排序
            node = sortKeysAndChilds(node);
            node.father = sortKeysAndChilds(node.father);

        }

        return node;
    }

    /**
     * 给节点的关键字和子节点排序
     * @param node
     * @return
     */
    private BTNode sortKeysAndChilds(BTNode node){
        if (node == null){
            return null;
        }

        if (node.keys.size() > 0){
            node.keys.sort(new Comparator<Map<Integer, Object>>() {
                @Override
                public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                    Integer[] o1Key = o1.keySet().toArray(new Integer[o1.keySet().size()]);
                    Integer[] o2Key = o2.keySet().toArray(new Integer[o2.keySet().size()]);
                    return  o1Key[0] > o2Key[0] ? 1 : -1;
                }
            });

        }

        if (node.childs.size() > 0){
            node.childs.sort(new Comparator<BTNode>() {
                @Override
                public int compare(BTNode o1, BTNode o2) {
                    Integer[] o1Keys = o1.keys.get(0).keySet().toArray(new Integer[o1.keys.get(0).keySet().size()]);
                    Integer[] o2Keys = o2.keys.get(0).keySet().toArray(new Integer[o2.keys.get(0).keySet().size()]);
                    return o1Keys[0] > o2Keys[0] ? 1 : -1;
                }
            });
        }
        return node;
    }
}
