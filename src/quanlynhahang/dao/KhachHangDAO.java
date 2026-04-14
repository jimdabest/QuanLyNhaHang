package quanlynhahang.dao;

import quanlynhahang.dto.KhachHangDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * DAO thực hiện truy vấn cho bảng KhachHang.
 * Quản lý thêm, sửa, xóa mềm và truy vấn thông tin khách hàng.
 */
public class KhachHangDAO implements IDAO<KhachHangDTO, String> {

    /**
     * Thêm mới khách hàng vào bảng KhachHang.
     * @param obj đối tượng KhachHangDTO chứa thông tin khách
     * @return true nếu thêm thành công, false nếu thất bại
     */
    @Override
    public boolean insert(KhachHangDTO obj) {
        String sql = "INSERT INTO KhachHang (maKhachHang, maHang, tenKH, soDienThoai, tongChiTieu, ngayDangKy, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaKhachHang());
            ps.setString(2, obj.getMaHang());
            ps.setString(3, obj.getTenKH());
            ps.setString(4, obj.getSoDienThoai());
            ps.setDouble(5, obj.getTongChiTieu());
            // Chuyển từ java.util.Date sang java.sql.Date để lưu vào SQL
            ps.setDate(6, new java.sql.Date(obj.getNgayDangKy().getTime()));
            ps.setBoolean(7, obj.isTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin khách hàng.
     * @param obj đối tượng KhachHangDTO chứa dữ liệu cập nhật
     * @return true nếu cập nhật thành công, false nếu lỗi
     */
    @Override
    public boolean update(KhachHangDTO obj) {
        String sql = "UPDATE KhachHang SET maHang = ?, tenKH = ?, soDienThoai = ?, tongChiTieu = ?, ngayDangKy = ?, trangThai = ? WHERE maKhachHang = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaHang());
            ps.setString(2, obj.getTenKH());
            ps.setString(3, obj.getSoDienThoai());
            ps.setDouble(4, obj.getTongChiTieu());
            ps.setDate(5, new java.sql.Date(obj.getNgayDangKy().getTime()));
            ps.setBoolean(6, obj.isTrangThai());
            ps.setString(7, obj.getMaKhachHang());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa mềm khách hàng bằng cách cập nhật trạng thái không hoạt động.
     * @param key mã khách hàng cần xóa
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean delete(String key) {
        // Thực hiện xóa mềm bằng cách cập nhật trangThai = false để giữ lịch sử hóa đơn
        String sql = "UPDATE KhachHang SET trangThai = 0 WHERE maKhachHang = ?";
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
     * Lấy danh sách khách hàng đang hoạt động.
     * @return danh sách KhachHangDTO
     */
    @Override
    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE trangThai = 1";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new KhachHangDTO(
                        rs.getString("maKhachHang"),
                        rs.getString("maHang"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("tongChiTieu"),
                        rs.getDate("ngayDangKy"),
                        rs.getBoolean("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy khách hàng theo mã hoặc số điện thoại.
     * @param key mã khách hàng hoặc số điện thoại
     * @return KhachHangDTO nếu tìm thấy, null nếu không
     */
    @Override
    public KhachHangDTO getById(String key) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ? OR soDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachHangDTO(
                        rs.getString("maKhachHang"),
                        rs.getString("maHang"),
                        rs.getString("tenKH"),
                        rs.getString("soDienThoai"),
                        rs.getDouble("tongChiTieu"),
                        rs.getDate("ngayDangKy"),
                        rs.getBoolean("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo mã khách hàng mới theo dạng KH000001.
     * @return mã khách hàng chưa tồn tại
     */
    public String taoMaKhachHangMoi() {
        for (int i = 1; i <= 999999; i++) {
            String ma = String.format("KH%06d", i);
            if (getById(ma) == null) {
                return ma;
            }
        }
        return "KH" + (System.currentTimeMillis() % 100000000);
    }

    /**
     * Tạo nhanh khách hàng mới từ tên và số điện thoại.
     * @param tenKhach tên khách hàng
     * @param soDienThoai số điện thoại khách hàng
     * @return KhachHangDTO vừa tạo nếu thành công, null nếu thất bại
     */
    public KhachHangDTO taoNhanhKhachMoi(String tenKhach, String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            return null;
        }

        KhachHangDTO daTonTai = getById(soDienThoai.trim());
        if (daTonTai != null) {
            return daTonTai;
        }

        KhachHangDTO khachMoi = new KhachHangDTO(
                taoMaKhachHangMoi(),
                "H01",
                tenKhach,
                soDienThoai.trim(),
                0,
                new Date(),
                true
        );

        return insert(khachMoi) ? khachMoi : null;
    }
}