package quanlynhahang.dto;

/**
 * DTO đại diện cho thông tin bàn ăn trong hệ thống quản lý nhà hàng.
 * Sử dụng để truyền dữ liệu giữa các lớp GUI, BUS và DAO.
 */
public class BanAnDTO {
    /**
     * Mã định danh duy nhất của bàn ăn.
     * Dùng để tham chiếu bàn khi mở hóa đơn, đặt bàn, hoặc cập nhật trạng thái.
     */
    private String maBan;
    /**
     * Tên hiển thị của bàn (ví dụ B01, VIP01).
     */
    private String tenBan;
    /**
     * Sức chứa tối đa của bàn, dùng để kiểm tra số khách và phân khu vực.
     */
    private int sucChua;
    /**
     * Khu vực đặt bàn, ví dụ "Khu A", "Khu VIP".
     */
    private String khuVuc;
    /**
     * Trạng thái hiện tại của bàn, ví dụ "Trống", "Đang ăn", "Đã đặt".
     */
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
