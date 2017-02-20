public class Pennant<E>{

    private Node<E> root;

    public Pennant(Node<E> n){
        this.root = n;
    }

    public static Pennant merge(Pennant p1, Pennant p2){

        p1.root.right = p2.root.left;
        p2.root.left = p1.root;
        return Pennant(p2.root);
    }

    public static Pennant split(Pennant p){

        Pennant np = new Pennant(p.root.left);
        p.root.left = np.root.right;
        np.root.right = null;
        return np;
    }
}
