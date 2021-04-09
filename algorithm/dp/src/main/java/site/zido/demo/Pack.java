package site.zido.demo;

/**
 * 01背包问题
 * <p>
 * 有一个背包，可以装载重量为5KG的物品，有4个物品，求最佳装载方案使价值最大。重量和价值如下：
 * <ul>
 *     <li>
 *         物品1，重量1KG，价值3
 *     </li>
 *     <li>
 *           物品2，重量2KG，价值4
 *     </li>
 *     <li>
 *           物品3，重量3KG，价值5
 *      </li>
 *      <li>
 *           物品4，重量4KG，价值6
 *      </li>
 * </ul>
 * <p>
 * 分析：
 * <p>
 * F(W,n)表示载重为W的背包，装前n件物品的最大价值。即求F(5,4)。
 * <p>
 * 找到最大方案，即装载物品4，则占用4KG,即求F(1,3)。此时要确定 F(1,3)+ 6 与装载前三件物品F(5,3)的最大值是谁。
 * <p>
 * 即 F(5,4) = max(F(1,3) + 6),F(5,3))
 * <p>
 * 按此推导，
 * <p>
 * 状态转移方程为： F(W,n) = max(F(W - wn,n - 1) + in,F(W,n - 1))
 * <p>
 * F(0,...) = 0
 * F(...,1) = i
 */
public class Pack {
    private static class Res {
        private int weight;
        private int price;

        public Res(int weight, int price) {
            this.weight = weight;
            this.price = price;
        }
    }

    private static int maxPrice(int packWeight, Res[] resources) {
        //初始化，F(0,...) = 0，F(...,1) = i
        int[][] dp = new int[resources.length][packWeight + 1];
        for (int i = 0; i < packWeight + 1; i++) {
            dp[0][i] = (i >= resources[0].weight) ? resources[0].price : 0;
        }

        //注意这里资源数量i = 1时其实代表资源数量为2
        for (int i = 1; i < resources.length; i++) {
            //dp[i][0] = 0; //不用算，自动初始化为0
            for (int j = 1; j < packWeight + 1; j++) {
                //考虑载重量够不够，不够直接退化
                if (j >= resources[i].weight) {
                    dp[i][j] = Math.max(
                            dp[i - 1][j - resources[i].weight] + resources[i].price,
                            dp[i - 1][j]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        //[0, 3, 3, 3, 3, 3],
        //[0, 3, 4, 7, 7, 7],
        //[0, 3, 4, 7, 8, 9],
        //[0, 3, 4, 7, 8, 9]
        return dp[resources.length - 1][packWeight];
    }

    public static void main(String[] args) {
        int packWeight = 5;
        Res[] resources = new Res[]{
                new Res(1, 3),
                new Res(2, 4),
                new Res(3, 5),
                new Res(4, 6)
        };
        System.out.println(maxPrice(packWeight, resources));
    }
}
