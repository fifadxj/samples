package sample.datastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author daixiaojun
 * @date 2021/2/5 13:35
 */
public class UnionFindSet<K> {
    Map<K, K> parentMap = new HashMap<>();
    Map<K, Integer> rankMap = new HashMap<>();
    int count;

    public UnionFindSet(Iterable<K> items){
        for(K item : items) {
            parentMap.put(item, item);
            rankMap.put(item, 1);
        }
        count = parentMap.size();
    }

    public K findRoot(K key){
        K parent = parentMap.get(key);
        if(key.equals(parent)){
            return key;
        }
        K root = findRoot(parent);
        parentMap.put(key, root);

        return root;
    }

    boolean isSameSet(K x, K y)
    {
        return Objects.equals(findRoot(x), findRoot(y));
    }

    public boolean union(K x, K y){
        K rootX = findRoot(x);
        K rootY = findRoot(y);
        if(Objects.equals(rootX, rootY)){
            return false;
        }

        if(rankMap.get(rootX) > rankMap.get(rootY)){
            parentMap.put(rootY, rootX);
        }
        else{
            if(rankMap.get(rootX) == rankMap.get(rootY)){
                rankMap.put(rootY, rankMap.get(rootY) + 1);
            }
            parentMap.put(rootX, rootY);
        }

        count--;
        return true;
    }

    public int getCount(){
        return count;
    }
}
