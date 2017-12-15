package sample.datastructure;

public class SeqQueue {
    private static final int INIT_CAPACITY = 5;
    private int size;
    private int[] values;
    
    public SeqQueue() {
        this(INIT_CAPACITY);
    }
    
    public SeqQueue(int capacity) {
        size = 0;
        values = new int[capacity];
    }
    
    public void enqueue(int el) {
        resizeCapacity(size + 1);
        size++;

        for (int i = size - 2; i >= 0; i--) {
            values[i + 1] = values[i];
        }
        values[0] = el;
    }
    
    public int dequeue() {
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
        SeqQueue queue = new SeqQueue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.listAll();
        
        System.out.println(queue.dequeue());
        queue.listAll();
        
        queue.enqueue(10);
        queue.listAll();
    }
}
