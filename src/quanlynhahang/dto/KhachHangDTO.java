package quanlynhahang.dto;

import java.util.Date;

/**
 * DTO đại diện cho khách hàng của nhà hàng.
 * Chứa thông tin thẻ thành viên, điểm tích lũy và trạng thái hoạt động.
 */
public class KhachHangDTO {
    /**
     * Mã khách hàng duy nhất trong hệ thống.
     */
    private String maKhachHang;
    /**
     * Mã hạng thành viên của khách (Bạc, Vàng, Kim Cương,...).
     */
    private String maHang;
    /**
     * Tên khách hàng.
     */
    private String tenKH;
    /**
     * Số điện thoại liên hệ của khách hàng.
     */
    private String soDienThoai;
    /**
     * Tổng chi tiêu đã tích lũy của khách để xác định hạng và ưu đãi.
     */
    private double tongChiTieu;
    /**
     * Ngày khách hàng đăng ký thành viên.
     */
    private Date ngayDangKy;
    /**
     * Trạng thái khách hàng còn hoạt động hay đã bị khoá.
     */
    private boolean trangThai;

    public KhachHangDTO(String maKhachHang, String tenKhachHang, String soDienThoai, double tongChiTieu, int diemHienTai, String maHang) {
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
