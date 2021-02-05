package sample.datastructure;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
        root.wordEnd = false;
    }

    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            Character c = new Character(word.charAt(i));
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            node = node.children.get(c);
        }
        node.wordEnd = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        boolean found = true;
        for (int i = 0; i < word.length(); i++) {
            Character c = new Character(word.charAt(i));
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return found && node.wordEnd;
    }

    public boolean startsWith(String prefix) {
        TrieNode node = root;
        boolean found = true;
        for (int i = 0; i < prefix.length(); i++) {
            Character c = new Character(prefix.charAt(i));
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return found;
    }

    public static List<String> subStirngs(String str) {
        List<String> strs = new ArrayList<>();

        for(int i = 0; i < str.length(); i++){
            for (int j = i+1; j<=str.length(); j++){
                String sub = str.substring(i,j);
                strs.add(sub);
                //System.out.println(sub);
            }
        }

        return strs;
    }

    public static void main(String[] args) throws Exception {
        Trie trie = new Trie();

        List<String> tags = FileUtils.readLines(new File("D:\\temp\\tag.csv"));
        System.out.println("tag总数: " + tags.size());
        for (String tag : tags) {
            trie.insert(tag);
        }

        long start = System.currentTimeMillis();

        String keyword = "发现周杰伦的新歌";
        System.out.println("输入: " + keyword);
        List<String> subkeywords = subStirngs(keyword);
        for (String subkeyword : subkeywords) {
            if (trie.search(subkeyword)) {
                System.out.println("发现tag: " + subkeyword);
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("cost: " + (end - start) + " ms");
    }
}

