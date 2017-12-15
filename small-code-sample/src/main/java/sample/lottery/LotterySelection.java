package sample.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotterySelection {
    public static final Integer RED_BALL_POOL_SIZE = 100;
    public static final Integer BLUE_BALL_POOL_SIZE = 100;
    
    public static final Integer RED_BALL_MAX_NO = 33;
    public static final Integer BLUE_BALL_MAX_NO = 16;
    
    public static final Integer RED_BALL_COUNT = 6;
    //public static final Integer BLUE_BALL_COUNT = 1;
    
    public static void main(String[] args) {
    	LotterySelection.generateSelections(3);
    }
    
    public static void generateSelections(int groupCount) {
    	System.out.println("======================================================================");
    	
        List<Integer> redBallPool = generateRedBallPool();
        println(redBallPool);
        System.out.println();
        List<Integer> blueBallPool = generateBlueBallPool();
        println(blueBallPool);
        
        System.out.println("======================================================================");
        
        for (int i = 0; i < groupCount; i++) {
            List<Integer> redBalls = selectRedBalls(redBallPool);

            Integer blueBall = selectBlueBall(blueBallPool);

            System.out.println();
        }
    }
    
    private static List<Integer> selectRedBalls(List<Integer> redBallPool) {
        List<Integer> readBalls = new ArrayList<Integer>();

        for (int i = 0; i < RED_BALL_COUNT; i++) {
            Integer select = null;

            boolean ok = false;
            while (!ok) {
                Random r = new Random();
                int rint = r.nextInt();
                rint = generatePoolIndex(rint);
                Integer index = (Math.abs(rint) % RED_BALL_POOL_SIZE);
                select = redBallPool.get(index);
                if (!readBalls.contains(select)) {
                    ok = true;
                    System.out.println("red[" + index + "] = " + select);
                }
                
            }
            readBalls.add(select);
        }

        return readBalls;
    }
    
    private static List<Integer> generateRedBallPool() {
        List<Integer> pool = new ArrayList<Integer>();
        for (int i = 0; i < RED_BALL_POOL_SIZE; i++) {
            Random r = new Random();
            int rint = r.nextInt();
            rint = generatePoolNum(rint);
            Integer num = (Math.abs(rint) % RED_BALL_MAX_NO) + 1;
            pool.add(num);
        }
        
        return pool;
    }
    
    private static Integer selectBlueBall(List<Integer> blueBallPool) {
        Random r = new Random();
        int rint = r.nextInt();
        rint = generatePoolIndex(rint);
        Integer index = (Math.abs(rint) % BLUE_BALL_POOL_SIZE);
        Integer select = blueBallPool.get(index);
        System.out.println("blue[" + index + "] = " + select);

        return select;
    }
    
    private static List<Integer> generateBlueBallPool() {
        List<Integer> pool = new ArrayList<Integer>();
        for (int i = 0; i < BLUE_BALL_POOL_SIZE; i++) {
            Random r = new Random();
            int rint = r.nextInt();
            rint = generatePoolNum(rint);
            Integer num = (Math.abs(rint) % BLUE_BALL_MAX_NO) + 1;
            pool.add(num);
        }
        
        return pool;
    }

    private static Integer generatePoolNum(Integer i) {
        Integer result = i;

        return result;
    }
    
    private static Integer generatePoolIndex(Integer i) {
        Integer result = i;
        
        return result;
    }
    
    private static void println(List<Integer> list) {
        int lineNum = 0;
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%-5d", list.get(i));
            lineNum++;
            if (lineNum == 10) {
                lineNum = 0;
                if (i != list.size() - 1) {
                    System.out.println();
                }
            }
        }
        
        System.out.println();
    }
}
