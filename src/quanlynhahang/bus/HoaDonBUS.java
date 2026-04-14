package quanlynhahang.bus;

import quanlynhahang.dao.DBConnection;
import quanlynhahang.dao.HoaDonDAO;
import quanlynhahang.dto.HoaDonDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Lớp xử lý nghiệp vụ liên quan đến hóa đơn.
 * Chịu trách nhiệm kiểm tra trạng thái hóa đơn và áp dụng chính sách thanh toán.
 */
public class HoaDonBUS {
    private HoaDonDAO hoaDonDAO = new HoaDonDAO();

    /**
     * Xác thực tổng tiền thanh toán của hóa đơn phải là số không âm.
     * @param tongTien tổng tiền cần thanh toán
     * @return true nếu tổng tiền hợp lệ, false nếu tổng tiền âm
     */
    public boolean validatePayment(double tongTien) {
        return tongTien >= 0;
    }

    /**
     * Lấy mức giảm giá của khách hàng từ hàm trong cơ sở dữ liệu.
     * @param maKhachHang mã khách hàng cần lấy ưu đãi
     * @return mức giảm giá tính theo phần trăm hoặc số tiền tùy cấu trúc hàm SQL
     */
    public double applyPromotion(String maKhachHang) {
        double mucGiamGia = 0.0;
        String sql = "SELECT dbo.fn_LayGiamGiaKhachHang(?) AS MucGiam";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhachHang);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                mucGiamGia = rs.getDouble("MucGiam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mucGiamGia;
    }

    /**
     * Kiểm tra trạng thái hóa đơn để xác định có thể thao tác tiếp hay không.
     * @param maHoaDon mã hóa đơn cần kiểm tra
     * @return true nếu hóa đơn tồn tại và chưa thanh toán, false nếu đã chốt hoặc không tồn tại
     */
    public boolean checkBillStatus(String maHoaDon) {
        HoaDonDTO hd = hoaDonDAO.getById(maHoaDon);
        return hd != null && hd.getTrangThai().equalsIgnoreCase("Chưa TT");
    }

    /**
     * Mở bàn mới bằng cách gọi DAO thực thi stored procedure.
     * @param maBan mã bàn cần mở
     * @param maKhachHang mã khách hàng (có thể null cho khách vãng lai)
     * @return true nếu mở bàn thành công, false nếu thất bại
     */
    public boolean moBanMoi(String maBan, String maKhachHang) {
        return hoaDonDAO.moBanMoi(maBan, maKhachHang);
    }

    /**
     * Thanh toán hóa đơn bằng cách gọi DAO thực hiện stored procedure chốt bill.
     * @param maHoaDon mã hóa đơn cần thanh toán
     * @return true nếu thanh toán thành công, false nếu có lỗi
     */
    public boolean thanhToanHoaDon(String maHoaDon) {
        return hoaDonDAO.thanhToanHoaDon(maHoaDon);
    }

    /**
     * Áp dụng thông tin khách hàng theo số điện thoại và tính tiền giảm giá cho hóa đơn.
     * @param maHoaDon mã hóa đơn cần cập nhật
     * @param soDienThoai số điện thoại khách hàng (để trống nếu khách vãng lai)
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean capNhatGiamGiaTheoSoDienThoai(String maHoaDon, String soDienThoai) {
        return hoaDonDAO.capNhatGiamGiaTheoSoDienThoai(maHoaDon, soDienThoai);
    }
}