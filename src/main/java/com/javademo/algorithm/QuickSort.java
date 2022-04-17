package com.javademo.algorithm;

import java.util.ArrayList;
import java.util.List;

public class QuickSort {

    /**
     * 快速排序
     * 1.从数列中挑出一个元素，称为 "基准"（pivot）;
     * 2.重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
     * 3.递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序；
     * @param numArray 排序数组
     * @param newList 返回结果数组
     * @return
     */

    public static List<Integer> quickSort(List<Integer> numArray, List<Integer> newList){

        //递归执行分区操作，直至返回的二维数组存在一边为空的情况表示分区完成
        Integer pivot = numArray.get(0);
        List<List> partionList = partion(numArray, 0);

        List<Integer> lowArray = partionList.get(0);
        List<Integer> topArray = partionList.get(1);

        if (lowArray.size() == 1){
            newList.add(lowArray.get(0));
            System.out.println(lowArray.get(0));

            if (topArray.size() == 1){
                newList.add(topArray.get(0));
                System.out.println(topArray.get(0));
            }
        }

        if (lowArray.size()>1){
            quickSort(lowArray,newList);
        }
        if (topArray.size()>1){
            quickSort(topArray,newList);
        }

        return newList;


    }

    /**
     * 分区操作，返回分区数组
     * @param numArray 原数组
     * @param start 分区起始点
     * @return
     */
    public static List<List> partion(List<Integer> numArray, int start){
        Integer pivot = numArray.get(start);
        //创建两个分区数组
        List<Integer> lowArray = new ArrayList<>();
        List<Integer> topArray = new ArrayList<>();

        for (int i = start+1; i < numArray.size(); i++) {
            if (numArray.get(i) < pivot){
                lowArray.add(numArray.get(i));
            }else {
                topArray.add(numArray.get(i));
            }
        }

        //判断基准值是否最小或最大
        if (lowArray.size() == 0){
            lowArray.add(pivot);
        }else {
            if (topArray.size() == 0){
                topArray.add(pivot);
            }else {
                topArray.add(0,pivot);
            }

        }

        //返回二维数组
        List<List> lists = new ArrayList<>();
        lists.add(lowArray);
        lists.add(topArray);
        return lists;
    }
}
