package quanlynhahang.bus;

import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;
import java.util.ArrayList;

public class BanAnBUS {
    private BanAnDAO banAnDAO = new BanAnDAO();
    private ArrayList<BanAnDTO> listBanAn;

    public BanAnBUS() {
        // Load toàn bộ danh sách lên bộ nhớ để xử lý cho nhanh
        this.listBanAn = banAnDAO.getAll();
    }

    public ArrayList<BanAnDTO> getAll() {
        // Luôn làm mới danh sách từ Database để đảm bảo dữ liệu mới nhất
        this.listBanAn = banAnDAO.getAll();
        return listBanAn;
    }

    // Logic: Thêm bàn ăn mới
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

    // Logic: Xóa bàn ăn
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

    // Logic: Cập nhật thông tin
    public String updateBanAn(BanAnDTO b) {
        if (banAnDAO.update(b)) {
            return "Cập nhật thành công!";
        }
        return "Cập nhật thất bại.";
    }

    // Logic: Tìm kiếm bàn theo khu vực (Xử lý trực tiếp trên ArrayList để nhanh)
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