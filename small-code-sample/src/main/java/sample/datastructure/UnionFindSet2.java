package sample.datastructure;

/**
 * @author daixiaojun
 * @date 2021/2/5 13:35
 */
public class UnionFindSet2 {
    int[] parent;
    int[] rank;
    int count;

    public UnionFindSet2(int n){
        parent = new int[n];
        rank = new int[n];
        for(int i = 0; i < n; i++){
            parent[i] = i;
            rank[i] = 1;
        }
        count = n;
    }

    public boolean union(int x, int y){
        int rootX = findRoot(x);
        int rootY = findRoot(y);
        if(rootX == rootY){
            return false;
        }
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            if (rank[rootX] == rank[rootY]) {
                rank[rootY]++;
            }
            parent[rootX] = rootY;
        }

        count--;
        return true;
    }

    public int findRoot(int x){
        if(x == parent[x]) {
            return x;
        }
        parent[x] = findRoot(parent[x]);
        return parent[x];
    }

    public int getCount(){
        return count;
    }
}
