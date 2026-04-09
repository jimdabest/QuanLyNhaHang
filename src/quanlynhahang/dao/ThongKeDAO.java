package quanlynhahang.dao;

import quanlynhahang.dto.ChiTietBanDangDungDTO;
import quanlynhahang.dto.DoanhThuMonDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO phục vụ báo cáo và thống kê.
 * Lấy dữ liệu tổng hợp phục vụ màn hình tính tiền và báo cáo doanh thu.
 */
public class ThongKeDAO {

    /**
     * Lấy thông tin chi tiết của bàn đang sử dụng từ view v_ChiTietBanDangDung.
     * @param maBan mã bàn cần lấy chi tiết
     * @return ChiTietBanDangDungDTO nếu bàn đang có khách, null nếu không
     */
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

    /**
     * Lấy báo cáo doanh thu theo món từ view v_DoanhThuTheoMon.
     * @return danh sách DoanhThuMonDTO sắp xếp theo doanh thu giảm dần
     */
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