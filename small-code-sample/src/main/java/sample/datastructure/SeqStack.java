package sample.datastructure;

public class SeqStack {
    private static final int INIT_CAPACITY = 5;
    private int size;
    private int[] values;
    
    public SeqStack() {
        this(INIT_CAPACITY);
    }
    
    public SeqStack(int capacity) {
        size = 0;
        values = new int[capacity];
    }
    
    public void push(int el) {
        resizeCapacity(size + 1);
        size++;
        values[size - 1] = el;
    }
    
    public int pop() {
        int value = values[size - 1];
        values[size - 1] = 0; // gc, currently element is int, there is no need gc.
        size--;
        
        return value;
    }
    
    public int top() {
        return values[size - 1];
    }
    
    public int size() {
        return size;
    }
    
    
    private void resizeCapacity(int need) {
        if (need > values.length) {

            int[] newValues = new int[(values.length * 2) >= need ? values.length * 2 : need];

            for (int i = 0; i < size; i++) {
                newValues[i] = values[i];
            }
            
            values = newValues;
        }
    }
    
    public void listAll() {
        for (int i = 0; i < size; i++) {
            System.out.print(values[i]);
            System.out.print(" ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        SeqStack stack = new SeqStack();

        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.listAll();

        System.out.println(stack.pop());
        stack.listAll();
        
        stack.push(10);
        stack.listAll();
    }
}
