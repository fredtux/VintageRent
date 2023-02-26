package org.models;

import java.util.ArrayList;
import java.util.List;
import java.lang.Comparable;

public class ModelList <T>{
    private List<T> list;

    public ModelList(List<T> list) {
        this.list = list;
    }

    public ModelList() {
        this.list = new ArrayList<T>();
    }

    public class SortedArrayList<T> extends ArrayList<T> {
        public SortedArrayList() {
            super();
        }

        public SortedArrayList(int initialCapacity) {
            super(initialCapacity);
        }

        public SortedArrayList(List<T> list) {
            super(list);
        }

        @Override
        public boolean add(T item) {
            super.add(item);
            sort(null);
            return true;
        }

        @Override
        public void add(int index, T item) {
            super.add(index, item);
            sort(null);
        }

        @Override
        public T set(int index, T item) {
            T oldItem = super.set(index, item);
            sort(null);
            return oldItem;
        }

    }
    public ModelList(boolean isSorted) {
        if(isSorted){
            this.list = new SortedArrayList<T>();
        } else {
            this.list = new ArrayList<T>();
        }
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void add(T item) {
        list.add(item);
    }

    public void remove(T item) {
        list.remove(item);
    }

    public void remove(int index) {
        list.remove(index);
    }

    public T get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        list.clear();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(T item) {
        return list.contains(item);
    }

    public int indexOf(T item) {
        return list.indexOf(item);
    }

    public int lastIndexOf(T item) {
        return list.lastIndexOf(item);
    }

    public void add(int index, T item) {
        list.add(index, item);
    }

    public T set(int index, T item) {
        return list.set(index, item);
    }

    public void sort() {
        list.sort(null);
    }


}
