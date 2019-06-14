package com.kodilla.model.tree;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T date = null;
    private Node<T> parent = null;
    private List<Node<T>> children = new ArrayList<>();

    public Node(T date) {
        this.date = date;
    }

    public Node(Node<T> node){
        this.date = node.date;
    }

    public void addChild(Node<T> date) {
        children.add(date);
        date.setParent(this);
    }

    public boolean isLeafNode() {
        return children.isEmpty();
    }

    public void setChildren(List<Node<T>> children) {
        for (Node<T> child : children) {
            addChild(child);
        }
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public T getDate() {
        return date;
    }

    public Node<T> getParent() {
        return parent;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public boolean isRoot(){
        return parent == null;
    }

}
