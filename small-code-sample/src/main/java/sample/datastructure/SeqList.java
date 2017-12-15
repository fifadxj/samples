package sample.datastructure;

public class SeqList {
    private static final int INIT_CAPACITY = 5;
    private int size;
    private int[] values;
    
    public SeqList() {
        this(INIT_CAPACITY);
    }
    
    public SeqList(int capacity) {
        size = 0;
        values = new int[capacity];
    }
    
    public void insert(int el, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("insert:   "+ index + " is not in 0 - " + size);
        }
        
        resizeCapacity(size + 1);
        size++;

        for (int i = size - 2; i >= index; i--) {
            values[i + 1] = values[i];
        }
        values[index] = el;
    }
    
    public void append(int el) {
        insert(el, size);
    }
    
    public int delete(int index) {
        checkIndex(index);
        
        int deleted = values[index];
        for (int i = index; i <= size - 2; i++) {
            values[i] = values[i + 1];
        }
        size--;
        
        return deleted;
    }
    
    public int get(int index) {
        checkIndex(index);
        
        return values[index];
    }
    
    public void set(int el, int index) {
        checkIndex(index);
        
        values[index] = el;
    }
    
    public int size() {
        return size;
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index > size - 1) {
            throw new IndexOutOfBoundsException(index + " is not in 0 - " + (size - 1));
        }
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
        SeqList list = new SeqList();
        
        //initalize
        list.append(1);
        list.append(2);
        list.append(3);
        list.append(4);
        list.append(5);
        list.append(6);
        list.append(7);
        list.append(8);
        list.append(9);
        list.append(10);
        list.listAll();
        
        //test insert
        list.insert(666666, 5);
        list.listAll();
        
        //test delete
        list.delete(5);
        list.listAll();
        
        //test set and get
        for (int i = 0; i < list.size(); i++) {
            list.set(list.get(i) * list.get(i), i);
        }
        list.listAll();
    }
}
