package quanlynhahang.bus;

import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;
import java.util.ArrayList;

/**
 * Lớp xử lý nghiệp vụ cho Bàn ăn.
 * Chịu trách nhiệm xác thực dữ liệu, kiểm tra điều kiện và điều phối thao tác với BanAnDAO.
 */
public class BanAnBUS {
    private BanAnDAO banAnDAO = new BanAnDAO();
    private ArrayList<BanAnDTO> listBanAn;

    /**
     * Kiểm tra sức chứa của bàn phải là số nguyên dương.
     * @param sucChua số lượng ghế của bàn
     * @return true nếu sức chứa hợp lệ (>0), false nếu không hợp lệ
     */
    public boolean checkSucChua(int sucChua) {
        return sucChua > 0;
    }

    /**
     * Kiểm tra xem trạng thái bàn mới có thể cập nhật từ trạng thái hiện tại hay không.
     * @param trangThaiCu trạng thái hiện tại của bàn
     * @param trangThaiMoi trạng thái muốn chuyển sang
     * @return false nếu cố gắng chuyển từ "Đang ăn" sang "Trống", ngược lại true
     */
    public boolean canUpdateStatus(String trangThaiCu, String trangThaiMoi) {
        if (trangThaiCu.equalsIgnoreCase("Đang ăn") && trangThaiMoi.equalsIgnoreCase("Trống")) {
            return false; // Chặn không cho ép thủ công
        }
        return true; // Các trường hợp khác hợp lệ
    }

    /**
     * Khởi tạo BUS và nạp danh sách bàn từ Database vào bộ nhớ.
     */
    public BanAnBUS() {
        this.listBanAn = banAnDAO.getAll();
    }

    /**
     * Lấy danh sách tất cả bàn ăn từ cơ sở dữ liệu.
     * @return danh sách BanAnDTO hiện có
     */
    public ArrayList<BanAnDTO> getAll() {
        this.listBanAn = banAnDAO.getAll();
        return listBanAn;
    }

    /**
     * Thêm bàn ăn mới vào hệ thống sau khi kiểm tra mã bàn và dữ liệu đầu vào.
     * @param b đối tượng BanAnDTO chứa thông tin bàn mới
     * @return thông báo trạng thái kết quả thao tác
     */
    public String addBanAn(BanAnDTO b) {
        // 1. Kiểm tra mã bàn có bị trống không
        if (b.getMaBan().trim().isEmpty()) {
            return "Mã bàn không được để trống!";
        }
        // 2. Kiểm tra trùng mã bằng cách dùng hàm getById bạn vừa test
        if (banAnDAO.getById(b.getMaBan()) != null) {
            return "Mã bàn này đã tồn tại! Vui lòng chọn mã khác.";
        }
        // 3. Gọi DAO để chèn vào Database
        if (banAnDAO.insert(b)) {
            return "Thêm bàn ăn thành công!";
        }
        return "Thêm bàn ăn thất bại do lỗi hệ thống.";
    }

    /**
     * Xóa bàn ăn ra khỏi hệ thống nếu bàn không đang có khách.
     * @param maBan mã bàn cần xóa
     * @return thông báo kết quả xóa
     */
    public String deleteBanAn(String maBan) {
        // 1. Tìm xem bàn đó có tồn tại không
        BanAnDTO b = banAnDAO.getById(maBan);
        if (b == null) {
            return "Không tìm thấy bàn cần xóa.";
        }
        // 2. QUY TẮC QUAN TRỌNG: Không cho xóa nếu bàn đang có khách
        if (b.getTrangThai().equalsIgnoreCase("Đang dùng")) {
            return "Không thể xóa bàn đang có khách đang sử dụng!";
        }
        // 3. Thực hiện xóa
        if (banAnDAO.delete(maBan)) {
            return "Xóa bàn thành công!";
        }
        return "Xóa bàn thất bại.";
    }

    /**
     * Cập nhật thông tin bàn ăn như tên bàn, khu vực, sức chứa và trạng thái.
     * @param b đối tượng BanAnDTO chứa dữ liệu mới của bàn
     * @return thông báo kết quả cập nhật
     */
    public String updateBanAn(BanAnDTO b) {
        if (banAnDAO.update(b)) {
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại.";
    }

    /**
     * Tìm kiếm bàn theo khu vực bằng cách lọc danh sách đã nạp.
     * @param khuVuc tên khu vực hoặc phần của tên khu vực
     * @return danh sách bàn thỏa mãn điều kiện
     */
    public ArrayList<BanAnDTO> getByKhuVuc(String khuVuc) {
        ArrayList<BanAnDTO> result = new ArrayList<>();
        for (BanAnDTO b : listBanAn) {
            if (b.getKhuVuc().toLowerCase().contains(khuVuc.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }
}