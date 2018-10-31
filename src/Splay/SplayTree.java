package Splay;

import Test.Person;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SplayTree<T extends Comparable<T>> {

    private Node<T> root;
    private int count;

    /**
     * Create splay tree with root node
     *
     * @param data
     */
    public SplayTree(T data) {
        this.root = new Node<T>(data);
        this.count++;
    }

    /**
     * Create splay tree without root node
     */
    public SplayTree() {
        this.root = null;
        this.count = 0;
    }

    public boolean insert(T data) {
        boolean result = false;
        Node<T> insertNode = new Node(data);
        if (isEmpty()) {
            root = insertNode;
            count++;
            return true;
        }

        Node<T> current = root;
        while (true) {
            if (current.getData().compareTo(insertNode.getData()) == 0) {
                result = false;
                break;
            }
            // vkladany prvok je "mensi"
            if (current.getData().compareTo(insertNode.getData()) <= -1) {
                // ak nema laveho syna tak mu ho nastav na vkladany prvok
                if (current.getLeftSon() == null) {
                    current.setLeftSon(insertNode);
                    insertNode.setParent(current);
                    current = insertNode;
                    count++;
                    result = true;
                    break;
                } else {
                    current = current.getLeftSon();
                }
            } else // ak nema praveho syna tak mu ho nastav na vkladany prvok
             if (current.getRightSon() == null) {
                    current.setRightSon(insertNode);
                    insertNode.setParent(current);
                    current = insertNode;
                    count++;
                    result = true;
                    break;
                } else {
                    current = current.getRightSon();
                }
        }
        splay(current);
        return result;
    }

    public boolean insert2(T data) {

        if (isEmpty()) {
            root = new Node(data);
            count++;
            return true;
        }
        if (root.getData().compareTo(data) == 0) {
            return false;
        } else {
            Node<T> insertNode = new Node(data);
            insertNode.setLeftSon(root);
            root.setParent(insertNode);
            insertNode.setRightSon(root.getRightSon());
            root.setRightSon(null);
            if (insertNode.getRightSon() != null) {
                insertNode.getRightSon().setParent(insertNode);
            }
            root = insertNode;
            insertNode = null;
        }
        count++;
        return true;
    }

    public T find(T data) {
        Node<T> current = root;
        if (root == null) {
            return null;
        }

        while (current.getData().compareTo(data) != 0) {
            if (current.getData().compareTo(data) <= -1) {
                if (current.getLeftSon() != null) {
                    current = current.getLeftSon();
                } else {
                    break;
                }
            } else if (current.getRightSon() != null) {
                current = current.getRightSon();
            } else {
                break;
            }
        }

        splay(current);
        if (root.getData().compareTo(data) == 0) {
            return root.getData();
        } else {
            return null;
        }
    }

    public boolean delete(T data) {

        // if is not presents return false
        T result = find(data);
        if (result == null) {
            return false;
        }
        //Split the tree into two trees Tree1 = root’s left subtree and Tree2 = root’s right subtree and delete the root node.
        Node<T> root1 = root.getLeftSon();
        Node<T> root2 = root.getRightSon();
        // hladany prvok je root
        if (root1 == null && root2 == null) {
            count--;
            root = null;
            return true;
        }
        if (root1 == null) {
            root = root2;
            root.setParent(null);
            count--;
            return true;
        }
        if (root2 == null) {
            root = root1;
            root.setParent(null);
            count--;
            return true;
        }

        //existuju dva stromy
        Node<T> findedNode = findMaxNode(root1); // Splay the maximum node (node having the maximum value) of Tree1.
        root = root1;
        count--;
        root.setParent(null);
        splay(findedNode);
        // make Root2 as the right child of Root1 and return Root1.
        root.setRightSon(root2);
        root2.setParent(root);
        return true;
    }

    private Node<T> findMaxNode(Node<T> root) {
        Node<T> current = root;
        while (current.getRightSon() != null) {
            current = current.getRightSon();
        }
        return current;
    }

    public void splay(Node<T> node) {
        while (node.getData().compareTo(root.getData()) != 0) {
            if (node.getParent().isRoot()) {// mozno null exeption ak node nema otca
                if (node.isLeftSon()) {
                    zig(node);
                } else {
                    zag(node);
                }
            } else if (node.isLeftSon() && node.getParent().isLeftSon()) {
                zigZig(node);
            } else if (node.isRightSon() && node.getParent().isRightSon()) {
                zagZag(node);
            } else if (node.isLeftSon() && node.getParent().isRightSon()) {
                zigZag(node);
            } else {
                zagZig(node);
            }
        }
    }

    private void zagZig(Node<T> node) {
        Node<T> grandParent = node.getParent().getParent();
        leftRotation(node.getParent());
        rightRotation(grandParent);
    }

    private void zigZag(Node<T> node) {
        Node<T> grandParent = node.getParent().getParent();
        rightRotation(node.getParent());
        leftRotation(grandParent);
    }

    private void zagZag(Node<T> node) {
        Node<T> grandParent = node.getParent().getParent();
        leftRotation(grandParent);
        leftRotation(node.getParent());
    }

    private void zigZig(Node<T> node) {
        Node<T> grandParent = node.getParent().getParent();
        rightRotation(grandParent);
        rightRotation(node.getParent());
    }

    private void zig(Node<T> node) {
        rightRotation(node.getParent());
    }

    private void zag(Node<T> node) {
        leftRotation(node.getParent());
    }

    private void leftRotation(Node<T> node) {
        Node<T> tmp = node.getRightSon();
        node.setRightSon(tmp.getLeftSon());
        if (node.getRightSon() != null) {
            node.getRightSon().setParent(node);
        }
        tmp.setLeftSon(node);
        setParents(node, tmp);
    }

    private void rightRotation(Node<T> node) {
        Node<T> tmp = node.getLeftSon();
        node.setLeftSon(tmp.getRightSon());
        if (node.getLeftSon() != null) {
            node.getLeftSon().setParent(node);
        }
        tmp.setRightSon(node);
        setParents(node, tmp);
    }

    private void setParents(Node<T> node, Node<T> tmp) {
        if (node.isRoot()) {
            setNewRoot(tmp);
        } else {
            if (node.isRightSon()) {
                node.getParent().setRightSon(tmp);
            } else {
                node.getParent().setLeftSon(tmp);
            }
            tmp.setParent(node.getParent());
        }
        node.setParent(tmp);
    }

    private void setNewRoot(Node<T> node) {
        this.root = node;
        this.root.setParent(null);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<T> inorder() {
        ArrayList<T> listArr = new ArrayList<>();
        Node<T> current = root;
        Stack<Node<T>> stack = new Stack<>();

        while (current != null || !stack.isEmpty()) {
            if (current == null) {
                current = stack.pop();
                listArr.add(current.getData());
                current = current.getRightSon();
            } else {
                stack.push(current);
                current = current.getLeftSon();
            }
        }
        return listArr;
    }

    public ArrayList<T> levelOrder() {
        ArrayList<T> listArr = new ArrayList<>();
        Queue<Node> level = new LinkedList<Node>();
        level.add(root);

        while (!level.isEmpty()) {
            int nodeCountInCurrentLevel = level.size(); //počet prvkov na danej urovni

            while (nodeCountInCurrentLevel > 0) {  // vloženie prvkov do pola z aktualnej urovne, pridanie prvkov do fornty z nasledujúceho levelu
                Node node = level.poll();
                if (node != null) {
                    listArr.add((T) node.getData());
                    if (node.getLeftSon() != null) {
                        level.add(node.getLeftSon());
                    }
                    if (node.getRightSon() != null) {
                        level.add(node.getRightSon());
                    }
                    nodeCountInCurrentLevel--;
                }
            }
        }
        return listArr;
    }
}
