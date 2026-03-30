package quanlynhahang.dto;

import java.util.Date;

public class PhieuDatBanDTO {
    private String maDatBan;
    private String maKhachHang;
    private String maBan;
    private Date thoiGianDat;
    private Date thoiGianNhanBan;
    private int soLuongKhach;
    private String trangThai;
    private String ghiChu;

    public PhieuDatBanDTO() {
    }

    public PhieuDatBanDTO(String maDatBan, String maKhachHang, String maBan, Date thoiGianDat, Date thoiGianNhanBan,
            int soLuongKhach, String trangThai, String ghiChu) {
        this.maDatBan = maDatBan;
        this.maKhachHang = maKhachHang;
        this.maBan = maBan;
        this.thoiGianDat = thoiGianDat;
        this.thoiGianNhanBan = thoiGianNhanBan;
        this.soLuongKhach = soLuongKhach;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public String getMaDatBan() {
        return maDatBan;
    }

    public void setMaDatBan(String maDatBan) {
        this.maDatBan = maDatBan;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public Date getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(Date thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }

    public Date getThoiGianNhanBan() {
        return thoiGianNhanBan;
    }

    public void setThoiGianNhanBan(Date thoiGianNhanBan) {
        this.thoiGianNhanBan = thoiGianNhanBan;
    }

    public int getSoLuongKhach() {
        return soLuongKhach;
    }

    public void setSoLuongKhach(int soLuongKhach) {
        this.soLuongKhach = soLuongKhach;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "PhieuDatBanDTO{" +
                "maDatBan='" + maDatBan + '\'' +
                ", maKhachHang='" + maKhachHang + '\'' +
                ", maBan='" + maBan + '\'' +
                ", thoiGianDat=" + thoiGianDat +
                ", thoiGianNhanBan=" + thoiGianNhanBan +
                ", soLuongKhach=" + soLuongKhach +
                ", trangThai='" + trangThai + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}
