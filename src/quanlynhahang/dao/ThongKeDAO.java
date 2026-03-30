package quanlynhahang.dao;

import quanlynhahang.dto.ChiTietBanDangDungDTO;
import quanlynhahang.dto.DoanhThuMonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ThongKeDAO {

    // =========================================================================
    // 1. LẤY CHI TIẾT BÀN ĐANG SỬ DỤNG (Phục vụ cho màn hình Tính tiền - Order)
    // =========================================================================
    public ChiTietBanDangDungDTO getChiTietBanDangAn(String maBan) {
        ChiTietBanDangDungDTO dto = null;
        String sql = "SELECT * FROM v_ChiTietBanDangDung WHERE MaBan = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maBan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Đóng gói 9 cột dữ liệu từ View SQL vào Thùng carton DTO
                    dto = new ChiTietBanDangDungDTO(
                            rs.getString("MaBan"),
                            rs.getString("TenBan"),
                            rs.getString("KhuVuc"),
                            rs.getString("MaHoaDon"),
                            rs.getString("TenKhachHang"),
                            rs.getTimestamp("ThoiGianVao"),
                            rs.getDouble("TongTien"),
                            rs.getDouble("TienGiamGia"),
                            rs.getDouble("ThanhTienTamtinh")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết bàn đang ăn: " + e.getMessage());
            e.printStackTrace();
        }

        return dto;
    }

    // =========================================================================
    // 2. LẤY BÁO CÁO DOANH THU CÁC MÓN (Phục vụ cho màn hình Báo Cáo)
    // =========================================================================
    public ArrayList<DoanhThuMonDTO> layDoanhThuCacMon() {
        ArrayList<DoanhThuMonDTO> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM v_DoanhThuTheoMon ORDER BY TongDoanhThu DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Nhặt từng dòng trong SQL nhét vào DTO
                DoanhThuMonDTO dto = new DoanhThuMonDTO(
                        rs.getString("MaMon"),
                        rs.getString("TenMon"),
                        rs.getString("PhanLoai"),
                        rs.getInt("TongSoLuongBan"),
                        rs.getDouble("TongDoanhThu")
                );

                // Chất lên xe đẩy (ArrayList)
                danhSach.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        return danhSach;
    }
}