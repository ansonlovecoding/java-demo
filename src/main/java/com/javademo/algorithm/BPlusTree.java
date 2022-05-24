package com.javademo.algorithm;

import java.util.*;

public class BPlusTree {

    // B+树是B树的一种变形，它保有B树的基本特征，同时又有其独特的特性：

    // 一颗m阶的B树定义如下：
    // 1）每个结点最多有m-1个关键字。
    // 2）根结点最少可以只有1个关键字。
    // 3）非根结点至少有Math.ceil(m/2)-1个关键字。
    // 4）每个结点中的关键字都按照从小到大的顺序排列，每个关键字的左子树中的所有关键字都小于它，而右子树中的所有关键字都大于它。
    // 5）所有叶子结点都位于同一层，或者说根结点到每个叶子结点的长度都相同。

    // B+树的特点：
    // 1）B+树包含2种类型的结点：内部结点（也称索引结点）和叶子结点。根结点本身即可以是内部结点，也可以是叶子结点。根结点的关键字个数最少可以只有1个。
    // 2）B+树与B树最大的不同是内部结点不保存数据，只用于索引，所有数据（或者说记录）都保存在叶子结点中。
    // 3） m阶B+树表示了内部结点最多有m-1个关键字（或者说内部结点最多有m个子树），阶数m同时限制了叶子结点最多存储m-1个记录。
    // 4）内部结点中的key都按照从小到大的顺序排列，对于内部结点中的一个key，左树中的所有key都小于它，右子树中的key都大于等于它。叶子结点中的记录也按照key的大小排列。
    // 5）每个叶子结点都存有相邻叶子结点的指针，叶子结点本身依关键字的大小自小而大顺序链接。

    //根节点
    public BPNode root;

    //阶数
    private int m;

    /**
     * 构造函数
     * @param m 阶数
     */
    public BPlusTree(int m){
        this.root = new BLNode();
        this.m = m;
    }

    public class BPNode{
        //父节点，方便回溯
        public BPNode father;

        //记录在父节点的子节点列表位置，方便取兄弟节点
        public Integer index;
    }

    //索引节点
    public class BINode extends BPNode{
        //关键字数组
        public List<Integer> keys;

        //子节点列表
        public List<BPNode> childs;

        /**
         * 构造函数
         */
        public BINode(){
            //每个结点最多有m-1个关键字,但预留多一个方便分裂
            this.keys = new ArrayList<>(m);
            //阶数代表子节点数
            this.childs = new ArrayList<>(m);
        }
    }

    //叶子节点，比索引节点在关键字上多了value，以及前进指针，少了子节点
    public class BLNode extends BPNode{
        //关键字数组
        public List<Map<Integer, Object>> keys;

        public BLNode forward;

        /**
         * 构造函数
         */
        public BLNode(){
            //每个结点最多有m-1个关键字,但预留多一个方便分裂
            this.keys = new ArrayList<>(m);
        }
    }

    /**
     * 给树添加节点
     * @param key 关键字key
     * @param value 关键字value
     */
    public void add(Integer key, Object value){
        BPNode node = this.addNode(this.root, key, value, true);
        BPNode father = node.father;
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
    private BPNode addNode(BPNode node, Integer key, Object value, boolean isDeep){

        //是否分裂父节点
        boolean hasNewFather = false;

        Map<Integer, Object> data = new HashMap<>();
        data.put(key, value);

        //树为空时生成新节点
        if (node == null){
            BLNode leaveNode = new BLNode();
            leaveNode.keys.add(data);
            node = leaveNode;
            return node;
        }

        //如果是索引节点则向下寻找叶子节点插入数据
        if (node.getClass() == BINode.class){
            BINode indexNode = (BINode) node;
            int childNum = indexNode.childs.size();
            if (isDeep){
                //向子节点遍历插入
                for (int i = 0; i < childNum; i++) {
                    if (i == 0){
                        //获取第一个关键字，看是否小于它，小于它则存进第一个节点
                        Integer tmpKey = indexNode.keys.get(0);
                        if (key < tmpKey){
                            BPNode tmpNode = indexNode.childs.get(0);
                            tmpNode = addNode(tmpNode, key, value, isDeep);
                            break;
                        }
                    }else if (i == childNum-1){
                        //获取最后一个关键字，看是否大于它
                        Integer tmpKey = indexNode.keys.get(indexNode.keys.size()-1);
                        if (key > tmpKey){
                            BPNode tmpNode = indexNode.childs.get(indexNode.childs.size()-1);
                            tmpNode = addNode(tmpNode, key, value, isDeep);
                            break;
                        }
                    }else {
                        //获取上一个关键字和i对应的关键字，看是否在二者之间
                        Integer tmpKey1 = indexNode.keys.get(i-1);
                        Integer tmpKey2 = indexNode.keys.get(i);
                        if (key > tmpKey1 && key <= tmpKey2){
                            BPNode tmpNode = indexNode.childs.get(i);
                            tmpNode = addNode(tmpNode, key, value, isDeep);
                            break;
                        }
                    }
                }
            }else {
                //插入关键字
                //当已存在相同的关键字则直接返回
                if (indexNode.keys.contains(key)){
                    return node;
                }

                //关键字最多为m-1，超过m-1需要进行分裂
                if (indexNode.keys.size() < this.m-1){
                    //未超过关键字数量
                    indexNode.keys.add(key);

                    indexNode.keys.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return  o1 > o2 ? 1 : -1;
                        }
                    });

                }else {
                    //已超过关键字数量
                    indexNode.keys.add(key);

                    indexNode.keys.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o1 > o2 ? 1 : -1;
                        }
                    });

                    //取出中间的关键字
                    int centerNum = this.m / 2;
                    List<Integer> oldData = new ArrayList<>(indexNode.keys);
                    List<BPNode> oldChilds = new ArrayList<>(indexNode.childs);
                    Integer centerKey = indexNode.keys.get(centerNum);

                    //将中间关键字的key存到父节点
                    BINode father = indexNode.father == null ? null : (BINode) indexNode.father;
                    if (father == null) {
                        father = new BINode();
                        father.keys.add(centerKey);
                        indexNode.father = father;
                        hasNewFather = true;
                    } else {
                        indexNode.father = addNode(indexNode.father, centerKey, null, false);
                    }

                    //新分裂的兄弟节点，将原来的关键字重新分配
                    indexNode.keys.remove(centerKey);
                    BINode anotherNode = new BINode();
                    anotherNode.father = indexNode.father;
                    for (int i = 0; i < oldData.size(); i++) {
                        //更新关键字
                        if (i > centerNum) {
                            anotherNode.keys.add(oldData.get(i));
                            indexNode.keys.remove(oldData.get(i));
                        }
                    }

                    //分配子节点
                    for (BPNode tmpNode : oldChilds
                    ) {
                        //更新子节点
                        Integer firstKey = null;
                        if (tmpNode.getClass() == BINode.class) {
                            firstKey = ((BINode) tmpNode).keys.get(0);
                        } else {
                            Map<Integer, Object> tmpObj = ((BLNode) tmpNode).keys.get(0);
                            Integer[] tmpKeys = tmpObj.keySet().toArray(new Integer[tmpObj.keySet().size()]);
                            firstKey = tmpKeys[0];
                        }

                        if (firstKey != null && firstKey >= centerKey) {
                            tmpNode.father = anotherNode;
                            anotherNode.childs.add(tmpNode);
                            indexNode.childs.remove(tmpNode);
                        }
                    }

                    anotherNode.father = indexNode.father;

                    //把新节点添加到父节点的子节点列表
                    if (hasNewFather) {
                        father.childs.add(indexNode);
                    }

                    int anotherIndex = father.childs.indexOf(indexNode)+1;
                    father.childs.add(anotherIndex, anotherNode);
                }
            }

            node = indexNode;

        }else {
            BLNode leaveNode = (BLNode) node;
            //当已存在相同的关键字则直接返回
            for (Map<Integer, Object> obj : leaveNode.keys
            ) {
                Integer[] tmpKeys = obj.keySet().toArray(new Integer[1]);
                if (tmpKeys[0] == key){
                    return node;
                }
            }

            //关键字最多为m-1，超过m-1需要进行分裂
            if (leaveNode.keys.size() < this.m-1){
                //未超过关键字数量
                leaveNode.keys.add(data);

                leaveNode.keys.sort(new Comparator<Map<Integer, Object>>() {
                    @Override
                    public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                        Integer[] o1Key = o1.keySet().toArray(new Integer[o1.keySet().size()]);
                        Integer[] o2Key = o2.keySet().toArray(new Integer[o2.keySet().size()]);
                        return  o1Key[0] > o2Key[0] ? 1 : -1;
                    }
                });

            }else {
                //已超过关键字数量
                leaveNode.keys.add(data);

                leaveNode.keys.sort(new Comparator<Map<Integer, Object>>() {
                    @Override
                    public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                        Integer[] o1Key = o1.keySet().toArray(new Integer[o1.keySet().size()]);
                        Integer[] o2Key = o2.keySet().toArray(new Integer[o2.keySet().size()]);
                        return  o1Key[0] > o2Key[0] ? 1 : -1;
                    }
                });

                //取出中间的关键字
                int centerNum = this.m/2;
                List<Map<Integer, Object>> oldData = new ArrayList<>(leaveNode.keys);

                Map<Integer, Object> centerData = leaveNode.keys.get(centerNum);
                Integer centerKey = centerData.keySet().toArray(new Integer[centerData.keySet().size()])[0];
                Object centerValue = centerData.get(centerKey);

                //将中间关键字的key存到父节点
                BINode father = leaveNode.father == null ? null : (BINode) leaveNode.father;
                if (father == null){
                    father = new BINode();
                    father.keys.add(centerKey);
                    leaveNode.father = father;
                    hasNewFather = true;
                }else {
                    leaveNode.father = addNode(leaveNode.father, centerKey, centerValue, false);
                }

                //新分裂的兄弟节点，将原来的关键字重新分配
                BLNode anotherNode = new BLNode();
                anotherNode.father = leaveNode.father;
                for (int i = 0; i < oldData.size(); i++) {
                    //更新关键字
                    Map<Integer, Object> tmpData = oldData.get(i);
                    if (i >= centerNum){
                        anotherNode.keys.add(tmpData);
                        leaveNode.keys.remove(tmpData);
                    }
                }

                //将叶子节点相连
                BLNode oldForward = leaveNode.forward;
                anotherNode.forward = oldForward;
                leaveNode.forward = anotherNode;

                anotherNode.father = leaveNode.father;

                //把新节点添加到父节点的子节点列表
                if (hasNewFather){
                    father.childs.add(leaveNode);
                }

                int anotherIndex = father.childs.indexOf(leaveNode)+1;
                father.childs.add(anotherIndex, anotherNode);
            }

            node = leaveNode;
        }

        return node;
    }

    /**
     * 给树删除节点
     * @param key
     */
    public void remove(Integer key){
        BPNode node = this.removeNode(this.root, key, null, true);
        BPNode father = node.father;
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
     * 删除节点
     * 通过B+树的删除操作后，索引结点中存在的key，不一定在叶子结点中存在对应的记录
     * @param node 树
     * @param key 关键字key
     * @return
     */
    private BPNode removeNode(BPNode node, Integer key, Integer childIndex, boolean isDeep){

        if (node == null){
            return null;
        }

        //非根节点最少关键字数量,注意是除以2.0，ceil是对浮点数向上取整
        Double minmumKeyNum = Math.ceil(this.m/2.0)-1;

        //遍历找到叶子节点删除其关键字
        if (node.getClass() == BLNode.class){
            //叶子节点，删除其关键字
            BLNode leaveNode = (BLNode) node;
            List<Map<Integer, Object>> oldKeys = new ArrayList<>(leaveNode.keys);

            for (int i=0; i < oldKeys.size(); i++) {
                Map<Integer, Object> obj = oldKeys.get(i);
                Integer[] tmpKeys = obj.keySet().toArray(new Integer[1]);

                if (key == tmpKeys[0]) {
                    //执行删除逻辑
                    leaveNode.keys.remove(obj);

                    //删除后关键字数量
                    int keyNum = leaveNode.keys.size();

                    //删除的是叶子节点的关键字，只要不少于Math.celi(m/2)-1个关键字，删除结束，否则有以下情况：
                    //1.如果前面或后面的兄弟节点关键字数量大于Math.celi(m/2)-1，从兄弟节点借关键字，
                    //  如果是前面的兄弟节点则将借取的key替换父节点的key,如果是后面的兄弟节点则取借取的key后面一个关键字替换父节点的key
                    //2.如果前后兄弟节点关键字数量不大于Math.celi(m/2)-1，则该叶子节点选择前后任意兄弟节点合并为一个子节点，然后删除父节点key
                    if (keyNum < minmumKeyNum && childIndex != null){
                        //父节点关键字
                        BINode father = (BINode) leaveNode.father;
                        List<Integer> tmpFatherKeys = new ArrayList<>(father.keys);
                        List<BPNode> oldFatherChilds = new ArrayList<>(father.childs);

                        //根据节点index来判断替换或者删除的父节点的关键字
                        Integer fatherKey;
                        BLNode surfixNode = null;
                        BLNode prefixNode = null;

                        if (childIndex == 0){
                            //节点是子节点列表里的第一个，操作父节点关键字列表的第一个
                            fatherKey = tmpFatherKeys.get(0);
                            surfixNode = (BLNode) oldFatherChilds.get(1);

                            if (surfixNode.keys.size() > minmumKeyNum){
                                //从兄弟节点借他的关键字的第一个
                                Map<Integer, Object> borrowKey = surfixNode.keys.get(0);
                                Map<Integer, Object> floatUpKey = surfixNode.keys.get(1);
                                //将借来的关键字插入原来的叶子节点
                                leaveNode.keys.add(borrowKey);
                                surfixNode.keys.remove(borrowKey);
                                //将借来的关键字的下一个key替换父节点的key
                                Integer tmpFloatKey = floatUpKey.keySet().toArray(new Integer[1])[0];
                                tmpFatherKeys.remove(fatherKey);
                                tmpFatherKeys.add(tmpFloatKey);
                                father.keys = tmpFatherKeys;
                                father.keys.sort(new Comparator<Integer>() {
                                    @Override
                                    public int compare(Integer o1, Integer o2) {
                                        return  o1 > o2 ? 1 : -1;
                                    }
                                });
                            }else {
                                //合并两个节点
                                leaveNode.keys.addAll(surfixNode.keys);
                                leaveNode.forward = surfixNode.forward;
                                father.keys.remove(fatherKey);
                                father.childs.remove(surfixNode);
                                leaveNode.father = removeNode(leaveNode.father, fatherKey, null, false);
                            }
                        }else if (childIndex == oldFatherChilds.size()-1){
                            //节点是子节点列表里的最后一个，操作父节点关键字列表的最后一个
                            fatherKey = tmpFatherKeys.get(tmpFatherKeys.size()-1);
                            prefixNode = (BLNode) oldFatherChilds.get(childIndex-1);

                            if (prefixNode.keys.size() > minmumKeyNum){
                                //从兄弟节点借他的关键字的最后一个
                                Map<Integer, Object> borrowKey = prefixNode.keys.get(prefixNode.keys.size()-1);
                                //将借来的关键字插入原来的叶子节点
                                leaveNode.keys.add(borrowKey);
                                prefixNode.keys.remove(borrowKey);
                                //将借来的关键字的key替换父节点的key
                                Integer tmpFloatKey = borrowKey.keySet().toArray(new Integer[1])[0];
                                tmpFatherKeys.remove(fatherKey);
                                tmpFatherKeys.add(tmpFloatKey);
                                father.keys = tmpFatherKeys;
                                father.keys.sort(new Comparator<Integer>() {
                                    @Override
                                    public int compare(Integer o1, Integer o2) {
                                        return  o1 > o2 ? 1 : -1;
                                    }
                                });
                            }else {
                                //合并两个节点
                                prefixNode.keys.addAll(leaveNode.keys);
                                prefixNode.forward = leaveNode.forward;
                                father.keys.remove(fatherKey);
                                father.childs.remove(leaveNode);
                                prefixNode.father = removeNode(prefixNode.father, fatherKey, null, false);
                                leaveNode = prefixNode;
                            }
                        }else {
                            //节点位于子节点列表中间，则需判断其左右兄弟节点的关键字数是否不超过Math.celi(m/2)-1
                            //如果右节点超过则取父节点第childIndex关键字,如果右节点没有超过则取父节点第(childIndex-1)关键字
                            prefixNode = (BLNode) oldFatherChilds.get(childIndex-1);
                            surfixNode = (BLNode) oldFatherChilds.get(childIndex+1);
                            if (surfixNode.keys.size() > minmumKeyNum){
                                fatherKey = tmpFatherKeys.get(childIndex);

                                //从兄弟节点借他的关键字的第一个
                                Map<Integer, Object> borrowKey = surfixNode.keys.get(0);
                                Map<Integer, Object> floatUpKey = surfixNode.keys.get(1);
                                //将借来的关键字插入原来的叶子节点
                                leaveNode.keys.add(borrowKey);
                                surfixNode.keys.remove(borrowKey);
                                //将借来的关键字的下一个key替换父节点的key
                                Integer tmpFloatKey = floatUpKey.keySet().toArray(new Integer[1])[0];
                                tmpFatherKeys.remove(fatherKey);
                                tmpFatherKeys.add(tmpFloatKey);
                                father.keys = tmpFatherKeys;
                                father.keys.sort(new Comparator<Integer>() {
                                    @Override
                                    public int compare(Integer o1, Integer o2) {
                                        return  o1 > o2 ? 1 : -1;
                                    }
                                });
                            }else if (prefixNode.keys.size() > minmumKeyNum){
                                fatherKey = tmpFatherKeys.get(childIndex-1);

                                //从兄弟节点借他的关键字的最后一个
                                Map<Integer, Object> borrowKey = prefixNode.keys.get(prefixNode.keys.size()-1);
                                //将借来的关键字插入原来的叶子节点
                                leaveNode.keys.add(borrowKey);
                                prefixNode.keys.remove(borrowKey);
                                //将借来的关键字的key替换父节点的key
                                Integer tmpFloatKey = borrowKey.keySet().toArray(new Integer[1])[0];
                                tmpFatherKeys.remove(fatherKey);
                                tmpFatherKeys.add(tmpFloatKey);
                                father.keys = tmpFatherKeys;
                                father.keys.sort(new Comparator<Integer>() {
                                    @Override
                                    public int compare(Integer o1, Integer o2) {
                                        return  o1 > o2 ? 1 : -1;
                                    }
                                });
                            }else {
                                //跟前面的兄弟节点合并，并删除父节点key
                                fatherKey = tmpFatherKeys.get(childIndex-1);
                                prefixNode.keys.addAll(leaveNode.keys);
                                prefixNode.forward = leaveNode.forward;
                                father.childs.remove(leaveNode);
                                prefixNode.father = removeNode(prefixNode.father, fatherKey, null, false);
                                leaveNode = prefixNode;

//                                leaveNode.keys.addAll(surfixNode.keys);
//                                leaveNode.forward = surfixNode.forward;
//                                father.keys.remove(fatherKey);
//                                father.childs.remove(surfixNode);
//                                leaveNode.father = removeNode(leaveNode.father, fatherKey, null, false);
                            }
                        }

                        //对节点的keys排序
                        if (leaveNode.keys.size() > 0){
                            leaveNode.keys.sort(new Comparator<Map<Integer, Object>>() {
                                @Override
                                public int compare(Map<Integer, Object> o1, Map<Integer, Object> o2) {
                                    Integer[] o1Key = o1.keySet().toArray(new Integer[o1.keySet().size()]);
                                    Integer[] o2Key = o2.keySet().toArray(new Integer[o2.keySet().size()]);
                                    return  o1Key[0] > o2Key[0] ? 1 : -1;
                                }
                            });

                        }

                        node = leaveNode;
                        return node;
                    }
                }
            }
        }else {
            BINode indexNode = (BINode) node;
            if (isDeep){
                //向下遍历直至叶子节点
                if (indexNode.childs != null){
                    int childNum = indexNode.childs.size();
                    for (int i = 0; i < childNum; i++) {
                        if (i == 0){
                            //获取第一个关键字，看是否小于它，小于它则存进第一个节点
                            Integer tmpKey = indexNode.keys.get(0);
                            if (key < tmpKey){
                                BPNode tmpNode = indexNode.childs.get(0);
                                tmpNode = removeNode(tmpNode, key, Integer.valueOf(i), isDeep);
                                break;
                            }
                        }else if (i == childNum-1){
                            //获取最后一个关键字，看是否大于它
                            Integer tmpKey = indexNode.keys.get(indexNode.keys.size()-1);
                            if (key >= tmpKey){
                                BPNode tmpNode = indexNode.childs.get(indexNode.childs.size()-1);
                                tmpNode = removeNode(tmpNode, key, Integer.valueOf(i), isDeep);
                                break;
                            }
                        }else {
                            //获取上一个关键字和i对应的关键字，看是否在二者之间
                            Integer tmpKey1 = indexNode.keys.get(i-1);
                            Integer tmpKey2 = indexNode.keys.get(i);
                            if (key >= tmpKey1 && key < tmpKey2){
                                BPNode tmpNode = indexNode.childs.get(i);
                                tmpNode = removeNode(tmpNode, key, Integer.valueOf(i), isDeep);
                                break;
                            }
                        }
                    }
                }
            }else {
                //删除索引节点的关键字，分为以下情况：
                //1.该节点的兄弟节点有关键字数量大于Math.celi(m/2)-1的节点，父节点的key下沉，左兄弟则取最大关键字，右兄弟则取最小关键字替换删除的key，左右兄弟对应剔除取出的关键字
                //2.该节点的左右兄弟节点关键字数量都不大于Math.celi(m/2)-1的节点，父节点key下沉与兄弟节点合并，删除父节点的key
                //3.非根节点的关键字数量不小于Math.celi(m/2)-1，根节点关键字数量不小于1
                //4.兄弟节点的子节点重新分配

                List<Integer> oldKeys = new ArrayList<>(indexNode.keys);
                List<BPNode> oldChilds = new ArrayList<>(indexNode.childs);

                //父节点关键字
                BINode father = indexNode.father == null ? null : (BINode) indexNode.father;
                List<Integer> tmpFatherKeys = father == null ? null : new ArrayList<>(father.keys);
                List<BPNode> oldFatherChilds = father == null ? null : new ArrayList<>(father.childs);

                if (indexNode.keys.contains(key)){
                    //执行删除逻辑
                    indexNode.keys.remove(key);

                    //删除后关键字数量
                    int keyNum = indexNode.keys.size();

                    if (keyNum > 0 && keyNum < minmumKeyNum){

                        //找到前后兄弟节点
                        BINode prefixNode = null;
                        BINode surfixNode = null;
                        //下沉父节点的关键字
                        Integer fatherKey = null;
                        for (int i = 0; i < oldFatherChilds.size(); i++) {
                            BPNode tmpNode = oldFatherChilds.get(i);
                            if (tmpNode == node){
                                if (i == 0){
                                    //兄弟节点为后一个
                                    surfixNode = (BINode) oldFatherChilds.get(1);
                                    //节点是子节点列表里的第一个，下沉父节点关键字列表的第一个
                                    fatherKey = tmpFatherKeys.get(0);
                                }else if (i == oldFatherChilds.size()-1){
                                    //兄弟节点为前一个
                                    prefixNode = (BINode) oldFatherChilds.get(i-1);
                                    //节点是子节点列表里的最后一个，下沉父节点关键字列表的最后一个
                                    fatherKey = tmpFatherKeys.get(tmpFatherKeys.size()-1);
                                }else {
                                    //前后兄弟节点都有
                                    surfixNode = (BINode) oldFatherChilds.get(i+1);
                                    prefixNode = (BINode) oldFatherChilds.get(i-1);
                                    //节点位于子节点列表中间，则需判断其左右兄弟节点的关键字数是否不超过Math.celi(m/2)-1
                                    //如果右节点超过则下沉父节点第childIndex关键字,如果右节点没有超过则下沉父节点第(childIndex-1)关键字
                                    if (surfixNode.keys.size() > minmumKeyNum){
                                        fatherKey = tmpFatherKeys.get(childIndex);
                                    }else {
                                        fatherKey = tmpFatherKeys.get(childIndex-1);
                                    }
                                }
                            }
                        }

                        //如果兄弟节点的key大于Math.celi(m/2)-1，将要下沉的key添加到节点，从父节点删除该key，再将兄弟节点的key上浮到父节点
                        if (prefixNode != null && prefixNode.keys.size() > minmumKeyNum){
                            Integer brotherKey = prefixNode.keys.get(prefixNode.keys.size()-1);
                            indexNode.keys.add(fatherKey);
                            father.keys.remove(fatherKey);
                            father.keys.add(brotherKey);
                            prefixNode.keys.remove(brotherKey);
                            //将前兄弟节点子节点最后一个移到自己子节点第一个
                            BPNode child = prefixNode.childs.get(prefixNode.childs.size()-1);
                            child.father = indexNode;
                            indexNode.childs.add(0, child);
                            prefixNode.childs.remove(child);
                        }else if (surfixNode != null && surfixNode.keys.size() > minmumKeyNum){
                            Integer brotherKey = surfixNode.keys.get(0);
                            indexNode.keys.add(fatherKey);
                            father.keys.remove(fatherKey);
                            father.keys.add(brotherKey);
                            surfixNode.keys.remove(brotherKey);
                            //将后兄弟节点子节点第一个移到自己子节点最后一个
                            BPNode child = surfixNode.childs.get(0);
                            child.father = indexNode;
                            indexNode.childs.add(child);
                            surfixNode.childs.remove(child);
                        }else {
                            //该节点的左右兄弟节点关键字数量都不大于Math.celi(m/2)-1的节点，父节点key下沉与兄弟节点合并，删除父节点的key
                            if (prefixNode != null){
                                //如果前兄弟节点不为空，优先与前兄弟节点合并
                                prefixNode.keys.add(fatherKey);
                                prefixNode.keys.addAll(indexNode.keys);
                                for (BPNode child:indexNode.childs
                                     ) {
                                    child.father = prefixNode;
                                    prefixNode.childs.add(child);
                                }
                                father.childs.remove(indexNode);
                                node = prefixNode;
                                node.father = removeNode(node.father, fatherKey, null, false);
                            }else if (surfixNode != null){
                                //如果前兄弟节点为空，则与后兄弟节点合并
                                indexNode.keys.add(fatherKey);
                                indexNode.keys.addAll(surfixNode.keys);
                                for (BPNode child:surfixNode.childs
                                ) {
                                    child.father = indexNode;
                                    indexNode.childs.add(child);
                                }
                                father.childs.remove(surfixNode);
                                node = indexNode;
                                node.father = removeNode(node.father, fatherKey, null, false);
                            }
                        }

                        return node;
                    }



                }

            }
        }

        //如果父节点关键字数为0，则合并node和他的所有兄弟节点为一个节点替换父节点
        if (node.getClass() == BINode.class){
            BINode indexNode = (BINode) node;
            BINode father = indexNode.father == null ? null : (BINode) indexNode.father;
            if (father != null && father.keys.size() == 0){
                BPNode tmpNode = father.childs.get(0);
                tmpNode.father = node.father.father;
                node = tmpNode;
            }

            //如果自己节点关键字数为0，则取子节点替代自己
            if (indexNode.keys.size() == 0){
                BPNode tmpNode = indexNode.childs.get(0);
                if (tmpNode != null){
                    tmpNode.father = node.father;
                }
                node = tmpNode;
            }
        }


        return node;
    }

}
