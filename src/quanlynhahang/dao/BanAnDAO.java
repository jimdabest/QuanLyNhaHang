package quanlynhahang.dao;

import quanlynhahang.dto.BanAnDTO;
import java.sql.*;
import java.util.ArrayList;

/**
 * DAO thực hiện truy vấn CRUD cho bảng BanAn.
 * Chịu trách nhiệm ghi nhận, chỉnh sửa và xóa dữ liệu bàn ăn trong database.
 */
public class BanAnDAO implements IDAO<BanAnDTO, String> {

    /**
     * Thêm bàn ăn mới vào bảng BanAn.
     * @param obj đối tượng BanAnDTO chứa dữ liệu bàn ăn
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    @Override
    public boolean insert(BanAnDTO obj) {
        String sql = "INSERT INTO BanAn (maBan, tenBan, sucChua, khuVuc, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaBan());
            ps.setString(2, obj.getTenBan());
            ps.setInt(3, obj.getSucChua());
            ps.setString(4, obj.getKhuVuc());
            ps.setString(5, obj.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin bàn ăn trong cơ sở dữ liệu.
     * @param obj đối tượng BanAnDTO chứa dữ liệu mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean update(BanAnDTO obj) {
        String sql = "UPDATE BanAn SET tenBan = ?, sucChua = ?, khuVuc = ?, trangThai = ? WHERE maBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getTenBan());
            ps.setInt(2, obj.getSucChua());
            ps.setString(3, obj.getKhuVuc());
            ps.setString(4, obj.getTrangThai());
            ps.setString(5, obj.getMaBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật riêng trạng thái của bàn ăn.
     * @param maBan mã bàn cần đổi trạng thái
     * @param trangThai trạng thái mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatTrangThai(String maBan, String trangThai) {
        String sql = "UPDATE BanAn SET trangThai = ? WHERE maBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ps.setString(2, maBan);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa bàn ăn theo mã bàn.
     * Lưu ý DAO chỉ thực hiện xóa vật lý, BUS cần đảm bảo không xóa bàn đang có khách.
     * @param key mã bàn cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    @Override
    public boolean delete(String key) {
        // Lưu ý: BUS nên chặn không cho xóa nếu bàn đang có trạng thái "Đang ăn"
        String sql = "DELETE FROM BanAn WHERE maBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể xóa bàn vì đang có dữ liệu hóa đơn liên quan.");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy toàn bộ danh sách bàn ăn từ bảng BanAn.
     * @return danh sách BanAnDTO
     */
    @Override
    public ArrayList<BanAnDTO> getAll() {
        ArrayList<BanAnDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Sử dụng Constructor đúng thứ tự: maBan, tenBan, sucChua, khuVuc, trangThai
                list.add(new BanAnDTO(
                        rs.getString("maBan"),
                        rs.getString("tenBan"),
                        rs.getInt("sucChua"),
                        rs.getString("khuVuc"),
                        rs.getString("trangThai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy thông tin bàn ăn theo mã bàn.
     * @param key mã bàn cần tìm
     * @return BanAnDTO nếu tìm thấy, null nếu không
     */
    @Override
    public BanAnDTO getById(String key) {
        String sql = "SELECT * FROM BanAn WHERE maBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BanAnDTO(
                        rs.getString("maBan"),
                        rs.getString("tenBan"),
                        rs.getInt("sucChua"),
                        rs.getString("khuVuc"),
                        rs.getString("trangThai")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}