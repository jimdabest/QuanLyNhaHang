package quanlynhahang.dao;

import quanlynhahang.dto.PhieuDatBanDTO;
import java.sql.*;
import java.util.ArrayList;

public class PhieuDatBanDAO implements IDAO<PhieuDatBanDTO, String> {

    @Override
    public boolean insert(PhieuDatBanDTO obj) {
        String sql = "INSERT INTO PhieuDatBan (maDatBan, maKhachHang, maBan, thoiGianDat, thoiGianNhanBan, soLuongKhach, trangThai, ghiChu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaDatBan());
            ps.setString(2, obj.getMaKhachHang());
            ps.setString(3, obj.getMaBan());
            // thoiGianDat thường là thời điểm tạo phiếu (hiện tại)
            ps.setTimestamp(4, new java.sql.Timestamp(obj.getThoiGianDat().getTime()));
            // thoiGianNhanBan là thời điểm khách hẹn tới
            ps.setTimestamp(5, new java.sql.Timestamp(obj.getThoiGianNhanBan().getTime()));
            ps.setInt(6, obj.getSoLuongKhach());
            ps.setString(7, obj.getTrangThai());
            ps.setString(8, obj.getGhiChu());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(PhieuDatBanDTO obj) {
        String sql = "UPDATE PhieuDatBan SET maKhachHang = ?, maBan = ?, thoiGianNhanBan = ?, soLuongKhach = ?, trangThai = ?, ghiChu = ? WHERE maDatBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaKhachHang());
            ps.setString(2, obj.getMaBan());
            ps.setTimestamp(3, new java.sql.Timestamp(obj.getThoiGianNhanBan().getTime()));
            ps.setInt(4, obj.getSoLuongKhach());
            ps.setString(5, obj.getTrangThai());
            ps.setString(6, obj.getGhiChu());
            ps.setString(7, obj.getMaDatBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String key) {
        // Thay vì xóa, thường chúng ta cập nhật trạng thái thành 'Đã hủy'
        String sql = "UPDATE PhieuDatBan SET trangThai = N'Đã hủy' WHERE maDatBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<PhieuDatBanDTO> getAll() {
        ArrayList<PhieuDatBanDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuDatBan ORDER BY thoiGianNhanBan ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new PhieuDatBanDTO(
                        rs.getString("maDatBan"),
                        rs.getString("maKhachHang"),
                        rs.getString("maBan"),
                        rs.getTimestamp("thoiGianDat"),
                        rs.getTimestamp("thoiGianNhanBan"),
                        rs.getInt("soLuongKhach"),
                        rs.getString("trangThai"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public PhieuDatBanDTO getById(String key) {
        String sql = "SELECT * FROM PhieuDatBan WHERE maDatBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PhieuDatBanDTO(
                        rs.getString("maDatBan"),
                        rs.getString("maKhachHang"),
                        rs.getString("maBan"),
                        rs.getTimestamp("thoiGianDat"),
                        rs.getTimestamp("thoiGianNhanBan"),
                        rs.getInt("soLuongKhach"),
                        rs.getString("trangThai"),
                        rs.getString("ghiChu")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}