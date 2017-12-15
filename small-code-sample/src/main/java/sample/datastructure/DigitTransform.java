package sample.datastructure;

import java.util.ArrayList;
import java.util.List;

public class DigitTransform {
    public static long[] decimal2binary(long decimal) {
        List<Long> binary = new ArrayList<Long>();
        long current = decimal;
        while (current != 0) {
            long bit = current % 2;
            binary.add(0, bit);
            current = current / 2;
        }
        
        long[] result = new long[binary.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = binary.get(i);
        }
        return result;
    }
    
    public static long binary2decimal(long[] binary) {
        long decimal = 0;
        for (int i = 0; i < binary.length; i++) {
            long value = (long) (binary[i] * Math.pow(2, binary.length - i - 1));
            decimal += value;
        }
        
        return  decimal;
    }
}
