package quanlynhahang.dto;

public class MonAnDTO {
    private String maMon;
    private String tenMon;
    private String phanLoai;
    private double giaHienTai;
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
