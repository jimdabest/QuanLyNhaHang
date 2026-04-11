package quanlynhahang.dto;

import java.sql.Timestamp;
import java.util.Date;

/**
 * DTO đại diện cho hóa đơn thanh toán tại nhà hàng.
 * Chứa thông tin liên quan đến bàn, khách hàng, thời gian và số tiền.
 */
public class HoaDonDTO {
    /**
     * Mã hóa đơn duy nhất.
     */
    private String maHoaDon;
    /**
     * Mã bàn liên kết với hóa đơn.
     */
    private String maBan;
    /**
     * Mã khách hàng nếu khách VIP hoặc thành viên, có thể null cho khách vãng lai.
     */
    private String maKhachHang;
    /**
     * Thời điểm khách vào bàn.
     */
    private Date thoiGianVao;
    /**
     * Thời điểm khách rời bàn khi thanh toán.
     */
    private Date thoiGianRa;
    /**
     * Tổng tiền trước khi áp dụng giảm giá.
     */
    private double tongTien;
    /**
     * Số tiền giảm giá áp dụng cho khách hàng/khuyến mãi.
     */
    private double tienGiamGia;
    /**
     * Số tiền phải thu sau khi đã trừ giảm giá.
     */
    private double thanhTien;
    /**
     * Trạng thái hóa đơn, ví dụ "Chưa TT", "Đã TT".
     */
    private String trangThai;

    public HoaDonDTO(String maHoaDon, Timestamp thoiGianVao, Timestamp thoiGianRa, double tongTien, String maBan, String maKH, String trangThai) {
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

