package org.itstack.demo.design.lang;
// 集合功能接⼝定
public interface Collection<E, L> extends Iterable<E> {

    boolean add(E e);

    boolean remove(E e);

    boolean addLink(String key, L l);

    boolean removeLink(String key);

    Iterator<E> iterator();

}
