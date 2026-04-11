package quanlynhahang.dao;

import quanlynhahang.dto.MonAnDTO;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO quản lý truy vấn cho bảng MonAn.
 * Thực hiện thêm, cập nhật, ngừng phục vụ và truy vấn thực đơn.
 */
public class MonAnDAO implements IDAO<MonAnDTO, String> {

    /**
     * Thêm món ăn mới vào bảng MonAn.
     * @param obj đối tượng MonAnDTO chứa dữ liệu món
     * @return true nếu thêm thành công, false nếu thất bại
     */
    @Override
    public boolean insert(MonAnDTO obj) {
        String sql = "INSERT INTO MonAn (maMon, tenMon, phanLoai, giaHienTai, trangThaiPhucVu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaMon());
            ps.setString(2, obj.getTenMon());
            ps.setString(3, obj.getPhanLoai());
            ps.setDouble(4, obj.getGiaHienTai());
            ps.setBoolean(5, obj.isTrangThaiPhucVu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin món ăn.
     * @param obj đối tượng MonAnDTO chứa dữ liệu mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean update(MonAnDTO obj) {
        String sql = "UPDATE MonAn SET tenMon = ?, phanLoai = ?, giaHienTai = ?, trangThaiPhucVu = ? WHERE maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getTenMon());
            ps.setString(2, obj.getPhanLoai());
            ps.setDouble(3, obj.getGiaHienTai());
            ps.setBoolean(4, obj.isTrangThaiPhucVu());
            ps.setString(5, obj.getMaMon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Ngừng phục vụ món ăn bằng cách cập nhật trạng thái.
     * Không xóa vật lý để tránh lỗi khóa ngoại với ChiTietHoaDon.
     * @param key mã món cần ngừng phục vụ
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean delete(String key) {
        // Nguyên tắc: Không xóa món ăn khỏi DB để tránh lỗi khóa ngoại với ChiTietHoaDon
        // Thực hiện cập nhật trạng thái ngừng phục vụ
        String sql = "UPDATE MonAn SET trangThaiPhucVu = 0 WHERE maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách tất cả món ăn.
     * @return danh sách MonAnDTO
     */
    @Override
    public ArrayList<MonAnDTO> getAll() {
        ArrayList<MonAnDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM MonAn";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new MonAnDTO(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("phanLoai"),
                        rs.getDouble("giaHienTai"),
                        rs.getBoolean("trangThaiPhucVu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy món ăn theo mã món.
     * @param key mã món cần tìm
     * @return MonAnDTO nếu tìm thấy, null nếu không tồn tại
     */
    @Override
    public MonAnDTO getById(String key) {
        String sql = "SELECT * FROM MonAn WHERE maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MonAnDTO(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("phanLoai"),
                        rs.getDouble("giaHienTai"),
                        rs.getBoolean("trangThaiPhucVu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Ngừng phục vụ món ăn bằng cách cập nhật cột TrangThaiPhucVu.
     * @param maMon mã món cần chuyển sang trạng thái ngừng phục vụ
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean xoaMem(String maMon) {
        String sql = "UPDATE MonAn SET TrangThaiPhucVu = 0 WHERE MaMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Hàm bổ sung: Lấy danh sách món theo phân loại (Khai vị, Món chính, Nước uống...)
     * Rất hữu ích cho việc lọc món trên giao diện bán hàng
     
    public ArrayList<MonAnDTO> getByPhanLoai(String loai) {
        ArrayList<MonAnDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM MonAn WHERE phanLoai = ? AND trangThaiPhucVu = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new MonAnDTO(
                        rs.getString("maMon"),
                        rs.getString("tenMon"),
                        rs.getString("phanLoai"),
                        rs.getDouble("giaHienTai"),
                        rs.getBoolean("trangThaiPhucVu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tận dụng View v_ThucDonHienTai để lấy danh sách món đang kinh doanh.
     * Giúp tầng GUI hiển thị Menu gọn gàng, không bao gồm các món đã ngừng bán.
     */
    public ArrayList<MonAnDTO> getThucDonDangPhucVu() {
        ArrayList<MonAnDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM v_ThucDonHienTai"; // Gọi trực tiếp từ View
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // View này trả về: MaMon, TenMon, PhanLoai, GiaHienTai
                // Ta set trangThaiPhucVu mặc định là true vì View đã lọc sẵn
                list.add(new MonAnDTO(
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getString("PhanLoai"),
                        rs.getDouble("GiaHienTai"),
                        true
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}