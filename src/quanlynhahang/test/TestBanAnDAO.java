package quanlynhahang.test;

import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class TestBanAnDAO {
    public static void main(String[] args) {
        // 1. Khởi tạo đối tượng DAO
        BanAnDAO dao = new BanAnDAO();

        String maCanTim = "B01";

        BanAnDTO ketQua = dao.getById(maCanTim);
        if (ketQua != null) {
            System.out.println("Tìm thấy dữ liệu:");
            System.out.println("+ Tên bàn: " + ketQua.getTenBan());
            System.out.println("+ Sức chứa: " + ketQua.getSucChua());
            System.out.println("+ Khu vực: " + ketQua.getKhuVuc());
            System.out.println("+ Trạng thái: " + ketQua.getTrangThai());
        } else {
            System.out.println("Không tìm thấy bàn nào có mã: " + maCanTim);
            System.out.println("Lưu ý: Hãy kiểm tra lại bảng BanAn trong SQL Server xem đã có mã này chưa.");
        }



    }
}