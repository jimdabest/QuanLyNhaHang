package quanlynhahang.dto;

import java.sql.Timestamp;

/**
 * DTO chứa thông tin tổng quát của bàn đang sử dụng.
 * Dùng để hiển thị trạng thái phòng bàn và hóa đơn đang mở.
 */
public class ChiTietBanDangDungDTO {
    /**
     * Mã bàn hiện tại.
     */
    private String maBan;
    /**
     * Tên hiển thị của bàn.
     */
    private String tenBan;
    /**
     * Khu vực bàn đang nằm.
     */
    private String khuVuc;
    /**
     * Mã hóa đơn đang áp dụng cho bàn này.
     */
    private String maHoaDon;
    /**
     * Tên khách hàng liên kết với hóa đơn nếu có.
     */
    private String tenKhachHang;
    /**
     * Thời điểm khách vào bàn (Timestamp bao gồm cả giờ và phút).
     */
    private Timestamp thoiGianVao;
    /**
     * Tổng tiền trước khi giảm giá.
     */
    private double tongTien;
    /**
     * Số tiền giảm giá đã áp dụng.
     */
    private double tienGiamGia;
    /**
     * Tổng tiền tạm tính sau khi đã áp dụng giảm giá.
     */
    private double thanhTienTamTinh;

    // Khởi tạo Constructor (Không tham số)
    public ChiTietBanDangDungDTO() {
    }

    // Khởi tạo Constructor (Đầy đủ tham số)
    public ChiTietBanDangDungDTO(String maBan, String tenBan, String khuVuc, String maHoaDon,
                                 String tenKhachHang, Timestamp thoiGianVao, double tongTien,
                                 double tienGiamGia, double thanhTienTamTinh) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.khuVuc = khuVuc;
        this.maHoaDon = maHoaDon;
        this.tenKhachHang = tenKhachHang;
        this.thoiGianVao = thoiGianVao;
        this.tongTien = tongTien;
        this.tienGiamGia = tienGiamGia;
        this.thanhTienTamTinh = thanhTienTamTinh;
    }

    // =====================================
    // CÁC HÀM GETTER VÀ SETTER
    // =====================================
    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }

    public String getTenBan() { return tenBan; }
    public void setTenBan(String tenBan) { this.tenBan = tenBan; }

    public String getKhuVuc() { return khuVuc; }
    public void setKhuVuc(String khuVuc) { this.khuVuc = khuVuc; }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public Timestamp getThoiGianVao() { return thoiGianVao; }
    public void setThoiGianVao(Timestamp thoiGianVao) { this.thoiGianVao = thoiGianVao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getTienGiamGia() { return tienGiamGia; }
    public void setTienGiamGia(double tienGiamGia) { this.tienGiamGia = tienGiamGia; }

    public double getThanhTienTamTinh() { return thanhTienTamTinh; }
    public void setThanhTienTamTinh(double thanhTienTamTinh) { this.thanhTienTamTinh = thanhTienTamTinh; }
}