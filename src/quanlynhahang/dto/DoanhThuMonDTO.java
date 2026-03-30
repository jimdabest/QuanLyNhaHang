package quanlynhahang.dto;

public class DoanhThuMonDTO {
    private String maMon;
    private String tenMon;
    private String phanLoai;
    private int tongSoLuongBan;
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