package quanlynhahang.bus;

import quanlynhahang.dao.MonAnDAO;
import quanlynhahang.dto.MonAnDTO;
import java.util.ArrayList;

public class MonAnBUS {
    private MonAnDAO monAnDAO = new MonAnDAO();

    // Lấy toàn bộ danh sách món ăn
    public ArrayList<MonAnDTO> getAll() {
        return monAnDAO.getAll();
    }

    // Thêm món ăn mới với các bước kiểm tra logic
    public String addMonAn(MonAnDTO mon) {
        // 1. Kiểm tra để trống tên món
        if (mon.getTenMon() == null || mon.getTenMon().trim().isEmpty()) {
            return "Tên món ăn không được để trống!";
        }

        // 2. Kiểm tra giá món ăn (Phải là số dương)
        if (mon.getGiaHienTai() < 0) {
            return "Giá món ăn không được là số âm!";
        }

        // 3. Kiểm tra trùng mã món (Nếu mã món do người dùng nhập)
        if (isDuplicateMaMon(mon.getMaMon())) {
            return "Mã món ăn này đã tồn tại trong hệ thống!";
        }

        // 4. Nếu mọi thứ OK, gọi xuống DAO để thực thi
        boolean result = monAnDAO.insert(mon);
        return result ? "Thành công" : "Lỗi: Không thể thêm món ăn vào cơ sở dữ liệu!";
    }

    // Cập nhật thông tin món ăn
    public String updateMonAn(MonAnDTO mon) {
        if (mon.getGiaHienTai() < 0) {
            return "Giá cập nhật không hợp lệ!";
        }

        boolean result = monAnDAO.update(mon);
        return result ? "Cập nhật thành công" : "Lỗi: Cập nhật thất bại!";
    }

    // Hàm phụ trợ kiểm tra trùng mã
    private boolean isDuplicateMaMon(String maMon) {
        ArrayList<MonAnDTO> list = monAnDAO.getAll();
        for (MonAnDTO m : list) {
            if (m.getMaMon().equalsIgnoreCase(maMon)) {
                return true;
            }
        }
        return false;
    }
}