package sample.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daixiaojun
 * @date 2020/6/15 19:15
 */
class TrieNode {
    Map<Character, TrieNode> children;
    boolean wordEnd;

    public TrieNode() {
        children = new HashMap<Character, TrieNode>();
        wordEnd = false;
    }
}
