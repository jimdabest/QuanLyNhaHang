package quanlynhahang.dto;

public class ChiTietHoaDonDTO {
    private String maHoaDon; // Không đặt là MaHoaDon hay mahoadon hay ma_hoa_don
    // Các bạn lưuý đặt tên biến kiểu camelCase nha
    private String maMon;
    private int soLuong;
    private double giaBan;
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