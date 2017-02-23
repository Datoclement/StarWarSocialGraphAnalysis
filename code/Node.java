public class Node<E>{

    public E val;
    public Node<E> left, right;

    public Node(E e){
        this.val = e;
        this.left = null;
        this.right = null;
    }
}
