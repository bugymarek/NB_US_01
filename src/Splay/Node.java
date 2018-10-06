package Splay;

public class Node<T extends Comparable<T>> {
    private Node leftSon;
    private Node rightSon;
    private Node parent;
    private T data;

    public Node() {
        this.leftSon = null;
        this.rightSon = null;
        this.parent = null;
        this.data = null;
    }

    public Node(T data) {
        this.data = data;
        this.leftSon = null;
        this.rightSon = null;
        this.parent = null;
    }

    public Node getLeftSon() {
        return leftSon;
    }

    public Node getRightSon() {
        return rightSon;
    }

    public Node getParent() {
        return parent;
    }

    public T getData() {
        return data;
    }

    public boolean isParent() {
        return this.leftSon != null || this.rightSon != null || (this.leftSon != null && this.rightSon != null);
    }

    public boolean isGrandParent() {
        return this.leftSon.isParent() || this.rightSon.isParent() || (this.leftSon.isParent() && this.rightSon.isParent());
    }

    public boolean isLeaf() {
        return !isParent();
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasGrandParent() {
        return this.parent != null && this.parent.getParent() != null;
    }

    public boolean isLeftSon() {
        if (this.hasParent() && this.parent.leftSon != null) {
            if(this.parent.leftSon.data.compareTo(this.data) == 0 ){
                return  true;
            }
        }
        return false;
    }

    public boolean isRightSon() {
        if (this.hasParent() && this.parent.rightSon != null) {
            if(this.parent.rightSon.data.compareTo(this.data) == 0 ){
                return  true;
            }
        }
        return false;
    }

    public void setLeftSon(Node leftSon) {
        this.leftSon = leftSon;
    }

    public void setRightSon(Node rightSon) {
        this.rightSon = rightSon;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setData(T data) {
        this.data = data;
    }

}
