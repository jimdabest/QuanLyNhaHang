package quanlynhahang.dao;

import quanlynhahang.dto.ChiTietHoaDonDTO;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO quản lý chi tiết món ăn trong hóa đơn.
 * Thực hiện thao tác thêm, cập nhật, xóa món và truy vấn chi tiết từ database.
 */
public class ChiTietHoaDonDAO implements IDAO<ChiTietHoaDonDTO, String> {

    /**
     * Thêm dòng món ăn vào hóa đơn.
     * @param obj đối tượng ChiTietHoaDonDTO chứa dữ liệu món cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    @Override
    public boolean insert(ChiTietHoaDonDTO obj) {
        return themMonVaoHoaDon(obj.getMaHoaDon(), obj.getMaMon(), obj.getSoLuong(), obj.getGiaBan(), obj.getGhiChu());
    }

    /**
     * Thêm món vào hóa đơn với số lượng, giá bán và ghi chú cụ thể.
     * @param maHoaDon mã hóa đơn cần thêm món
     * @param maMon mã món ăn
     * @param soLuong số lượng món
     * @param giaBan giá bán áp dụng
     * @param ghiChu ghi chú món ăn
     * @return true nếu thực hiện thành công, false nếu lỗi
     */
    public boolean themMonVaoHoaDon(String maHoaDon, String maMon, int soLuong, double giaBan, String ghiChu) {
        // Khi thêm món, Trigger SQL của bạn sẽ tự động gọi hàm fn_TongTienHoaDon để cập nhật bảng HoaDon
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maMon, soLuong, giaBan, ghiChu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ps.setString(2, maMon);
            ps.setInt(3, soLuong);
            ps.setDouble(4, giaBan);
            ps.setString(5, ghiChu);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật số lượng hoặc ghi chú cho một món đã tồn tại trong hóa đơn.
     * @param obj đối tượng ChiTietHoaDonDTO chứa dữ liệu cập nhật
     * @return true nếu cập nhật thành công, false nếu không
     */
    @Override
    public boolean update(ChiTietHoaDonDTO obj) {
        // Cập nhật số lượng hoặc ghi chú của một món cụ thể trong hóa đơn
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, ghiChu = ? WHERE maHoaDon = ? AND maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, obj.getSoLuong());
            ps.setString(2, obj.getGhiChu());
            ps.setString(3, obj.getMaHoaDon());
            ps.setString(4, obj.getMaMon());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa toàn bộ món ăn của một hóa đơn.
     * Lưu ý phương thức này xóa theo mã hóa đơn, không xóa theo mã món riêng.
     * @param key mã hóa đơn cần xóa toàn bộ món
     * @return true nếu xóa thành công, false nếu không
     */
    @Override
    public boolean delete(String key) {
        // Interface nhận 1 tham số String, nên hàm này hiểu là xóa TẤT CẢ các món của 1 mã hóa đơn
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
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
     * Xóa một món cụ thể khỏi hóa đơn.
     * @param maHoaDon mã hóa đơn chứa món cần xóa
     * @param maMon mã món cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean xoaMonKhoiHoaDon(String maHoaDon, String maMon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maMon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ps.setString(2, maMon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả các dòng chi tiết hóa đơn từ bảng ChiTietHoaDon.
     * @return danh sách ChiTietHoaDonDTO
     */
    @Override
    public ArrayList<ChiTietHoaDonDTO> getAll() {
        ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new ChiTietHoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaBan"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách món ăn theo mã hóa đơn.
     * Dùng để hiển thị chi tiết order của hóa đơn trên giao diện.
     * @param maHoaDon mã hóa đơn cần truy vấn
     * @return danh sách ChiTietHoaDonDTO các món của hóa đơn
     */
    public ArrayList<ChiTietHoaDonDTO> getByHoaDonId(String maHoaDon) {
        ArrayList<ChiTietHoaDonDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietHoaDonDTO(
                        rs.getString("maHoaDon"),
                        rs.getString("maMon"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaBan"),
                        rs.getString("ghiChu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm không được sử dụng trong bối cảnh khóa chính tổng hợp của ChiTietHoaDon.
     * @param key khóa chính không đầy đủ
     * @return luôn trả về null trong triển khai hiện tại
     */
    @Override
    public ChiTietHoaDonDTO getById(String key) {
        // Do dùng khóa chính tổng hợp nên hàm 1 tham số này ít được dùng trực tiếp
        return null;
    }
}