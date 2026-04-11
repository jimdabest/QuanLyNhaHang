package quanlynhahang.dto;

/**
 * DTO đại diện cho chi tiết một dòng món ăn trong hóa đơn.
 * Mỗi bản ghi chứa mã món, số lượng, giá bán và ghi chú riêng.
 */
public class ChiTietHoaDonDTO {
    /**
     * Mã hóa đơn liên kết với dòng món này.
     */
    private String maHoaDon;
    /**
     * Mã món ăn.
     */
    private String maMon;
    /**
     * Số lượng món ăn được gọi.
     */
    private int soLuong;
    /**
     * Giá bán áp dụng cho món ăn tại thời điểm gọi.
     */
    private double giaBan;
    /**
     * Ghi chú thêm cho món, ví dụ yêu cầu bỏ rau, thêm đá.
     */
    private String ghiChu;

    // ALT + Insert

    public ChiTietHoaDonDTO() {
    }

    public ChiTietHoaDonDTO(String maHoaDon, String maMon, int soLuong, double giaBan, String ghiChu) {
        this.maHoaDon = maHoaDon;
        this.maMon = maMon;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.ghiChu = ghiChu;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonDTO{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", maMon='" + maMon + '\'' +
                ", soLuong=" + soLuong +
                ", giaBan=" + giaBan +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }
}