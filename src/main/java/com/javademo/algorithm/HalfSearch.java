package com.javademo.algorithm;

import java.util.List;

public class HalfSearch {

    /**
     * 二分查账
     * 又叫折半查找，要求待查找的序列有序。每次取中间位置的值与待查关键字比较，如果中间位置
     * 的值比待查关键字大，则在前半部分循环这个查找的过程，如果中间位置的值比待查关键字小，
     * 则在后半部分循环这个查找的过程。直到查找到了为止，否则序列中没有待查的关键字
     * @param numArray
     * @param searchNum
     * @return
     */
    public static int halfSearch(List<Integer> numArray, int searchNum){

        int halfIndex = numArray.size()/2;
        Integer halfNum = numArray.get(halfIndex);

        if (halfNum < searchNum){
            List<Integer> tmpList = numArray.subList(0, halfIndex-1);
            halfSearch(tmpList, searchNum);
        }else if (halfNum > searchNum){
            if (numArray.size() > halfIndex+2){
                List<Integer> tmpList = numArray.subList(halfIndex+1, numArray.size()-1);
                halfSearch(tmpList, searchNum);
            }
        }else {
            return halfIndex;
        }

        return -1;

    }
}
