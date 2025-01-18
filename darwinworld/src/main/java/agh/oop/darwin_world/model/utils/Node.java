package agh.oop.darwin_world.model.utils;

public class Node<A>{
    protected A object;
    protected Node<A> next;
    public Node(A object) {
        this.object = object;
    }
}
