package sample.datastructure;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {
    public static void main(String[] args) throws Exception {
//        String uri = "C:\\Documents and Settings\\daixiaoj\\Desktop\\wsdl\\sod.xml";
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder= factory.newDocumentBuilder();
//        Document doc = builder.parse(uri);
//        breadthFirst(doc.getDocumentElement());
        
        TreeNode sod = new TreeNode("sod");
        TreeNode symbol = new TreeNode("symbol");
        TreeNode logoUrl = new TreeNode("logoUrl");
        TreeNode siteUrl = new TreeNode("siteUrl");
        TreeNode companyName = new TreeNode("companyName");
        TreeNode welcomeMessage = new TreeNode("welcomeMessage");
        TreeNode docs = new TreeNode("docs");
        sod.children.add(symbol);
        sod.children.add(logoUrl);
        sod.children.add(siteUrl);
        sod.children.add(companyName);
        sod.children.add(welcomeMessage);
        sod.children.add(docs);
        
        TreeNode doc = new TreeNode("doc");
        docs.children.add(doc);
        
        TreeNode index = new TreeNode("index");
        TreeNode title = new TreeNode("title");
        TreeNode description = new TreeNode("description");
        TreeNode originalFileName = new TreeNode("sourceLocation");
        TreeNode sourceLocation = new TreeNode("originalFileName");
        TreeNode pictureLocation = new TreeNode("pictureLocation");
        TreeNode allowSave = new TreeNode("allowSave");
        doc.children.add(index);
        doc.children.add(title);
        doc.children.add(description);
        doc.children.add(originalFileName);
        doc.children.add(sourceLocation);
        doc.children.add(pictureLocation);
        doc.children.add(allowSave);
        
        TreeNode doc2 = new TreeNode("doc");
        docs.children.add(doc2);
        
        TreeNode index2 = new TreeNode("index");
        TreeNode title2 = new TreeNode("title");
        TreeNode description2 = new TreeNode("description");
        TreeNode originalFileName2 = new TreeNode("sourceLocation");
        TreeNode sourceLocation2 = new TreeNode("originalFileName");
        TreeNode pictureLocation2 = new TreeNode("pictureLocation");
        TreeNode allowSave2 = new TreeNode("allowSave");
        doc2.children.add(index2);
        doc2.children.add(title2);
        doc2.children.add(description2);
        doc2.children.add(originalFileName2);
        doc2.children.add(sourceLocation2);
        doc2.children.add(pictureLocation2);
        doc2.children.add(allowSave2);
        
        //breadthFirst(sod);
        deepthFirst(sod, 1);
    }

    private static void access(TreeNode node, int level) {
        for (int i = 0; i < level * 2; i++) {
            System.out.print("-");
        }
        System.out.println(node.name);
    }
    
    private static void access(TreeNode node) {
        System.out.println(node.name);
    }
    
    public static void deepthFirst(TreeNode node, int level) {
        access(node, level++);
        for (int i = 0; i < node.children.size(); i++) {
            deepthFirst(node.children.get(i), level);
        }
    }
    
    public static void breadthFirst(TreeNode node) {
        TreeNodeQueue queue = new TreeNodeQueue();
        queue.enqueue(node);

        int level = 1;
        while (queue.size() > 0) {
            TreeNode n = queue.dequeue();
            access(n);

            List<TreeNode> children = n.children;
            boolean first = true;
            for (int i = 0; i < children.size(); i++) {
                queue.enqueue(children.get(i));
            }
        }
    }
}

class TreeNode {
    public TreeNode(String name) {
        this.name = name;
    }
    String name;
    List<TreeNode> children = new ArrayList<TreeNode>();
}

class TreeNodeQueue {
    private static final int INIT_CAPACITY = 100;

    private int size;

    private TreeNode[] values;

    public TreeNodeQueue() {
        this(INIT_CAPACITY);
    }

    public TreeNodeQueue(int capacity) {
        size = 0;
        values = new TreeNode[capacity];
    }

    public void enqueue(TreeNode el) {
        resizeCapacity(size + 1);
        size++;

        for (int i = size - 2; i >= 0; i--) {
            values[i + 1] = values[i];
        }
        values[0] = el;
    }

    public TreeNode dequeue() {
        TreeNode value = values[size - 1];
        values[size - 1] = null; // gc
        size--;

        return value;
    }

    public TreeNode top() {
        return values[size - 1];
    }

    public int size() {
        return size;
    }

    private void resizeCapacity(int need) {
        if (need > values.length) {

            TreeNode[] newValues = new TreeNode[(values.length * 2) >= need ? values.length * 2 : need];

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
}

