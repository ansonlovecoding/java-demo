package com.javademo.algorithm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Array {

    /**
     * 查找符合条件的三元数组
     * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
     * 注意：答案中不可以包含重复的三元组
     * @param nums
     * @return
     */
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
}
