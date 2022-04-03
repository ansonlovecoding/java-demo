package com.javademo.algorithm;

import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AlgorithmDemo {
    //快速排序
    //    1.从数列中挑出一个元素，称为 "基准"（pivot）;
    //    2.重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
    //    3.递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序；

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

    //分区操作，返回分区数组
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

    //二分查账
//    又叫折半查找，要求待查找的序列有序。每次取中间位置的值与待查关键字比较，如果中间位置
//    的值比待查关键字大，则在前半部分循环这个查找的过程，如果中间位置的值比待查关键字小，
//    则在后半部分循环这个查找的过程。直到查找到了为止，否则序列中没有待查的关键字
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

//    给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
//    注意：答案中不可以包含重复的三元组
    public static List<List<Integer>> threeNum(int[] nums){
        //对参数合法性做判断
        if (nums == null || nums.length < 3){
            return null;
        }

        //先对数组排序
        Arrays.sort(nums);

        List<List<Integer>> threeNumList = new LinkedList<>();

        //a+b+c=0,所以得出 a+b=-c，遍历数组取c，取左右双指针代表a和b,如果c不为负数则终止
        //由于设立了双指针，所以i最大为数组长度减二
        for (int i = 0; i < nums.length-2; i++) {
            int c = nums[i];
            if (c > 0){
                break;
            }

            //跳过重复
            if (i > 0 && nums[i-1] == c){
                continue;
            }

            //设立指针坐标
            int left = i+1;
            int right = nums.length-1;

            while (left < right){

                int leftNum = nums[left];
                int rightNum = nums[right];

                if (leftNum + rightNum + c == 0){
                    //存进列表，继续下一组指针
                    List<Integer> tmpList = new LinkedList<>();
                    tmpList.add(c);
                    tmpList.add(leftNum);
                    tmpList.add(rightNum);
                    threeNumList.add(tmpList);
                    System.out.println("三元数组："+tmpList.toString());
                    //左指针右移，右指针左移
                    left++;
                    right--;
                    //过滤重复
                    while (left < right && nums[left] == nums[left-1]){
                        left++;
                    }
                    while (left < right && nums[right] == nums[right+1]){
                        right--;
                    }
                }else if (leftNum + rightNum + c > 0){
                    //右指针数值过大，需往左移，左指针不变
                    right--;
                    //过滤重复
                    while (left < right && nums[right] == nums[right+1]){
                        right--;
                    }
                }else {
                    //左指针数值过小，需往右移，右指针不变
                    left++;
                    //过滤重复
                    while (left < right && nums[left] == nums[left-1]){
                        left++;
                    }
                }
            }
        }

        //返回数组
        return threeNumList;
    }

    public static void main(String[] args) {
        //数组
        Integer[] array = {1,3,2,6,7,4,8,5};

//        //快速排序
//        ArrayList<Integer> tmpList = new ArrayList<Integer>(Arrays.asList(array));
//        List<Integer> newList = quickSort(Arrays.asList(array),new ArrayList<Integer>());
//        System.out.println("排序完成!");
//
//        //二分查找
//        int searchIndex = halfSearch(newList, 0);
//        System.out.println("5所在的位置是："+searchIndex);

        //查账三元组和为0的组合
        int[] testThreeNum = {-1,0,1,2,-1,-4};
        threeNum(testThreeNum);

    }
}
