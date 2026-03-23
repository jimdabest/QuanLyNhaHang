package quanlynhahang.dto;

public class HangThanhVienDTO {
    private String maHang;
    private String tenHang;
    private double mucGiamGia;
    private double dieuKienTongChiTieu;
    private String quyenLoiKhac;

    public HangThanhVienDTO() {
    }

    public HangThanhVienDTO(String maHang, String tenHang, double mucGiamGia, double dieuKienTongChiTieu, String quyenLoiKhac) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.mucGiamGia = mucGiamGia;
        this.dieuKienTongChiTieu = dieuKienTongChiTieu;
        this.quyenLoiKhac = quyenLoiKhac;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public double getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(double mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public double getDieuKienTongChiTieu() {
        return dieuKienTongChiTieu;
    }

    public void setDieuKienTongChiTieu(double dieuKienTongChiTieu) {
        this.dieuKienTongChiTieu = dieuKienTongChiTieu;
    }

    public String getQuyenLoiKhac() {
        return quyenLoiKhac;
    }

    public void setQuyenLoiKhac(String quyenLoiKhac) {
        this.quyenLoiKhac = quyenLoiKhac;
    }
}


