package com.javademo.algorithm;

public class AVLTree {

    //AVL树，Adelson-Velsky and Landis Tree，又称高度平衡树，具有以下特性：
    //1.左节点值小于根节点，右节点值大于根节点
    //2.每个节点的平衡因子为左子树高度减去右子树高度，每个节点的平衡因子的绝对值不能大于期望的平衡因子，一般期望平衡因子为1
    //3.插入新节点或删除节点时需要判断平衡因子绝对值是否大于期望的平衡因子，大于则需要旋转已达到每个节点的平衡因子绝对值不大于期望平衡因子的限定
    //4.查找，插入，删除某一节点的时间复杂度在log(n)以内
    //5.每一节点包含节点数值，左节点，右节点，平衡因子，高度

    //节点数值
    public int value;

    //节点高度
    public int height;

    //节点平衡因子
    public int balance;

    //左节点
    public AVLTree leftNode;

    //右节点
    public AVLTree rightNode;

    public AVLTree(int value){
        this.value = value;
        this.height = 1; //高度默认为1
        this.balance = 0; //平衡因子默认为0
        this.leftNode = null;
        this.rightNode = null;
    }

    /**
     * 添加节点
     * @param node 二叉树
     * @param value 新节点的数值
     * @return
     */
    public AVLTree addNode(AVLTree node, int value){
        //父节点为空直接生成新节点
        if (node == null){
            return new AVLTree(value);
        }

        //从父节点往下遍历插入新节点
        if (node.value > value){
            //父节点值大于新节点值，往左树遍历
            node.leftNode = addNode(node.leftNode, value);
        }else if (node.value < value){
            //父节点值大于新节点值，往右树遍历
            node.rightNode = addNode(node.rightNode, value);
        }else {
            //与父节点相等不允许插入,直接返回父节点
            return node;
        }

        //更新节点高度
        node.height = getNodeHeight(node);

        //获取平衡因子判断是否需要旋转
        node.balance = getNodeBalance(node);

        //如果需要旋转则进行旋转，并返回最终树对象
        //左子树平衡因子
        Integer leftBalance = getNodeBalance(node.leftNode);
        Integer rightBalance = getNodeBalance(node.rightNode);

        //当平衡因子大于1，如果左子树平衡因子小于0，则先对左子树进行左旋转，再对父节点进行右旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为-1
        //需要先对左子树进行左旋转，再对父节点进行右旋转,简称"LR"
        //这里可以这么理解，由于父节点的平衡因子大于1，所以它必须要右旋转，一旦右旋转，左树的高度会减一，此时如果左节点本来的平衡因子就小于0，要达到平衡则要先把左节点左旋让其左子树的高度加一
        //          8(2)
        //       /    \
        //    5(-1)    11
        //  /  \
        // 4    6
        //       \
        //        7
        if (leftBalance !=null && node.balance > 1 && leftBalance < 0){
            node.leftNode = leftRotate(node.leftNode);
            return rightRotate(node);
        }

        //当平衡因子大于1，如果左子树平衡因子大于0，则只需要对该节点进行右旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为1
        //          8(2)
        //       /    \
        //     5(1)    11
        //    /  \
        //   4    6
        //  /
        // 3
        if (leftBalance !=null && node.balance > 1 && leftBalance >= 0){
            return rightRotate(node);
        }

        //当平衡因子小于-1，如果右子树平衡因子大于0，则先对右子树进行右旋转，再对父节点进行左旋转，示意图如下图,8的平衡因子为-2，左节点5的平衡因子为1
        //需要先对右子树进行右旋转，再对父节点进行左旋转,简称"RL"
        //这里可以这么理解，由于父节点的平衡因子小于1，所以它必须要左旋转，一旦左旋转，右树的高度会减一，此时如果右节点本来的平衡因子就大于0，要达到平衡则要先把右节点右旋让其右子树的高度加一
        //          8(-2)
        //       /    \
        //     5       11(1)
        //            /  \
        //           10   12
        //          /
        //         9
        if (rightBalance !=null && node.balance < -1 && rightBalance > 0){
            node.rightNode = rightRotate(node.rightNode);
            return leftRotate(node);
        }

        //当平衡因子小于-1，如果右子树平衡因子小于0，则只需要对该节点进行左旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为-1
        //          8(-2)
        //       /    \
        //     5       11(-1)
        //            /  \
        //           10   12
        //                 \
        //                 13
        if (rightBalance !=null && node.balance < -1 && rightBalance <= 0){
            return leftRotate(node);
        }

        //本来就平衡则无需旋转直接返回
        return node;
    }

    /**
     * 删除节点
     * @param node 二叉树
     * @param value 删除的数值
     * @return
     */
    public AVLTree removeNode(AVLTree node, int value){
        //当节点为空时，高度为0
        if (node == null){
            return null;
        }

        //遍历寻找删除的节点
        AVLTree newNode = null;
        if (node.value > value){
            //当值小于父节点的值，则往左子树继续遍历
            node.leftNode = removeNode(node.leftNode, value);
            newNode = node;
        }else if (node.value < value){
            //当值大于父节点的值，则往右子树继续遍历
            node.rightNode = removeNode(node.rightNode, value);
            newNode = node;
        }else {
            //当值相等则找到删除的节点,这时候判断删除的节点左右节点是否为空
            //1.删除节点的左节点不为空，右节点为空，则取他的左节点返回
            if (node.leftNode != null && node.rightNode == null){
                AVLTree tmpLeftNode = node.leftNode;
                newNode = tmpLeftNode;
                node.leftNode = null;
            }
            //2.删除节点的左节点为空，右节点不为空，则取他的右节点返回
            if (node.leftNode == null && node.rightNode != null){
                AVLTree tmpRightNode = node.rightNode;
                newNode = tmpRightNode;
                node.rightNode = null;
            }
            //3.删除节点的左节点不为空，右节点不为空，则取他的右子树最小节点为根节点，删除节点的左节点为根节点的左节点，右子树去除替换节点作为新的根节点的右子树
            if (node.leftNode != null && node.rightNode != null){
                AVLTree minimumNode = minimum(node.rightNode);
                AVLTree tmpNode = removeNode(node.rightNode, minimumNode.value);
                minimumNode.rightNode = tmpNode;
                minimumNode.leftNode = node.leftNode;
                node.leftNode = node.rightNode = null;
                newNode = minimumNode;
            }
            //4.删除的节点左右节点都为空，则返回null
            node = null;
        }

        //查找不到删除节点返回空
        if (newNode == null){
            return null;
        }

        //更新节点高度
        newNode.height = getNodeHeight(newNode);

        //获取平衡因子判断是否需要旋转
        newNode.balance = getNodeBalance(newNode);

        //如果需要旋转则进行旋转，并返回最终树对象
        //左子树平衡因子
        Integer leftBalance = getNodeBalance(newNode.leftNode);
        Integer rightBalance = getNodeBalance(newNode.rightNode);

        //当平衡因子大于1，如果左子树平衡因子小于0，则先对左子树进行左旋转，再对父节点进行右旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为-1
        //需要先对左子树进行左旋转，再对父节点进行右旋转,简称"LR"
        //这里可以这么理解，由于父节点的平衡因子大于1，所以它必须要右旋转，一旦右旋转，左树的高度会减一，此时如果左节点本来的平衡因子就小于0，要达到平衡则要先把左节点左旋让其左子树的高度加一
        //          8(2)
        //       /    \
        //    5(-1)    11
        //  /  \
        // 4    6
        //       \
        //        7
        if (newNode.balance > 1 && leftBalance < 0){
            newNode.leftNode = leftRotate(newNode.leftNode);
            return rightRotate(newNode);
        }

        //当平衡因子大于1，如果左子树平衡因子大于0，则只需要对该节点进行右旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为1
        //          8(2)
        //       /    \
        //     5(1)    11
        //    /  \
        //   4    6
        //  /
        // 3
        if (newNode.balance > 1 && leftBalance >= 0){
            return rightRotate(newNode);
        }

        //当平衡因子小于-1，如果右子树平衡因子大于0，则先对右子树进行右旋转，再对父节点进行左旋转，示意图如下图,8的平衡因子为-2，左节点5的平衡因子为1
        //需要先对右子树进行右旋转，再对父节点进行左旋转,简称"RL"
        //这里可以这么理解，由于父节点的平衡因子小于1，所以它必须要左旋转，一旦左旋转，右树的高度会减一，此时如果右节点本来的平衡因子就大于0，要达到平衡则要先把右节点右旋让其右子树的高度加一
        //          8(-2)
        //       /    \
        //     5       11(1)
        //            /  \
        //           10   12
        //          /
        //         9
        if (newNode.balance < -1 && rightBalance > 0){
            newNode.rightNode = rightRotate(newNode.rightNode);
            return leftRotate(newNode);
        }

        //当平衡因子小于-1，如果右子树平衡因子小于0，则只需要对该节点进行左旋转，示意图如下图,8的平衡因子为2，左节点5的平衡因子为-1
        //          8(-2)
        //       /    \
        //     5       11(-1)
        //            /  \
        //           10   12
        //                 \
        //                 13
        if (newNode.balance < -1 && leftBalance < 0){
            return leftRotate(newNode);
        }

        //本来就平衡则无需旋转直接返回
        return newNode;
    }

    /**
     * 获取节点的高度
     * @param node 节点
     * @return
     */
    public int getNodeHeight(AVLTree node){
        //当节点为空时，高度为0
        if (node == null){
            return 0;
        }

        //遍历计算左子树高度，右子树高度
        int leftHeight = 0;
        int rightHeight = 0;
        if (node.leftNode != null){
            leftHeight += getNodeHeight(node.leftNode);
        }
        if (node.rightNode != null){
            rightHeight += getNodeHeight(node.rightNode);
        }

        //左右子树的最大值加一为该节点的高度
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * 获取节点的平衡因子
     * @param node 节点
     * @return
     */
    public Integer getNodeBalance(AVLTree node){
        //当节点为null时，平衡因子为空
        if (node == null){
            return null;
        }

        //左子树高度
        int leftHeight = getNodeHeight(node.leftNode);
        //右子树高度
        int rightHeight = getNodeHeight(node.rightNode);
        //平衡因子=左子树高度-右子树高度
        return leftHeight - rightHeight;
    }

    /** 左旋转
     * 下图示意对8进行左旋转,可以看出左旋转时该节点的右节点11的左节点变成它的右节点，11则变成根节点
     *          8(-2)                         11
     *        /    \                        /    \
     *     5       11(-1)      =>         8      12
     *            /  \                  /    \     \
     *           10   12               5      10    13
     *                 \
     *                 13
     * @param node 节点
     * @return
     */
    public AVLTree leftRotate(AVLTree node){
        //取右节点用于最终节点返回
        AVLTree newNode = node.rightNode;
        //获取新节点的左节点作为原根节点的右节点
        AVLTree tmpRightNode = newNode.leftNode;
        node.rightNode = tmpRightNode;
        //将原根节点赋值给新节点的左节点，完成左旋转
        newNode.leftNode = node;
        //更新旋转的两个节点的高度和平衡因子
        node.height = getNodeHeight(node);
        node.balance = getNodeBalance(node);
        newNode.height = getNodeHeight(newNode);
        newNode.balance = getNodeBalance(newNode);
        return newNode;
    }

    /** 右旋转
     * 下图示意对8进行右旋转,可以看出右旋转时该节点的左节点5的右节点变成它的左节点，5则变成根节点
     *        8(2)                        5
     *       /    \                    /    \
     *     5(1)    11      =>         4      8
     *    /  \                      /       /  \
     *   4    6                    3       6    11
     *  /
     * 3
     * @param node 节点
     * @return
     */
    public AVLTree rightRotate(AVLTree node){
        //取左节点用于最终节点返回
        AVLTree newNode = node.leftNode;
        //获取新节点的右节点作为原根节点的左节点
        AVLTree tmpLeftNode = newNode.rightNode;
        node.leftNode = tmpLeftNode;
        //将原根节点赋值给新节点的右节点，完成左旋转
        newNode.rightNode = node;
        //更新旋转的两个节点的高度和平衡因子
        node.height = getNodeHeight(node);
        node.balance = getNodeBalance(node);
        newNode.height = getNodeHeight(newNode);
        newNode.balance = getNodeBalance(newNode);
        return newNode;
    }

    /**
     * 获取二叉树最小节点
     * @param node
     * @return
     */
    public AVLTree minimum(AVLTree node){
        if (node.leftNode == null){
            return node;
        }else {
            //遍历取左节点
            return minimum(node.leftNode);
        }
    }
}
