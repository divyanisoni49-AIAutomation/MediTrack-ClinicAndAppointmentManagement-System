package com.airtribe.meditrack.util;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataStore<T> {


    private final Map<String, T> store;
    private final List<T> orderedList;

    public DataStore() {
        this.store = new HashMap<>();
        this.orderedList = new ArrayList<>();
    }


    public void add(String id, T entity) {
        if (!store.containsKey(id)) {
            orderedList.add(entity);
        }
        store.put(id, entity);
    }


    public T getById(String id) {
        return store.get(id);
    }


    public boolean remove(String id) {
        T entity = store.remove(id);
        if (entity != null) {
            orderedList.remove(entity);
            return true;
        }
        return false;
    }


    public boolean update(String id, T entity) {
        if (store.containsKey(id)) {
            store.put(id, entity);
            int idx = orderedList.indexOf(store.get(id));
            if (idx >= 0) orderedList.set(idx, entity);
            return true;
        }
        return false;
    }


    public List<T> getAll() {
        return Collections.unmodifiableList(orderedList);
    }


    public List<T> filter(Predicate<T> predicate) {
        return orderedList.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }


    public List<T> getSorted(Comparator<T> comparator) {
        return orderedList.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }


    public boolean exists(String id) {
        return store.containsKey(id);
    }


    public int size() { return store.size(); }


    public boolean isEmpty() { return store.isEmpty(); }


    public void clear() {
        store.clear();
        orderedList.clear();
    }


    public Iterator<T> iterator() {
        return orderedList.iterator();
    }

    @Override
    public String toString() {
        return "DataStore{size=" + store.size() + "}";
    }
}
