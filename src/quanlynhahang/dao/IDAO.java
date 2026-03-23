package quanlynhahang.dao;

import java.util.ArrayList;

public interface IDAO<T, K> {
    boolean insert(T obj);
    boolean update(T obj);
    boolean delete(K key);
    ArrayList<T> getAll();
    T getById(K key);
}
