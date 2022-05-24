package com.javademo.algorithm;

public class AlgorithmDemo {

    public static void main(String[] args) {
//        //数组
//        Integer[] array = {1,3,2,6,7,4,8,5};
//
//        //快速排序
//        ArrayList<Integer> tmpList = new ArrayList<Integer>(Arrays.asList(array));
//        List<Integer> newList = QuickSort.quickSort(Arrays.asList(array),new ArrayList<Integer>());
//        System.out.println("排序完成!");
//
//        //二分查找
//        int searchIndex = HalfSearch.halfSearch(newList, 5);
//        System.out.println("5所在的位置是："+searchIndex);
//
//        //查账三元组和为0的组合
//        int[] testThreeNum = {-1,0,1,2,-1,-4};
//        Array.threeNum(testThreeNum);
//
//        //AVL树的增加和删除，请打断点查看树的每一节点变化
//        int[] avlNum = {2,3,4,5,6,8,11};
//        AVLTree tree = new AVLTree(avlNum[0]);
//        for (int i = 1; i < avlNum.length; i++) {
//            tree = tree.addNode(tree, avlNum[i]);
//        }
//        System.out.println(tree.value);
//        tree.removeNode(tree, 3);
//        System.out.println(tree.value);

//        //红黑树的增加和删除，请打断点查看树的每一节点变化
//        int[] rbtNum = {6,14,12,17,18,33,37,42,48,50,88,66,24,54,22,10,4};
//        System.out.println("数组大小："+rbtNum.length);
//        RedBlackTree rbTree = new RedBlackTree();
//        for (int i = 0; i < rbtNum.length; i++) {
//            if (rbtNum[i] == 22){
//                System.out.println("断点");
//            }
//            rbTree.addNode(rbtNum[i]);
//            if (rbTree.checkBalance()){
//                System.out.println("平衡！");
//            }else {
//                System.out.println("不平衡！");
//            }
//        }
//
//        for (int i = 0; i < rbtNum.length; i++) {
//            if (rbtNum[i] == 12){
//                System.out.println("断点");
//            }
//            rbTree.deleteNode(rbtNum[i]);
//            if (rbTree.checkBalance()){
//                System.out.println("平衡！");
//            }else {
//                System.out.println("不平衡！");
//            }
//        }

//        //B树的增加和删除
//        int[] bTreeNum = {39,22,97,41,53,13,21,40,30,27,33,36,35,34,24,29,26,17,28,29,31,32,23};
//        BTree bTree = new BTree(5);
//        for (int i = 0; i < bTreeNum.length; i++) {
//            if (i == 16){
//                System.out.println("断点！");
//            }
//            bTree.add(bTreeNum[i], "data-"+bTreeNum[i]);
//            System.out.println(bTree);
//        }
//        System.out.println(bTree);
//        for (int i = 0; i < bTreeNum.length; i++) {
//            if (i == 17){
//                System.out.println("断点！");
//            }
//            bTree.remove(bTreeNum[i]);
//            System.out.println(bTree);
//        }
//        System.out.println(bTree);

        //B+树的增加和删除
        int[] bPlusTreeNum = {5,8,10,15,16,17,18,19,20,21,22,6,9,7};
        BPlusTree bPlusTree = new BPlusTree(5);
        for (int i = 0; i < bPlusTreeNum.length; i++) {
            if (i == 13){
                System.out.println("断点！");
            }
            bPlusTree.add(bPlusTreeNum[i], "data-"+bPlusTreeNum[i]);
            System.out.println(bPlusTree);
        }
        System.out.println(bPlusTree);
        int[] bPlusDeleteNum = {22,15,7};
        for (int i = 0; i < bPlusDeleteNum.length; i++) {
            if (i == 17){
                System.out.println("断点！");
            }
            bPlusTree.remove(bPlusDeleteNum[i]);
            System.out.println(bPlusTree);
        }
        System.out.println(bPlusTree);

//        //跳跃表,需要大量数据才能测试出索引的分布
//        double[] skipListScores = new double[]{1,3,5,4,2};
//        String[] skipListObjs = new String[]{"作者","一个","帅哥","大","是"};
//        SkipList skipList = new SkipList();
//        for (int i = 0; i < skipListScores.length; i++) {
//            SkipList.SkipListNode node = skipList.insert(skipListScores[i], skipListObjs[i]);
//        }
//        System.out.println(skipList.nodeLength);
//        for (int i = 0; i < skipListScores.length; i++) {
//            boolean result = skipList.deleteNode(skipListScores[i], skipListObjs[i]);
//        }
//        System.out.println(skipList.nodeLength);
    }
}
