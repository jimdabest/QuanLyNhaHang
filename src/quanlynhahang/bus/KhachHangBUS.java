package quanlynhahang.bus;

import quanlynhahang.dao.KhachHangDAO;
import quanlynhahang.dto.KhachHangDTO;

/**
 * Lớp xử lý nghiệp vụ khách hàng.
 * Cung cấp các hàm kiểm tra định dạng, tính điểm và xác định hạng khách.
 */
public class KhachHangBUS {
    private KhachHangDAO khDAO = new KhachHangDAO();

    /**
     * Xác thực số điện thoại khách hàng phải đúng 10 chữ số.
     * @param phone số điện thoại cần kiểm tra
     * @return true nếu hợp lệ, false nếu không hợp lệ
     */
    public boolean validatePhone(String phone) {
        return phone != null && phone.matches("^\\d{10}$");
    }

    /**
     * Tính số tiền còn thiếu để khách hàng lên hạng thành viên tiếp theo.
     * @param tongChiTieuHienTai tổng chi tiêu hiện tại của khách
     * @return số tiền cần thêm để lên hạng tiếp theo, hoặc 0 nếu đã ở hạng cao nhất
     */
    public double calculateNextRank(double tongChiTieuHienTai) {
        if (tongChiTieuHienTai < 2000000) {
            return 2000000 - tongChiTieuHienTai; // Lên Bạc
        } else if (tongChiTieuHienTai < 5000000) {
            return 5000000 - tongChiTieuHienTai; // Lên Vàng
        } else if (tongChiTieuHienTai < 15000000) {
            return 15000000 - tongChiTieuHienTai; // Lên Kim Cương
        }
        return 0; // Đã đạt hạng cao nhất
    }
}