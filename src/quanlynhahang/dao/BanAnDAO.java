package quanlynhahang.dao;

import quanlynhahang.dto.BanAnDTO;
import java.sql.*;
import java.util.ArrayList;

public class BanAnDAO implements IDAO<BanAnDTO, String> {

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