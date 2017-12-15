package sample.datastructure;

public class Hanoi {
    public static void main(String[] args) throws Exception {
        Hanoi hanoi = new Hanoi();
        hanoi.run(5, "A", "C", "B");
    }

    int count = 0;
    
    public void run(int n, String from, String to, String helper) {
        if (n == 1) {
            count++; moveSingle(from, to);
        }
        else {//consider top n-1 as a single one
            run(n - 1, from, helper, to);
            count++; moveSingle(from, to);
            run(n - 1, helper, to, from);
        }
    }
    
    public void moveSingle(String from, String to) {
        System.out.println(count + "  " + from + " ---> " + to);
    }
}
