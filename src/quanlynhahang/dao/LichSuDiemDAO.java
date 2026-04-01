package quanlynhahang.dao;

import quanlynhahang.dto.LichSuDiemDTO;
import java.sql.*;
import java.util.ArrayList;

public class LichSuDiemDAO implements IDAO<LichSuDiemDTO, String> {

    @Override
    public boolean insert(LichSuDiemDTO obj) {
        String sql = "INSERT INTO LichSuDiem (maGiaoDich, maKhachHang, maHoaDon, loaiGiaoDich, soDiemThayDoi, thoiGianGiaoDich, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaGiaoDich());
            ps.setString(2, obj.getMaKhachHang());
            ps.setString(3, obj.getMaHoaDon());
            ps.setString(4, obj.getLoaiGiaoDich());
            ps.setInt(5, obj.getSoDiemThayDoi());
            // Chuyển java.util.Date sang java.sql.Timestamp để lưu cả ngày và giờ
            ps.setTimestamp(6, new java.sql.Timestamp(obj.getThoiGianGiaoDich().getTime()));
            ps.setString(7, obj.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(LichSuDiemDTO obj) {
        // Thông thường lịch sử giao dịch không nên cho sửa.
        // Nếu cần, chỉ cho phép sửa trường 'ghiChu'.
        String sql = "UPDATE LichSuDiem SET ghiChu = ? WHERE maGiaoDich = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, obj.getGhiChu());
            ps.setString(2, obj.getMaGiaoDich());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        // Nguyên tắc: Không xóa lịch sử điểm để tránh gian lận
        return false;
    }

    @Override
    public ArrayList<LichSuDiemDTO> getAll() {
        ArrayList<LichSuDiemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM LichSuDiem ORDER BY thoiGianGiaoDich DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new LichSuDiemDTO(
                        rs.getString("maGiaoDich"),
                        rs.getString("maKhachHang"),
                        rs.getString("maHoaDon"),
                        rs.getString("loaiGiaoDich"),
                        rs.getInt("soDiemThayDoi"),
                        rs.getTimestamp("thoiGianGiaoDich"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public LichSuDiemDTO getById(String key) {
        String sql = "SELECT * FROM LichSuDiem WHERE maGiaoDich = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new LichSuDiemDTO(
                        rs.getString("maGiaoDich"),
                        rs.getString("maKhachHang"),
                        rs.getString("maHoaDon"),
                        rs.getString("loaiGiaoDich"),
                        rs.getInt("soDiemThayDoi"),
                        rs.getTimestamp("thoiGianGiaoDich"),
                        rs.getString("ghiChu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hàm bổ sung: Lấy lịch sử điểm của một khách hàng cụ thể
     * Rất hữu ích để hiển thị trên GUI khi xem chi tiết khách hàng
     */
    public ArrayList<LichSuDiemDTO> getByKhachHangId(String maKhachHang) {
        ArrayList<LichSuDiemDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM LichSuDiem WHERE maKhachHang = ? ORDER BY thoiGianGiaoDich DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new LichSuDiemDTO(
                        rs.getString("maGiaoDich"),
                        rs.getString("maKhachHang"),
                        rs.getString("maHoaDon"),
                        rs.getString("loaiGiaoDich"),
                        rs.getInt("soDiemThayDoi"),
                        rs.getTimestamp("thoiGianGiaoDich"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}