package quanlynhahang.dto;

/**
 * DTO phục vụ báo cáo doanh thu theo từng món.
 * Lưu lượng bán và doanh thu để tổng hợp báo cáo bán hàng.
 */
public class DoanhThuMonDTO {
    /**
     * Mã món ăn.
     */
    private String maMon;
    /**
     * Tên món ăn.
     */
    private String tenMon;
    /**
     * Phân loại món để phân tích theo nhóm.
     */
    private String phanLoai;
    /**
     * Tổng số lượng món đã bán.
     */
    private int tongSoLuongBan;
    /**
     * Tổng doanh thu tạo ra từ món này.
     */
    private double tongDoanhThu;

    // Khởi tạo Constructor (Không tham số)
    public DoanhThuMonDTO() {
    }

    // Khởi tạo Constructor (Đầy đủ tham số)
    public DoanhThuMonDTO(String maMon, String tenMon, String phanLoai, int tongSoLuongBan, double tongDoanhThu) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.phanLoai = phanLoai;
        this.tongSoLuongBan = tongSoLuongBan;
        this.tongDoanhThu = tongDoanhThu;
    }

    // =====================================
    // CÁC HÀM GETTER VÀ SETTER
    // =====================================
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public String getPhanLoai() { return phanLoai; }
    public void setPhanLoai(String phanLoai) { this.phanLoai = phanLoai; }

    public int getTongSoLuongBan() { return tongSoLuongBan; }
    public void setTongSoLuongBan(int tongSoLuongBan) { this.tongSoLuongBan = tongSoLuongBan; }

    public double getTongDoanhThu() { return tongDoanhThu; }
    public void setTongDoanhThu(double tongDoanhThu) { this.tongDoanhThu = tongDoanhThu; }
}