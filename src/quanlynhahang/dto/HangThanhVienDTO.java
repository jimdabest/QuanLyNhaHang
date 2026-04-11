package quanlynhahang.dto;

/**
 * DTO định nghĩa thông tin về hạng thành viên khách hàng.
 * Chứa biểu đồ ưu đãi và điều kiện chi tiêu để phân loại khách.
 */
public class HangThanhVienDTO {
    /**
     * Mã hạng thành viên.
     */
    private String maHang;
    /**
     * Tên hiển thị của hạng thành viên (Bạc, Vàng, Kim Cương,...).
     */
    private String tenHang;
    /**
     * Phần trăm hoặc số tiền giảm giá cho hạng thành viên.
     */
    private double mucGiamGia;
    /**
     * Điều kiện tổng chi tiêu tối thiểu để đạt hạng này.
     */
    private double dieuKienTongChiTieu;
    /**
     * Các quyền lợi bổ sung khác của hạng thành viên.
     */
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


