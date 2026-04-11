package quanlynhahang.dao;

import quanlynhahang.dto.LichSuDiemDTO;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO ghi lại lịch sử giao dịch điểm thưởng của khách hàng.
 * Quản lý thêm và truy vấn bảng LichSuDiem.
 */
public class LichSuDiemDAO implements IDAO<LichSuDiemDTO, String> {

    /**
     * Thêm bản ghi lịch sử điểm mới.
     * @param obj đối tượng LichSuDiemDTO chứa dữ liệu giao dịch điểm
     * @return true nếu thêm thành công, false nếu thất bại
     */
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

    /**
     * Cập nhật ghi chú của một giao dịch điểm nếu cần.
     * @param obj đối tượng LichSuDiemDTO chứa dữ liệu cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
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

    /**
     * Không cho phép xóa bản ghi lịch sử điểm để bảo toàn dữ liệu giao dịch.
     * @param key khóa chính
     * @return luôn trả về false trong triển khai hiện tại
     */
    @Override
    public boolean delete(String key) {
        // Nguyên tắc: Không xóa lịch sử điểm để tránh gian lận
        return false;
    }

    /**
     * Lấy toàn bộ lịch sử giao dịch điểm.
     * @return danh sách LichSuDiemDTO
     */
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

    /**
     * Lấy bản ghi lịch sử điểm theo mã giao dịch.
     * @param key mã giao dịch cần tìm
     * @return LichSuDiemDTO nếu tồn tại, null nếu không
     */
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
     * Lấy toàn bộ lịch sử điểm của một khách hàng.
     * @param maKhachHang mã khách hàng cần lấy lịch sử
     * @return danh sách LichSuDiemDTO của khách hàng
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