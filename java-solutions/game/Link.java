package game;

/**
* Class for making Links to some object
*/
public final class Link<T> {
// #fields:
    private T object;
    public Link<T> next;
// fields#

// #constructors:
    public Link() {}

    public Link(T object) {
        this.object = object;
    }

    public Link(Link<T> next) {
        this.next = next;
    }
// constructors#

// #static:
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
        Link<S> save  = first.getRoot();
        Link<S> merge = second.getRoot();
        if (save == merge) {
            return true;
        }
        S so = save.getObj();
        S mo = merge.getObj();
        if (so != mo && so != null && mo != null) {
            return false;
        } else if (so == null) {
            save = Util.swap(merge, merge = save);
        }
        merge.object = null;
        merge.next = save;
        return true;
    }

    public static boolean areRootsShared(Link<?> first, Link<?> second) {
        return first.getRoot() == second.getRoot();
    }
// static#

// #public:
    public T getObj() {
        Link<T> root = getRoot();
        if (this != root && next != root) {
            while (root != next) {
                next.object = null;
                next = Util.swap(next.next, next.next = root);
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
// public#

// #implemented:
    @Override
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
// implemented#
}