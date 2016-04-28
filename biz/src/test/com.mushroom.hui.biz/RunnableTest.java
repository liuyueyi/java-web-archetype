package com.mushroom.hui.biz;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by yihui on 16/3/11.
 */
public class RunnableTest {
    private static ExecutorService executorService = new ThreadPoolExecutor(10,
            10,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(10), new DefaultThreadFactory("sms-sender"),
            new ThreadPoolExecutor.CallerRunsPolicy());

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


    private int[] sort(int[] num, int size) {
        if (size <= 1) {
            return num;
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

        return num;
    }

    public int[] merge(int[] ans, int[] sub) {
        if (ans == null) {
            return sub;
        }


        int ansSize = ans.length;
        int subSize = sub.length;
        int[] result = new int[subSize + ansSize];

        for (int i =0, ansIndex=0, subIndex=0; i < ansSize + subSize; i ++) {
            if (subIndex >= subSize) {
                result[i] = ans[ansIndex ++];
                continue;
            }

            if (ansIndex >= ansSize) {
                result[i] = sub[subIndex ++];
                continue;
            }

            if (ans[ansIndex] < sub[subIndex]) {
                result[i] = ans[ansIndex ++];
            } else {
                result[i] = sub[subIndex ++];
            }
        }
        return result;
    }


    public int[] calculate(int[] numbers, int size) {
        CompletionService<int[]> completionService = new ExecutorCompletionService<int[]>(executorService);
        if (size <= 50) {
            return this.sort(numbers, size);
        }


        // 将数组分割，50个作为一组，进行排序
        int subNum = (size - 1) / 50 + 1;
        for (int i = 0; i < subNum; i++) {
            int len = 50;
            if (i == subNum - 1) {
                len = size - 50 * i;
            }
            final int[] subNumbers = new int[len];
            System.arraycopy(numbers, i * 50 + 0, subNumbers, 0, len);

            final int finalLen = len;
            Callable<int[]> runnable = new Callable<int[]>() {
                public int[] call() throws Exception {
                    return sort(subNumbers, finalLen);
                }
            };
            completionService.submit(runnable);
        }

        int[] ans = null;

        try{
             for (int i = 0; i < subNum; i ++) {
                 // get and remove the result
                 Future<int[]> f = completionService.take();
                 int[] tmp = f.get();
                 ans = this.merge(ans, tmp);
             }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return ans;
    }

    private void print(int[] num, int size, boolean newLine) {
        for (int i = 0; i < num.length; i++) {
            System.out.print(num[i] + ",");
        }
        if (newLine) {
            System.out.println();
        }
    }

    @Test
    public void tt() {
        int size = 250;
        int[] numbers = this.genNums(size);
        int[] numbers2 = new int[size];
        System.arraycopy(numbers, 0, numbers2, 0, size);

        long start = System.nanoTime();
        this.sort(numbers, size);
        long end = System.nanoTime();
        this.print(numbers, size, true);
        System.out.println("Cost is : " + (end - start) / 1000);

        this.print(numbers2, size, true);
        start = System.nanoTime();
        int[] ans = this.calculate(numbers2, size);
        end = System.nanoTime();
        this.print(ans, size, true);
        System.out.println("cost is : " + (end - start) / 1000);
    }


    @Test
    public void test() {
        int size = 10;
        int[] numbers = this.genNums(size);
        int[] ans1 = this.sort(numbers, size);
        this.print(ans1, size, true);

        size += 5;
        int[] numbers2 = this.genNums(size);
        int[] ans2 = this.sort(numbers2, size);
        this.print(ans2, size, true);

        int[] ans = this.merge(ans1, ans2);
        this.print(ans, 25, true);
    }
}
