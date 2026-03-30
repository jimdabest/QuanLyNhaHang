package quanlynhahang.dto;

import java.util.Date;

public class HoaDonDTO {
    // mọi người chú ý đặt tên file theo kiểuc camelCase nhé
    private String maHoaDon; // Không đặt là MaHoaDon nha
    private String maBan;
    private String maKhachHang;
    private Date thoiGianVao;
    private Date thoiGianRa;
    private double tongTien;
    private double tienGiamGia;
    private double thanhTien;
    private String trangThai;

    // ALT + Insert


    public HoaDonDTO() {
    }

    public HoaDonDTO(String maHoaDon, String maBan, String maKhachHang, Date thoiGianVao, Date thoiGianRa, double tongTien, double tienGiamGia, double thanhTien, String trangThai) {
        this.maHoaDon = maHoaDon;
        this.maBan = maBan;
        this.maKhachHang = maKhachHang;
        this.thoiGianVao = thoiGianVao;
        this.thoiGianRa = thoiGianRa;
        this.tongTien = tongTien;
        this.tienGiamGia = tienGiamGia;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public Date getThoiGianVao() {
        return thoiGianVao;
    }

    public void setThoiGianVao(Date thoiGianVao) {
        this.thoiGianVao = thoiGianVao;
    }

    public Date getThoiGianRa() {
        return thoiGianRa;
    }

    public void setThoiGianRa(Date thoiGianRa) {
        this.thoiGianRa = thoiGianRa;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienGiamGia() {
        return tienGiamGia;
    }

    public void setTienGiamGia(double tienGiamGia) {
        this.tienGiamGia = tienGiamGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "HoaDonDTO{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", maBan='" + maBan + '\'' +
                ", maKhachHang='" + maKhachHang + '\'' +
                ", thoiGianVao=" + thoiGianVao +
                ", thoiGianRa=" + thoiGianRa +
                ", tongTien=" + tongTien +
                ", tienGiamGia=" + tienGiamGia +
                ", thanhTien=" + thanhTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}

