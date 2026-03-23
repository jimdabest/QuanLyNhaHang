package quanlynhahang.dao;

import quanlynhahang.dto.BanAnDTO;
import java.sql.*;
import java.util.ArrayList;

public class BanAnDAO implements IDAO<BanAnDTO, String> {

    @Override
    public boolean insert(BanAnDTO obj) {
        String sql = "INSERT INTO BanAn (MaBan, TenBan, SucChua, KhuVuc, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, obj.getMaBan());
            ps.setNString(2, obj.getTenBan());
            ps.setInt(3, obj.getSucChua());
            ps.setNString(4, obj.getKhuVuc());
            ps.setNString(5, obj.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(BanAnDTO obj) {
        String sql = "UPDATE BanAn SET TenBan = ?, SucChua = ?, KhuVuc = ?, TrangThai = ? WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setNString(1, obj.getTenBan());
            ps.setInt(2, obj.getSucChua());
            ps.setNString(3, obj.getKhuVuc());
            ps.setNString(4, obj.getTrangThai());
            ps.setString(5, obj.getMaBan());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String key) {
        String sql = "DELETE FROM BanAn WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<BanAnDTO> getAll() {
        ArrayList<BanAnDTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM BanAn";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BanAnDTO b = new BanAnDTO(
                        rs.getString("MaBan"),
                        rs.getNString("TenBan"),
                        rs.getInt("SucChua"),
                        rs.getNString("KhuVuc"),
                        rs.getNString("TrangThai")
                );
                ds.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    @Override
    public BanAnDTO getById(String key) {
        String sql = "SELECT * FROM BanAn WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BanAnDTO(
                            rs.getString("MaBan"),
                            rs.getNString("TenBan"),
                            rs.getInt("SucChua"),
                            rs.getNString("KhuVuc"),
                            rs.getNString("TrangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}