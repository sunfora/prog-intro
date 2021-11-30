package game;

/**
 * Class for making Links to some object
 */
public final class Link<T> {

    private T object;
    public Link<T> next;

    public Link() {}

    public Link(T object) {
        this.object = object;
    }

    public Link(Link<T> next) {
        this.next = next;
    }

    public T getObj() {
        
        Link<T> root = getRoot();
        
        if (this != root && next != root) {
            while (root != next) {
                next.object = null;
                Link<T> old = next;
                next = next.next;
                old.next = root;
            }
        }
        return root.object;
    }

    public Link<T> getRoot() {
        Link<T> root = this;
        while (root.next != null) {
            root = root.next;
        }
        return root;
    }

    public boolean isRoot() {
        return getRoot() == this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Link<?>) {
            Link<?> link = (Link<?>) obj;
            return link.getRoot() == this.getRoot();
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (isRoot()) {
            return super.hashCode();
        }
        return getRoot().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Link{%s} -> %s", hashCode(), getObj());
    }

   /**
    * Operation merges roots
    * true - operation successful:
    *   > if two links are pointing to the same object
    *   > if one of links is null link
    * false - operation is not successful:
    *   > if they point to two different objects
    */
    public static <S> boolean mergeRoots(Link<S> first, Link<S> second) {
        if (first == second) {
            return true;
        }
        S fo =  first.getObj();
        S so = second.getObj();
        if (fo != so && fo != null && so != null) {
            return false;
        }

        Link<S> commonRoot =  first.getRoot();
        Link<S> attachRoot = second.getRoot();

        if (commonRoot == attachRoot) {
            return true;
        }
        
        Link<S> swap;
        if (commonRoot.getObj() == null) {
            swap = commonRoot;
            commonRoot = attachRoot;
            attachRoot = swap;
        }

        commonRoot.next = null;
        attachRoot.next = commonRoot;
        attachRoot.object = null;
        return true;
    }

    public static boolean areRootsShared(Link<?> first, Link<?> second) {
        return first.getRoot() == second.getRoot();
    }
}