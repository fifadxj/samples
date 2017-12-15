package sample.lottery;

public class LotteryProbability {
    public static void main(String[] args) {
        long base = c(6, 33) * c(1, 16);
        long sub;
        
        // 1: 6 + 1
        sub = c(6, 6) * c(1, 1);
        System.out.println(format(sub, base));
        
        // 2: 6 + 0
        sub = c(6, 6) * c(1, 15);
        System.out.println(format(sub, base));
        
        // 3: 5 + 1
        sub = c(5, 6) * c(1, 27) * c(1, 1);
        System.out.println(format(sub, base));
        
        // 4: 5 + 0 or 4 + 1
        sub = c(5, 6) * c(1, 27) * c(1, 15) +
              c(4, 6) * c(2, 27) * c(1, 1);
        System.out.println(format(sub, base));
        
        // 5: 4 + 0 or 3 + 1
        sub = c(4, 6) * c(2, 27) * c(1, 15) +
              c(3, 6) * c(3, 27) * c(1, 1);
        System.out.println(format(sub, base));
        
        // 6: 2 + 1 or 1 + 1 or 0 + 1
        sub = c(2, 6) * c(4, 27) * c(1, 1) + 
              c(1, 6) * c(5, 27) * c(1, 1) +
              c(6, 27) * c(1, 1);
        System.out.println(format(sub, base));
    }

    /*
     * c(m, n) = n! / m!(n - m)! = p(m, n) / p(m, m)
     */
    public static long c(long m, long n) {
        if (m < 1 || n < 1 || m > n) {
            throw new IllegalArgumentException("c(" + m + "," + n + ")");
        }

        long c = p(m, n) / p(m, m);
        return c;
    }

    /*
     * p(m, n) = n! / (n - m)!
     */
    public static long p(long m, long n) {
        if (m < 1 || n < 1 || m > n) {
            throw new IllegalArgumentException("p(" + m + "," + n + ")");
        }

        long p = 1;
        for (long i = n; i >= n - m + 1; i--) {
            p = p * i;
        }

        return p;
    }

    public static long factorial(long num) {
        if (num < 1) {
            throw new IllegalArgumentException("factorial(" + num + ")");
        } else if (num == 1) {
            return num;
        } else {
            return num * (num - 1);
        }
    }
    
    public static String format(long sub, long base) {
        long value = base / sub;
        return "1 / " + value;
        //return sub + " / " + base;
    }
}