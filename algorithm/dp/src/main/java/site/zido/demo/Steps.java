package site.zido.demo;

/**
 * 台阶问题：
 * <p>
 * 一个楼梯有10级台阶，从下往上走，每跨一步只能向上迈一级或两级台阶，一共有多少种走法
 * <p>
 * 分析：
 * <p>
 * F(x - 1)和F(x - 2) 称为F(x)的最优子结构
 * F(X) = F(x - 1) + F(x - 2)叫状态转移方程
 * F(1) = 1,F(2) = 2（两级台阶有两种走法）是问题的边界
 */
public class Steps {
    /**
     * 不带备忘录的动态规划
     * <p>
     * 时间复杂度位O(n^2)，空间复杂度为O(1)
     *
     * @param x 总台阶数
     * @return 走法
     */
    private static int step1(int x) {
        if (x == 1) {
            return 1;
        }
        if (x == 2) {
            return 2;
        }
        return step1(x - 1) + step1(x - 2);
    }

    /**
     * 带有备忘录，此处只需要前两级台阶的备忘录，所以只需要pre 和 preSecond
     * <p>
     * 时间复杂度位O(n)，空间复杂度位O(1)
     *
     * @param x 总台阶数
     * @return 走法
     */
    private static int step2(int x) {
        if (x == 1) {
            return 1;
        }
        if (x == 2) {
            return 2;
        }
        int preSecond = 1;
        int pre = 2;
        int result = 0;
        for (int i = 3; i <= x; i++) {
            result = pre + preSecond;
            preSecond = pre;
            pre = result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(step1(10));
        System.out.println(step2(10));
    }
}
