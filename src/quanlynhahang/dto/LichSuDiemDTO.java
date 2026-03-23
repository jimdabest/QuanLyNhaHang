package quanlynhahang.dto;

import java.util.Date;

public class LichSuDiemDTO {
    private String maGiaoDich;
    private String maKhachHang;
    private String maHoaDon;
    private String loaiGiaoDich;
    private int soDiemThayDoi;
    private Date thoiGianGiaoDich;
    private String ghiChu;

    public LichSuDiemDTO() {
    }

    public LichSuDiemDTO(String maGiaoDich, String maKhachHang, String maHoaDon, String loaiGiaoDich, int soDiemThayDoi, Date thoiGianGiaoDich, String ghiChu) {
        this.maGiaoDich = maGiaoDich;
        this.maKhachHang = maKhachHang;
        this.maHoaDon = maHoaDon;
        this.loaiGiaoDich = loaiGiaoDich;
        this.soDiemThayDoi = soDiemThayDoi;
        this.thoiGianGiaoDich = thoiGianGiaoDich;
        this.ghiChu = ghiChu;
    }

    public String getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public int getSoDiemThayDoi() {
        return soDiemThayDoi;
    }

    public void setSoDiemThayDoi(int soDiemThayDoi) {
        this.soDiemThayDoi = soDiemThayDoi;
    }

    public Date getThoiGianGiaoDich() {
        return thoiGianGiaoDich;
    }

    public void setThoiGianGiaoDich(Date thoiGianGiaoDich) {
        this.thoiGianGiaoDich = thoiGianGiaoDich;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
