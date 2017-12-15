package sample.datastructure;

public class Arithmetic {
    
    public static int[] mergeSort(int[] list_1, int[] list_2) {
        int[] result = new int[list_1.length + list_2.length];
        
        int i = 0, j = 0, k = 0;
        for (; i < list_1.length && j < list_2.length; k++) {
            if (list_1[i] <= list_2[j]) {
                result[k] = list_1[i]; i++;
            }
            else {
                result[k] = list_2[j]; j++; 
            }
        }
        
        while (i < list_1.length) {
            result[k++] = list_1[i++];
        }
        while (j < list_2.length) {
            result[k++] = list_2[j++];
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        int[] list_1 = new int[] {1, 33, 555, 7777, 99999};
        int[] list_2 = new int[] {2, 44, 666, 8888, 10000};
        
        int[] result = mergeSort(list_1, list_2);
        
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
    }
}
