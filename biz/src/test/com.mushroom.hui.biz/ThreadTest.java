package com.mushroom.hui.biz;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多线程排序的测试
 * Created by yihui on 16/3/11.
 */
public class ThreadTest {

    /**
     * 随机生成一些数
     *
     * @param size
     * @return
     */
    private int[] genNums(final int size) {
        int[] num = new int[size];

        for (int i = 0; i < size; i++) {
            num[i] = (int) (Math.random() * 1230);
        }

        return num;
    }

    private void print(int[] num, int size, boolean newLine) {
        for (int i = 0; i < num.length; i++) {
            System.out.print(num[i] + ",");
        }
        if (newLine) {
            System.out.println();
        }
    }

    private static ExecutorService executorService = new ThreadPoolExecutor(2,
            4,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(10), new DefaultThreadFactory("sms-sender"),
            new ThreadPoolExecutor.CallerRunsPolicy());


    private void sort(int[] num, int size) {
        if (size <= 1) {
            return;
        }

        int tmp;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (num[i] > num[j]) {
                    tmp = num[i];
                    num[i] = num[j];
                    num[j] = tmp;
                }
            }
        }
    }

    private List<int[]> calculate(int[] numbers, int size) {
        if (size <= 50) {
            this.sort(numbers, size);
            return Arrays.asList(numbers);
        }


        int subNum = (size - 1) / 50 + 1;
        final List<int[]> ary = new ArrayList<int[]>(subNum);
        for (int i = 0; i < subNum; i++) {
            int len = 50;
            if (i == subNum - 1) {
                len = size - 50 * i;
            }
            final int[] subNumbers = new int[len];
            for (int j = 0; j < len; j++) {
                subNumbers[j] = numbers[i * 50 + j];
            }

            Runnable runnable = new Runnable() {
                public void run() {
                    sort(subNumbers, 50);
                }
            };
            ary.add(subNumbers);
            executorService.submit(runnable);
        }
        return ary;
    }

    private int[] sort2(int[] numbers, int size) {
        List<int[]> result = this.calculate(numbers, size);

        int ans[] = new int[size];

        return ans;
    }


    @Test
    public void test() {
        int size = 59;
        int[] nums = genNums(size);
        int[] other = new int[size];
        for (int i = 0; i < size; i++) {
            other[i] = nums[i];
        }

        long start = System.currentTimeMillis();
        this.sort(other, size);
        long end = System.currentTimeMillis();
        print(other, size, true);
        System.out.println("The cost is : " + (end - start));


        start = System.currentTimeMillis();
        List<int[]> ans = this.calculate(nums, size);
        end = System.currentTimeMillis();
        for (int[] tmp : ans) {
            this.print(tmp, 50, false);
        }
        System.out.println("\n The cost is : " + (end - start));
    }

}
