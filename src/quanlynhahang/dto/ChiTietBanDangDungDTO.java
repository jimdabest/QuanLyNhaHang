package quanlynhahang.dto;

import java.sql.Timestamp;

public class ChiTietBanDangDungDTO {
    private String maBan;
    private String tenBan;
    private String khuVuc;
    private String maHoaDon;
    private String tenKhachHang;
    private Timestamp thoiGianVao; // Dùng Timestamp để lưu cả ngày lẫn giờ phút giây
    private double tongTien;
    private double tienGiamGia;
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