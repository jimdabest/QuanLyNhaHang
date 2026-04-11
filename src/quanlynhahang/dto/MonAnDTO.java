package quanlynhahang.dto;

/**
 * DTO lưu trữ thông tin món ăn trong thực đơn.
 * Dùng trong quản lý món, gọi món và báo cáo doanh thu.
 */
public class MonAnDTO {
    /**
     * Mã định danh món ăn.
     */
    private String maMon;
    /**
     * Tên của món ăn.
     */
    private String tenMon;
    /**
     * Phân loại món (ví dụ: Thịt nướng, Hải sản, Nước uống).
     */
    private String phanLoai;
    /**
     * Giá hiện tại tính theo VNĐ.
     */
    private double giaHienTai;
    /**
     * Trạng thái món ăn có còn phục vụ hay đã ngừng bán.
     */
    private boolean trangThaiPhucVu;

    public MonAnDTO() {
    }

    public MonAnDTO(String maMon, String tenMon, String phanLoai, double giaHienTai, boolean trangThaiPhucVu) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.phanLoai = phanLoai;
        this.giaHienTai = giaHienTai;
        this.trangThaiPhucVu = trangThaiPhucVu;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public double getGiaHienTai() {
        return giaHienTai;
    }

    public void setGiaHienTai(double giaHienTai) {
        this.giaHienTai = giaHienTai;
    }

    public boolean isTrangThaiPhucVu() {
        return trangThaiPhucVu;
    }

    public void setTrangThaiPhucVu(boolean trangThaiPhucVu) {
        this.trangThaiPhucVu = trangThaiPhucVu;
    }

    @Override
    public String toString() {
        return "MonAnDTO{" +
                "maMon='" + maMon + '\'' +
                ", tenMon='" + tenMon + '\'' +
                ", phanLoai='" + phanLoai + '\'' +
                ", giaHienTai=" + giaHienTai +
                ", trangThaiPhucVu=" + trangThaiPhucVu +
                '}';
    }
}
