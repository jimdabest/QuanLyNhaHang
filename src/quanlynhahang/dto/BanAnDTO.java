package quanlynhahang.dto;

public class BanAnDTO {
    private String maBan;
    private String tenBan;
    private int sucChua;
    private String khuVuc;
    private String trangThai;

    public BanAnDTO(String maBan, String tenBan, String khuVuc, int sucChua, String trangThai) {
    }

    public BanAnDTO(String maBan, String tenBan, int sucChua, String khuVuc, String trangThai) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.sucChua = sucChua;
        this.khuVuc = khuVuc;
        this.trangThai = trangThai;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "BanAnDTO{" +
                "maBan='" + maBan + '\'' +
                ", tenBan='" + tenBan + '\'' +
                ", sucChua=" + sucChua +
                ", khuVuc='" + khuVuc + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
