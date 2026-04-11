package quanlynhahang.dto;

import java.util.Date;

/**
 * DTO đại diện cho phiếu đặt bàn của khách hàng.
 * Lưu thông tin lịch hẹn, số lượng khách và trạng thái đặt bàn.
 */
public class PhieuDatBanDTO {
    /**
     * Mã phiếu đặt bàn.
     */
    private String maDatBan;
    /**
     * Mã khách hàng đặt bàn.
     */
    private String maKhachHang;
    /**
     * Mã bàn được đặt.
     */
    private String maBan;
    /**
     * Thời điểm tạo phiếu đặt bàn.
     */
    private Date thoiGianDat;
    /**
     * Thời điểm khách hẹn nhận bàn.
     */
    private Date thoiGianNhanBan;
    /**
     * Số lượng khách dự kiến.
     */
    private int soLuongKhach;
    /**
     * Trạng thái của phiếu đặt bàn.
     */
    private String trangThai;
    /**
     * Ghi chú bổ sung cho yêu cầu đặt bàn.
     */
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
