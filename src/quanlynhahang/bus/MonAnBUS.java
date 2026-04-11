package quanlynhahang.bus;

import quanlynhahang.dao.MonAnDAO;
import quanlynhahang.dto.MonAnDTO;
import java.util.ArrayList;

/**
 * Lớp xử lý nghiệp vụ cho món ăn.
 * Chịu trách nhiệm kiểm tra dữ liệu món, kiểm tra trùng mã và điều phối DAO.
 */
public class MonAnBUS {
    private MonAnDAO monAnDAO = new MonAnDAO();

    /**
     * Kiểm tra giá món ăn không âm.
     * @param gia giá món cần kiểm tra
     * @return true nếu giá hợp lệ, false nếu giá âm
     */
    public boolean checkGia(double gia) {
        return gia >= 0;
    }

    /**
     * Kiểm tra tên món ăn không được để trống.
     * @param tenMon tên món cần kiểm tra
     * @return true nếu tên hợp lệ, false nếu trống
     */
    public boolean checkTenMon(String tenMon) {
        return tenMon != null && !tenMon.trim().isEmpty();
    }

    /**
     * Kiểm tra mã món có tồn tại trong hệ thống chưa.
     * @param maMon mã món cần kiểm tra
     * @return true nếu mã đã tồn tại, false nếu chưa
     */
    public boolean checkDuplicate(String maMon) {
        // Sử dụng hàm getById trong DAO, nếu trả về khác null nghĩa là đã tồn tại
        return monAnDAO.getById(maMon) != null;
    }

    /**
     * Lấy danh sách toàn bộ món ăn từ cơ sở dữ liệu.
     * @return danh sách MonAnDTO
     */
    public ArrayList<MonAnDTO> getAll() {
        return monAnDAO.getAll();
    }

    /**
     * Thêm món ăn mới sau khi kiểm tra tên và giá.
     * @param mon đối tượng MonAnDTO chứa thông tin món mới
     * @return thông báo kết quả thêm món
     */
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

    /**
     * Cập nhật thông tin món ăn đã tồn tại trong cơ sở dữ liệu.
     * @param mon đối tượng MonAnDTO chứa thông tin mới
     * @return thông báo kết quả cập nhật
     */
    public String updateMonAn(MonAnDTO mon) {
        if (mon.getGiaHienTai() < 0) {
            return "Giá cập nhật không hợp lệ!";
        }

        boolean result = monAnDAO.update(mon);
        return result ? "Cập nhật thành công" : "Lỗi: Cập nhật thất bại!";
    }

    /**
     * Kiểm tra trùng mã món trong danh sách hiện có.
     * @param maMon mã món cần kiểm tra
     * @return true nếu mã trùng, false nếu không trùng
     */
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