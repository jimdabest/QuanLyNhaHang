package quanlynhahang.dto;

import java.util.Date;

public class KhachHangDTO {
    private String maKhachHang;
    private String maHang;
    private String tenKH;
    private String soDienThoai;
    private double tongChiTieu;
    private Date ngayDangKy;
    private boolean trangThai;

    public KhachHangDTO() {
    }

    public KhachHangDTO(String maKhachHang, String maHang, String tenKH, String soDienThoai, double tongChiTieu, Date ngayDangKy, boolean trangThai) {
        this.maKhachHang = maKhachHang;
        this.maHang = maHang;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
        this.tongChiTieu = tongChiTieu;
        this.ngayDangKy = ngayDangKy;
        this.trangThai = trangThai;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public double getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(double tongChiTieu) {
        this.tongChiTieu = tongChiTieu;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
