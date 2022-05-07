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

        //记录在父节点的子节点列表位置，方便取兄弟节点
        public Integer index;

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
     * 给树添加节点
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
     * 给树删除节点
     * @param key
     */
    public void remove(Integer key){
        BTNode node = this.removeNode(this.root, key, null);
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
                        tmpNode.father = anotherNode;
                        anotherNode.childs.add(tmpNode);
                        node.childs.remove(tmpNode);
                    }
                }

                //把新节点添加到父节点的子节点列表
                anotherNode = sortKeysAndChilds(anotherNode);
                if (hasNewFather){
                    node.father.childs.add(node);
                }
                anotherNode.father = node.father;
                node.father.childs.add(anotherNode);
            }

            //对关键字和子节点排序
            node = sortKeysAndChilds(node);
            node.father = sortKeysAndChilds(node.father);

        }

        return node;
    }

    /**
     * 删除节点
     * @param node 树
     * @param key 关键字key
     * @return
     */
    private BTNode removeNode(BTNode node, Integer key, Integer childIndex){

        if (node == null){
            return null;
        }

        //遍历关键字列表，查找不到则根据删除的key值遍历对应的子节点

        //非根节点最少关键字数量,注意是除以2.0，ceil是对浮点数向上取整
        Double minmumKeyNum = Math.ceil(this.m/2.0)-1;

        List<Map<Integer, Object>> oldKeys = new ArrayList<>(node.keys);
        for (int i=0; i < oldKeys.size(); i++) {
            Map<Integer, Object> obj = oldKeys.get(i);
            Integer[] tmpKeys = obj.keySet().toArray(new Integer[1]);

            List<BTNode> oldChilds = new ArrayList<>(node.childs);

            if (key == tmpKeys[0]){
                //执行删除逻辑
                node.keys.remove(obj);

                //删除后关键字数量
                int keyNum = node.keys.size();

                //删除的是叶子节点的关键字，只要不少于Math.celi(m/2)-1个关键字，删除结束，否则有以下情况：
                //1.父节点下沉，如果前面或后面的兄弟节点关键字数量大于Math.celi(m/2)-1，从兄弟节点抽取关键字上移到父节点
                //2.父节点下沉，如果前后兄弟节点关键字数量不大于Math.celi(m/2)-1，则该叶子节点选择前后任意兄弟节点合并为一个子节点
                if (oldChilds.size() == 0 && keyNum < minmumKeyNum && childIndex != null){
                    //父节点关键字
                    List<Map<Integer, Object>> tmpFatherKeys = new ArrayList<>(node.father.keys);
                    //根据节点index来判断要下沉父节点的关键字
                    Map<Integer, Object> fatherKey;
                    if (childIndex == 0){
                        //节点是子节点列表里的第一个，下沉父节点关键字列表的第一个'
                        fatherKey = tmpFatherKeys.get(0);
                    }else if (childIndex == node.father.childs.size()-1){
                        //节点是子节点列表里的最后一个，下沉父节点关键字列表的最后一个
                        fatherKey = tmpFatherKeys.get(tmpFatherKeys.size()-1);
                    }else {
                        //节点位于子节点列表中间，则需判断其左右兄弟节点的关键字数是否不超过Math.celi(m/2)-1
                        //如果左节点没有超过则下沉父节点第(childIndex-1)关键字,如果右节点没有超过则下沉父节点第childIndex关键字
                        BTNode prefixNode = node.father.childs.get(childIndex-1);
                        if (prefixNode.keys.size() < minmumKeyNum){
                            fatherKey = tmpFatherKeys.get(childIndex-1);
                        }else {
                            fatherKey = tmpFatherKeys.get(childIndex);
                        }
                    }

                    //将要下沉的key添加到节点，从父节点删除该key
                    Integer downKey = fatherKey.keySet().toArray(new Integer[1])[0];
                    Object downValue = fatherKey.get(downKey);
                    node = addNode(node, downKey, downValue, false);
                    node.father = removeNode(node.father, downKey, null);

                    return node;
                }


                //删除的为非叶子节点。分为以下情况：
                //1.该节点的左右孩子节点有关键字数量大于Math.celi(m/2)-1的节点，左孩子则取最大关键字右孩子则取最小关键字替换删除的节点，左右孩子对应剔除取出的关键字
                //2.该节点的左右孩子节点关键字数量都不大于Math.celi(m/2)-1的节点，将左右孩子合并
                if (oldChilds.size() > 0){
                    //原节点关键字
                    List<Map<Integer, Object>> tmpFatherKeys = oldKeys;
                    //根据删除的关键字index判断需要操作的左右孩子节点以及要从孩子节点上浮的关键字
                    BTNode leftNode = null;
                    BTNode rightNode = null;
                    Map<Integer, Object> leftUpKey = null;
                    Map<Integer, Object> rightUpKey = null;

                    if (i == 0){
                        //删除关键字为第一个，左右孩子为第一个和第二个
                        leftNode = oldChilds.get(0);
                        rightNode = oldChilds.get(1);
                    }else if (i == oldChilds.size()-1){
                        //删除关键字为最后一个，左右孩子为倒数两个
                        leftNode = oldChilds.get(oldChilds.size()-2);
                        rightNode = oldChilds.get(oldChilds.size()-1);
                    }else {
                        //删除关键字为最后一个，左右孩子为第i和第i+1个
                        leftNode = oldChilds.get(i);
                        rightNode = oldChilds.get(i + 1);
                    }

                    //当左孩子关键字数大于Math.celi(m/2)-1取其关键字最后一个上浮，当右孩子关键字数大于Math.celi(m/2)-1取其关键字第一个上浮
                    if (leftNode != null && leftNode.keys.size() > minmumKeyNum){
                        leftUpKey = leftNode.keys.get(leftNode.keys.size()-1);
                    }else if (rightNode != null && rightNode.keys.size() > minmumKeyNum){
                        rightUpKey = rightNode.keys.get(0);
                    }

                    //当有上浮关键字则上浮，不需合并左右孩子，否则需要合并左右孩子
                    if (leftUpKey != null){
                        Integer tmpLeftKey = leftUpKey.keySet().toArray(new Integer[1])[0];
                        Object tmpLeftValue = leftUpKey.get(tmpLeftKey);
                        leftNode = removeNode(leftNode, tmpLeftKey, null);
                        node = addNode(node, tmpLeftKey, tmpLeftValue, false);
                    }else if (rightUpKey != null){
                        Integer tmpRightKey = rightUpKey.keySet().toArray(new Integer[1])[0];
                        Object tmpRightValue = rightUpKey.get(tmpRightKey);
                        rightNode = removeNode(rightNode, tmpRightKey, null);
                        node = addNode(node, tmpRightKey, tmpRightValue, false);
                    }else {
                        leftNode.keys.addAll(rightNode.keys);
                        leftNode.childs.addAll(rightNode.childs);
                        node.childs.remove(rightNode);
                        rightNode = null;
                    }

                    //非根节点，关键字少于Math.ceil(m/2)-1，需要从父节点下沉关键字
                    //下沉后如果兄弟节点关键字数量大于Math.celi(m/2)-1，从兄弟节点抽取关键字上移到父节点
                    if (node.keys.size() < minmumKeyNum && node.father != null){
                        List<BTNode> oldFatherChilds = new ArrayList<>(node.father.childs);
                        List<Map<Integer, Object>> oldFatherKeys = new ArrayList<>(node.father.keys);
                        BTNode brotherNode = null;
                        Map<Integer, Object> brotherKey = null;

                        //找到父节点要下沉关键字和兄弟节点要上浮的关键字
                        Map<Integer, Object> downKey = null;
                        Integer nodeIndex = node.index;
                        if (nodeIndex != null){
                            if (nodeIndex == 0){
                                //节点是子节点列表里的第一个，兄弟节点则是第二个，取兄弟节点的第一个关键字
                                brotherNode = oldFatherChilds.get(nodeIndex+1);
                                downKey = oldFatherKeys.get(0);
                                if (brotherNode.keys.size() > minmumKeyNum){
                                    brotherKey = brotherNode.keys.get(0);
                                }
                            }else if (nodeIndex == oldFatherChilds.size()-1){
                                //节点是子节点列表里的最后一个，兄弟节点则是倒数第二个，取兄弟节点的最后一个关键字
                                brotherNode = oldFatherChilds.get(nodeIndex-1);
                                downKey = oldFatherKeys.get(oldFatherKeys.size()-1);
                                if (brotherNode.keys.size() > minmumKeyNum){
                                    brotherKey = brotherNode.keys.get(brotherNode.keys.size()-1);
                                }
                            }else {
                                //节点位于子节点列表中间，则需判断其左右兄弟节点的关键字数是否大于Math.celi(m/2)-1
                                //如果左节点超过则取左节点最后一个关键字，如果是右节点取右节点第一个关键字
                                BTNode prefixNode = oldFatherChilds.get(nodeIndex-1);
                                BTNode surfixNode = oldFatherChilds.get(nodeIndex+1);
                                if (prefixNode.keys.size() > minmumKeyNum){
                                    brotherNode = prefixNode;
                                    brotherKey = prefixNode.keys.get(prefixNode.keys.size()-1);
                                }else if (surfixNode.keys.size() > minmumKeyNum){
                                    brotherNode = surfixNode;
                                    brotherKey = surfixNode.keys.get(0);
                                }else {
                                    brotherNode = prefixNode;
                                }

                                if (prefixNode.keys.size() < minmumKeyNum){
                                    downKey = oldFatherKeys.get(nodeIndex-1);
                                }else {
                                    downKey = oldFatherKeys.get(nodeIndex-1);
                                }
                            }
                        }

                        //将下沉的关键字添加到节点，并从父节点删除
                        Integer tmpDownKey = downKey.keySet().toArray(new Integer[1])[0];
                        Object tmpDownValue = downKey.get(tmpDownKey);
                        node = addNode(node, tmpDownKey, tmpDownValue, false);
                        node.father = removeNode(node.father, tmpDownKey, null);

                    }

                }

                return sortKeysAndChilds(node);
            }
        }

        //节点关键字查找不到要删除的key,继续遍历子节点
        if (node.childs != null){
            int childNum = node.childs.size();
            for (int i = 0; i < childNum; i++) {
                if (i == 0){
                    //获取第一个关键字，看是否小于它，小于它则存进第一个节点
                    Integer tmpKeys[] = node.keys.get(0).keySet().toArray(new Integer[1]);
                    if (tmpKeys.length > 0 && key < tmpKeys[0]){
                        BTNode tmpNode = node.childs.get(0);
                        tmpNode = removeNode(tmpNode, key, Integer.valueOf(i));
                        break;
                    }
                }else if (i == childNum-1){
                    //获取最后一个关键字，看是否大于它
                    Integer tmpKeys[] = node.keys.get(node.keys.size()-1).keySet().toArray(new Integer[1]);
                    if (tmpKeys.length > 0 && key > tmpKeys[0]){
                        BTNode tmpNode = node.childs.get(node.childs.size()-1);
                        tmpNode = removeNode(tmpNode, key, Integer.valueOf(i));
                        break;
                    }
                }else {
                    //获取上一个关键字和i对应的关键字，看是否在二者之间
                    Integer tmpKeys1[] = node.keys.get(i-1).keySet().toArray(new Integer[1]);
                    Integer tmpKeys2[] = node.keys.get(i).keySet().toArray(new Integer[1]);
                    if (tmpKeys1.length > 0 && tmpKeys2.length > 0 && key > tmpKeys1[0] && key < tmpKeys2[0]){
                        BTNode tmpNode = node.childs.get(i);
                        tmpNode = removeNode(tmpNode, key, Integer.valueOf(i));
                        break;
                    }
                }
            }
        }

        //如果父节点关键字数为0，则合并node和他的所有兄弟节点为一个节点替换父节点
        if (node.father != null && node.father.keys.size() == 0){
            BTNode tmpNode = node.father.childs.get(0);
            tmpNode.father = node.father.father;
            node = tmpNode;
        }

        //如果自己节点关键字数为0，则取子节点替代自己
        if (node.keys.size() == 0){
            node = node.childs.get(0);
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

            for (int i = 0; i < node.childs.size(); i++) {
                BTNode tmpNode = node.childs.get(i);
                tmpNode.index = i;
            }
        }
        return node;
    }
}
