package com.javademo.algorithm;

public class RedBlackTree {

//    红黑树是一种常见的自平衡二叉查找树，常用于关联数组、字典，在各种语言的底层实现中被广泛应用，Java 的 TreeMap 和 TreeSet 就是基于红黑树实现的。
//    红黑树具备以下特点：
//    1.每个节点或者是黑色，或者是红色。
//    2.根节点是黑色。
//    3.每个叶子节点是黑色。
//    4.如果一个节点是红色的，则它的子节点必须是黑色的
//    5.从任意一个节点到叶子节点，经过的黑色节点是一样的。

    public class RedBlackNode {

        //节点数值
        public int value;

        //节点总数量
        public int size;

        //节点颜色，true为红色，false为黑色
        public boolean color;

        //左节点
        public RedBlackNode leftNode;

        //右节点
        public RedBlackNode rightNode;

        public RedBlackNode(int value){
            this.value = value;
            this.color = RED;
            this.size = 1;
        }

        public RedBlackNode(int value, boolean color){
            this.value = value;
            this.color = color;
            this.size = 1;
        }
    }

    public static boolean RED = true;
    public static boolean BLACK = false;

    //LEFT表示左子树黑色节点数多，RIGHT表示右子树黑色节点数多，ZERO表示左右子树黑色节点数相等
    private enum BALANCETYPE{
        LEFT,
        RIGHT,
        ZERO
    }

    //根节点
    private RedBlackNode root;

    /**
     * 红黑树插入节点
     * @param value
     */
    public void addNode(int value){
        this.root = this.newAddNode(this.root, value);
        this.root = balanceTree(this.root);

        //根节点置为黑色
        if (this.root != null){
            this.root.color = BLACK;
        }
    }

    public void deleteNode(int value){
        this.root = this.removeNode(this.root, value);
        this.root = balanceTree(this.root);

        //根节点置为黑色
        if (this.root != null){
            this.root.color = BLACK;
        }
    }

    /**
     * 节点上添加节点
     * @param node 树
     * @param value 节点数值
     * @return
     */
    public RedBlackNode newAddNode(RedBlackNode node, int value){
        //添加节点，默认是红色
        if (node == null){
            RedBlackNode newNode = new RedBlackNode(value);
            return newNode;
        }

        //先将节点按二叉树的性质插入红黑树里面，默认为红色
        if (value > node.value){
            //往右子树遍历添加
            node.rightNode = newAddNode(node.rightNode, value);
        }else if (value < node.value){
            //往左子树遍历添加
            node.leftNode = newAddNode(node.leftNode, value);
        }else {
            //已存在该节点
            return node;
        }

        //调整树的平衡并返回
        return color(node);
    }

    public RedBlackNode removeNode(RedBlackNode node, int value){

        if (node == null){
            return null;
        }

        //遍历寻找删除的节点
        RedBlackNode newNode = null;
        if (node.value > value){
            //当值小于父节点的值，则往左子树继续遍历

            //删除的节点左右节点都为空，如果删除的节点是黑色，将其兄弟节点置为红色，已达到黑色节点数量平衡
            boolean isCheck = false;
            if (node.leftNode != null && node.leftNode.leftNode == null && node.leftNode.rightNode == null){
                if (node.leftNode.color == BLACK){
                    isCheck = true;
                }
            }

            node.leftNode = removeNode(node.leftNode, value);

            //如果不是删除节点的父节点则不需要变色
            if (node.leftNode != null){
                isCheck = false;
            }
            if (isCheck){

                //由于删除的节点是黑色，有以下三种情况需要重建平衡
                //1.父亲是红，兄弟是黑，把父亲变黑，兄弟变红
                if (isRed(node) && !isRed(node.rightNode)){
                    node = newLeftRotate(node);
                }
                //2.父亲是红，兄弟是红，父亲左旋，即父亲变成了兄弟节点的左节点
                else if (isRed(node) && isRed(node.rightNode)){
                    node = newLeftRotate(node);
                }
                //3.父亲是黑，兄弟是红，取兄弟树最小节点作为父节点，将原来的父节点作为其左节点
                else if (!isRed(node) && isRed(node.rightNode)){
                    RedBlackNode minimumNode = minimum(node.rightNode);
                    RedBlackNode tmpNode = removeNode(node.rightNode, minimumNode.value);
                    minimumNode.rightNode = tmpNode;

                    node.leftNode = node.rightNode = null;
                    minimumNode.leftNode = node;

                    //为了不打破平衡将新节点的颜色置为删除节点的颜色
                    minimumNode.color = node.color;

                    node = minimumNode;

                }
                //4.父亲是黑，兄弟是黑，先判断兄弟有无子节点，有子节点则先对做以下判断：
                //4.1.兄弟节点有右节点，但是没左节点，将右节点变黑，对兄弟节点的父节点左旋
                //4.2.兄弟节点只要有左节点，不管有没右节点，将左节点变黑，先对兄弟节点右旋，再对兄弟节点的父节点左旋
                //4.3.兄弟节点无孩子或孩子都为黑色，则兄弟节点变红色
                else if (!isRed(node) && !isRed(node.rightNode)){
                    if (node.rightNode != null && node.rightNode.rightNode != null && node.rightNode.leftNode == null){
                        node.rightNode.rightNode.color = BLACK;
                        node = newLeftRotate(node);
                    }else if (node.rightNode != null && node.rightNode.leftNode != null){
                        node.rightNode.leftNode.color = BLACK;
                        node.rightNode = newRightRotate(node.rightNode);
                        node = newLeftRotate(node);
                    }else if (node.rightNode != null && !isRed(node.rightNode.leftNode) && !isRed(node.rightNode.rightNode)){
                        node.rightNode.color = RED;
                    }
                }
            }
            newNode = node;


        }else if (node.value < value){
            //当值大于父节点的值，则往右子树继续遍历

            //删除的节点左右节点都为空，如果删除的节点是黑色，将其兄弟节点置为红色，已达到黑色节点数量平衡
            boolean isCheck = false;
            if (node.rightNode != null && node.rightNode.leftNode == null && node.rightNode.rightNode == null){
                if (node.rightNode.color == BLACK){
                    isCheck = true;
                }
            }

            node.rightNode = removeNode(node.rightNode, value);

            //如果不是删除节点的父节点则不需要变色
            if (node.rightNode != null){
                isCheck = false;
            }
            if (isCheck){

                //由于删除的节点是黑色，有以下三种情况需要重建平衡
                //1.父亲是红，兄弟是黑，把父亲变黑，兄弟变红，分两种情况：
                //1.1.兄弟有孩子的情况，对父亲右旋
                //1.2.兄弟无孩子的情况，对父亲和兄弟进行变色
                if (isRed(node) && !isRed(node.leftNode)){
                    if (hasChild(node.leftNode)){
                        node = newRightRotate(node);
                    }else {
                        node.color = BLACK;
                        if (node.leftNode != null){
                            node.leftNode.color = RED;
                        }
                    }
                }
                //2.父亲是红，兄弟是红，父亲右旋，即父亲变成了兄弟节点的右节点
                else if (isRed(node) && isRed(node.leftNode)){
                    node = newRightRotate(node);
                }
                //3.父亲是黑，兄弟是红，取兄弟树最大节点作为父节点，将原来的父节点作为其右节点
                else if (!isRed(node) && isRed(node.leftNode)){
                    RedBlackNode maxmumNode = maxmum(node.leftNode);
                    RedBlackNode tmpNode = removeNode(node.leftNode, maxmumNode.value);
                    maxmumNode.leftNode = tmpNode;

                    node.leftNode = node.rightNode = null;
                    maxmumNode.rightNode = node;

                    //为了不打破平衡将新节点的颜色置为删除节点的颜色
                    maxmumNode.color = node.color;

                    node = maxmumNode;
                }
                //4.父亲是黑，兄弟是黑，先判断兄弟有无子节点，有子节点则先对做以下判断：
                //4.1.兄弟节点有左节点，但是没右节点，将左节点变黑，对兄弟节点的父节点右旋
                //4.2.兄弟节点只要有右节点，不管有没左节点，将右节点变黑，先对兄弟节点左旋，再对兄弟节点的父节点右旋
                //4.3.兄弟节点无孩子或孩子都为黑色，则兄弟节点变红色
                else if (!isRed(node) && !isRed(node.leftNode)){
                    if (node.leftNode != null && node.leftNode.leftNode != null && node.leftNode.rightNode == null){
                        node.leftNode.leftNode.color = BLACK;
                        node = newRightRotate(node);
                    }else if (node.leftNode != null && node.leftNode.rightNode != null){
                        node.leftNode.rightNode.color = BLACK;
                        node.leftNode = newLeftRotate(node.leftNode);
                        node = newRightRotate(node);
                    }else if (node.leftNode != null && !isRed(node.leftNode.leftNode) && !isRed(node.leftNode.rightNode)){
                        node.leftNode.color = RED;
                    }
                }

            }

            newNode = node;
        }else {
            //当值相等则找到删除的节点,这时候判断删除的节点左右节点是否为空
            //1.删除节点的左节点不为空，右节点为空，则取他的左节点返回
            if (node.leftNode != null && node.rightNode == null){
                RedBlackNode tmpLeftNode = node.leftNode;
                newNode = tmpLeftNode;

                //假如删除的节点是黑色，将左节点置为黑色，填补少了的一个黑色
                if (node.color == BLACK){
                    newNode.color = BLACK;
                }

                node.leftNode = null;
            }
            //2.删除节点的左节点为空，右节点不为空，则取他的右节点返回
            if (node.leftNode == null && node.rightNode != null){
                RedBlackNode tmpRightNode = node.rightNode;
                newNode = tmpRightNode;

                //假如删除的节点是黑色，将左节点置为黑色，填补少了的一个黑色
                if (node.color == BLACK){
                    newNode.color = BLACK;
                }

                node.rightNode = null;
            }
            //3.删除节点的左节点不为空，右节点不为空，有两种情况：
            //3.1.左节点有孩子，且右节点无孩子，则取左子树最大节点为根节点，删除节点的右节点为根节点的右节点，左子树去除替换节点作为新的根节点的左子树
            //3.2.右节点有孩子，则取他的右子树最小节点为根节点，删除节点的左节点为根节点的左节点，右子树去除替换节点作为新的根节点的右子树
            //3.3.左右节点都没孩子,取右节点做父节点，左节点作为新的父节点的左节点，父节点变黑色
            if (node.leftNode != null && node.rightNode != null){
                if (hasChild(node.leftNode) && !hasChild(node.rightNode)){
                    RedBlackNode maxmumNode = maxmum(node.leftNode);
                    RedBlackNode tmpNode = removeNode(node.leftNode, maxmumNode.value);
                    maxmumNode.leftNode = tmpNode;
                    maxmumNode.rightNode = node.rightNode;

                    //为了不打破平衡将新节点的颜色置为删除节点的颜色
                    maxmumNode.color = node.color;

                    node.leftNode = node.rightNode = null;
                    newNode = maxmumNode;
                }else if (hasChild(node.rightNode)){
                    RedBlackNode minimumNode = minimum(node.rightNode);
                    RedBlackNode tmpNode = removeNode(node.rightNode, minimumNode.value);
                    minimumNode.rightNode = tmpNode;
                    minimumNode.leftNode = node.leftNode;

                    //为了不打破平衡将新节点的颜色置为删除节点的颜色
                    minimumNode.color = node.color;

                    node.leftNode = node.rightNode = null;
                    newNode = minimumNode;
                }else {
                    RedBlackNode tmpNode = node.rightNode;
                    tmpNode.color = BLACK;
                    node.leftNode.color = RED;
                    tmpNode.leftNode = node.leftNode;
                    newNode = tmpNode;
                }
            }

            //4.删除的节点左右节点都为空，则返回null

            node = null;


        }

        //查找不到删除节点返回空
        if (newNode == null){
            return null;
        }

        //重新上色
        newNode = color(newNode);
        //删除节点可能导致两边树的黑色节点数不相等，需要遍历调整平衡
//        newNode = balanceTree(newNode);
        return newNode;
    }

    /** 左旋转
     * 下图示意对8进行左旋转,可以看出左旋转时该节点的右节点11的左节点变成它的右节点，11则变成根节点
     *           8                            11
     *        /    \                        /    \
     *     5       11           =>         8      12
     *            /  \                  /    \     \
     *           10   12               5      10    13
     *                 \
     *                 13
     * @param node 节点
     * @return
     */
    public RedBlackNode newLeftRotate(RedBlackNode node){
        //取右节点用于最终节点返回
        RedBlackNode newNode = node.rightNode;
        //获取新节点的左节点作为原根节点的右节点
        RedBlackNode tmpRightNode = newNode.leftNode;
        node.rightNode = tmpRightNode;
        //将原根节点赋值给新节点的左节点，完成左旋转
        newNode.leftNode = node;
        //newnode替换了node,所以节点数量相等
        newNode.size = node.size;
        //重新计算node的节点数量
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        //将新节点返回
        return newNode;
    }

    /** 右旋转
     * 下图示意对8进行右旋转,可以看出右旋转时该节点的左节点5的右节点变成它的左节点，5则变成根节点
     *          8                        5
     *       /    \                    /    \
     *     5      11      =>         4       8
     *    /  \                      /       /  \
     *   4    6                    3       6    11
     *  /
     * 3
     * @param node 节点
     * @return
     */
    public RedBlackNode newRightRotate(RedBlackNode node){
        //取左节点用于最终节点返回
        RedBlackNode newNode = node.leftNode;
        //获取新节点的右节点作为原根节点的左节点
        RedBlackNode tmpLeftNode = newNode.rightNode;
        node.leftNode = tmpLeftNode;
        //将原根节点赋值给新节点的右节点，完成左旋转
        newNode.rightNode = node;
        //newnode替换了node,所以节点数量相等
        newNode.size = node.size;
        //重新计算node的节点数量
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        //将新节点返回
        return newNode;
    }

    /**
     * 检测节点是否红色
     * @param node 节点
     * @return
     */
    private boolean isRed(RedBlackNode node) {
        if(node == null) {
            return BLACK;
        }
        return node.color == RED;
    }

    /**
     * 获取树的节点数量
     * @param node
     * @return
     */
    private int size(RedBlackNode node) {
        if(node == null) {
            return 0;
        }
        return node.size;
    }

    /**
     * 获取二叉树最小节点
     * @param node
     * @return
     */
    public RedBlackNode minimum(RedBlackNode node){
        if (node == null){
            return null;
        }

        if (node.leftNode == null){
            return node;
        }else {
            //遍历取左节点
            return minimum(node.leftNode);
        }
    }

    /**
     * 获取二叉树最大节点
     * @param node
     * @return
     */
    public RedBlackNode maxmum(RedBlackNode node){
        if (node == null){
            return null;
        }

        if (node.rightNode == null){
            return node;
        }else {
            //遍历取左节点
            return maxmum(node.rightNode);
        }
    }

    /**
     * 重新着色
     * @param node
     * @return
     */
    private RedBlackNode color(RedBlackNode node) {
        //先调整平衡
        //右节点为红色，左节点为黑色或空节点，则父节点是黑色，先调换颜色再左转，即"右右"的情况
        if(isRed(node.rightNode) && !isRed(node.leftNode)) {
            node.rightNode.color = node.color;
//            node.rightNode.color = BLACK;
            node.color = RED;
            node = newLeftRotate(node);
        }

        //左节点为红色，左节点的左节点为红色，即左左的情况，先调换颜色再右转
        if(isRed(node.leftNode) && isRed(node.leftNode.leftNode)) {
            node.leftNode.color = node.color;
            node.color = RED;
            node = newRightRotate(node);
        }

        //左节点是红色，右节点是红色，右节点的左节点是红色，即右左的情况，把右节点右旋，再把父节点左旋，父节点置为红色，两个子节点为黑色
        //这个情形可能会导致根节点变红，所以在删除和添加节点最后都要把根节点变黑
        if (isRed(node.leftNode) && isRed(node.rightNode) && isRed(node.rightNode.leftNode)){
            node.rightNode = newRightRotate(node.rightNode);
            node = newLeftRotate(node);

            node.color = RED;
            node.leftNode.color = node.rightNode.color = BLACK;
        }

        //左节点为红，右节点为红，将父节点和子节点置为相反色
        if (isRed(node.leftNode) && isRed(node.rightNode)){
            node.color = !node.color;
            node.leftNode.color = !node.leftNode.color;
            node.rightNode.color = !node.rightNode.color;
        }

        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        return node;
    }

    /**
     * 是否有孩子
     * @param node
     * @return
     */
    private boolean hasChild(RedBlackNode node){
        if (node == null){
            return false;
        }
        if (node.leftNode == null && node.rightNode == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 节点的左右子树黑色节点数量相等则平衡，检测返回平衡情况
     * LEFT表示左子树黑色节点数多，RIGHT表示右子树黑色节点数多，ZERO表示左右子树黑色节点数相等
     * @param node
     * @return
     */
    private BALANCETYPE isBalanced(RedBlackNode node){
        if (node == null){
            return BALANCETYPE.ZERO;
        }

        if (node.leftNode == null && node.rightNode == null){
            return BALANCETYPE.ZERO;
        }

        //左子树的黑色节点数
        int leftBlackNum = 0;
        int rightBlackNum = 0;

        RedBlackNode leftNode = node.leftNode;
        RedBlackNode rightNode = node.rightNode;

        int tmpLeftBlackNum1 = 0;
        int tmpRightBlackNum1 = 0;

        while (leftNode != null){
            if (leftNode.color == BLACK){
                tmpLeftBlackNum1++;
            }
            leftNode = leftNode.leftNode;
        }
        while (rightNode != null){
            if (rightNode.color == BLACK){
                tmpRightBlackNum1++;
            }
            rightNode = rightNode.rightNode;
        }

        leftNode = node.leftNode;
        rightNode = node.rightNode;

        int tmpLeftBlackNum2 = 0;
        int tmpRightBlackNum2 = 0;

        while (leftNode != null){
            if (leftNode.color == BLACK){
                tmpLeftBlackNum2++;
            }
            leftNode = leftNode.rightNode;
        }
        while (rightNode != null){
            if (rightNode.color == BLACK){
                tmpRightBlackNum2++;
            }
            rightNode = rightNode.leftNode;
        }

        //这里之所以遍历左右子树取最大值，是因为左右子树可能会存在黑色节点数不同，而父节点的左右子树黑色节点数相同的情况
        leftBlackNum = Math.max(tmpLeftBlackNum1, tmpLeftBlackNum2);
        rightBlackNum = Math.min(tmpRightBlackNum1, tmpRightBlackNum2);

        if (leftBlackNum == rightBlackNum){
            leftBlackNum = tmpLeftBlackNum1 + tmpLeftBlackNum2;
            rightBlackNum = tmpRightBlackNum1 + tmpRightBlackNum2;
        }

        if (leftBlackNum > rightBlackNum){
            return BALANCETYPE.LEFT;
        }else if (leftBlackNum < rightBlackNum){
            return BALANCETYPE.RIGHT;
        }else {
            return BALANCETYPE.ZERO;
        }
    }

    /**
     * 遍历调整树的平衡
     * @param node
     * @return
     */
    private RedBlackNode balanceTree(RedBlackNode node){
        //判断是否平衡
        BALANCETYPE balanceType = isBalanced(node);
        while (balanceType != BALANCETYPE.ZERO){
            if (balanceType == BALANCETYPE.LEFT){

                //由于左子树的黑色数量大于右子树，所以从左子树取最大节点作为父节点
                RedBlackNode maxmumNode = maxmum(node.leftNode);
                if (maxmumNode == null){
                    break;
                }

                //新的父节点颜色跟原来的父节点保持一致
                maxmumNode.color = node.color;

                //左子树去除节点之后作为新父节点的左子树
                RedBlackNode tmpNode = removeNode(node.leftNode, maxmumNode.value);
                maxmumNode.leftNode = tmpNode;

                //新父节点右子树添加原来父节点，这样更好让右子树平衡
                maxmumNode.rightNode = node.rightNode;
                node.leftNode = node.rightNode = null;
                maxmumNode.rightNode = newAddNode(maxmumNode.rightNode, node.value);
                node = maxmumNode;
            }else if (balanceType == BALANCETYPE.RIGHT){

                //由于右子树的黑色数量大于左子树，所以从右子树取最小节点作为父节点
                RedBlackNode minimumNode = minimum(node.rightNode);
                if (minimumNode == null){
                    break;
                }

                //新的父节点颜色跟原来的父节点保持一致
                minimumNode.color = node.color;

                //右子树去除节点之后作为新父节点的右子树
                RedBlackNode tmpNode = removeNode(node.rightNode, minimumNode.value);
                minimumNode.rightNode = tmpNode;

                //新父节点左子树添加原来父节点，这样更好让左子树平衡
                minimumNode.leftNode = node.leftNode;
                node.leftNode = node.rightNode = null;
                minimumNode.leftNode = newAddNode(minimumNode.leftNode, node.value);
                node = minimumNode;
            }

            node.size = size(node.leftNode) + size(node.rightNode) + 1;

            //判断调整后是否平衡
            balanceType = isBalanced(node);
        }
        return node;
    }

    /**
     * 检查自身是否平衡
     * @return
     */
    public boolean checkBalance(){
        BALANCETYPE balancetype = this.isBalanced(this.root);
        if (balancetype == BALANCETYPE.ZERO){
            return true;
        }else {
            return false;
        }
    }

}
