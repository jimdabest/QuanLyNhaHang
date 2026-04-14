package quanlynhahang.dao;

import java.util.ArrayList;

/**
 * Interface chung cho các lớp DAO.
 * Định nghĩa các thao tác CRUD cơ bản với kiểu dữ liệu generic.
 * @param <T> kiểu DTO
 * @param <K> kiểu khóa chính
 */
public interface IDAO<T, K> {
    /**
     * Thêm bản ghi mới vào cơ sở dữ liệu.
     * @param obj đối tượng DTO cần chèn
     * @return true nếu chèn thành công, false nếu thất bại
     */
    boolean insert(T obj);

    /**
     * Cập nhật bản ghi hiện có trong cơ sở dữ liệu.
     * @param obj đối tượng DTO chứa dữ liệu cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean update(T obj);

    /**
     * Xóa bản ghi theo khóa chính.
     * @param key khóa chính của bản ghi cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean delete(K key);

    /**
     * Lấy tất cả bản ghi của kiểu DTO.
     * @return danh sách các bản ghi
     */
    ArrayList<T> getAll();

    /**
     * Lấy bản ghi theo khóa chính.
     * @param key khóa chính của bản ghi
     * @return đối tượng DTO nếu tìm thấy, null nếu không tìm thấy
     */
    T getById(K key);
}
