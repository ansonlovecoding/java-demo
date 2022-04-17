package com.javademo.algorithm;

import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AlgorithmDemo {

    public static void main(String[] args) {
        //数组
        Integer[] array = {1,3,2,6,7,4,8,5};

        //快速排序
        ArrayList<Integer> tmpList = new ArrayList<Integer>(Arrays.asList(array));
        List<Integer> newList = QuickSort.quickSort(Arrays.asList(array),new ArrayList<Integer>());
        System.out.println("排序完成!");

        //二分查找
        int searchIndex = HalfSearch.halfSearch(newList, 5);
        System.out.println("5所在的位置是："+searchIndex);

        //查账三元组和为0的组合
        int[] testThreeNum = {-1,0,1,2,-1,-4};
        Array.threeNum(testThreeNum);

        //AVL树的增加和删除，请打断点查看树的每一节点变化
        int[] avlNum = {2,3,4,5,6,8,11};
        AVLTree tree = new AVLTree(avlNum[0]);
        for (int i = 1; i < avlNum.length; i++) {
            tree = tree.addNode(tree, avlNum[i]);
        }
        System.out.println(tree.value);
        tree.removeNode(tree, 3);
        System.out.println(tree.value);
    }
}
