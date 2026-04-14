package quanlynhahang.dto;

import java.util.Date;

/**
 * DTO ghi lại lịch sử giao dịch điểm thưởng của khách hàng.
 * Dùng để truy vấn điểm tích luỹ, đổi điểm hoặc hoàn tiền.
 */
public class LichSuDiemDTO {
    /**
     * Mã giao dịch điểm duy nhất.
     */
    private String maGiaoDich;
    /**
     * Mã khách hàng tham gia giao dịch.
     */
    private String maKhachHang;
    /**
     * Mã hóa đơn liên quan đến giao dịch điểm.
     */
    private String maHoaDon;
    /**
     * Loại giao dịch điểm, ví dụ "Tích điểm" hay "Đổi điểm".
     */
    private String loaiGiaoDich;
    /**
     * Số điểm thay đổi trong giao dịch.
     */
    private int soDiemThayDoi;
    /**
     * Thời gian thực hiện giao dịch.
     */
    private Date thoiGianGiaoDich;
    /**
     * Ghi chú bổ sung cho giao dịch điểm.
     */
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
