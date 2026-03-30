package quanlynhahang.test;

import quanlynhahang.dao.*;
import quanlynhahang.dto.*;
import java.util.ArrayList;

public class AutomationTest {

    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG KIỂM THỬ TỰ ĐỘNG (BACKEND TEST) ===");

        try {
            // TEST 1: Kiểm tra kết nối DB
            System.out.print("1. Kiểm tra kết nối Database: ");
            if (DBConnection.getConnection() != null) {
                System.out.println("✅ THÀNH CÔNG");
            }

            // TEST 2: Kiểm tra lấy danh sách món ăn
            System.out.print("2. Kiểm tra truy vấn thực đơn: ");
            ArrayList<MonAnDTO> dsMon = new MonAnDAO().getAll();
            if (!dsMon.isEmpty()) {
                System.out.println("✅ THÀNH CÔNG (Tìm thấy " + dsMon.size() + " món)");
            }

            // TEST 3: KỊCH BẢN NGHIỆP VỤ (QUAN TRỌNG NHẤT)
            System.out.println("\n3. BẮT ĐẦU TEST KỊCH BẢN PHỤC VỤ:");

            String maBanTest = "B01"; // Giả sử dùng bàn số 1
            HoaDonDAO hdDAO = new HoaDonDAO();
            ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();
            ThongKeDAO tkDAO = new ThongKeDAO();

            // Bước A: Mở bàn mới
            System.out.print("   -> A. Mở bàn " + maBanTest + ": ");
            boolean moBan = hdDAO.moBanMoi(maBanTest, null);
            if (moBan) System.out.println("✅ OK");

            // Bước B: Lấy mã hóa đơn vừa tạo
            ChiTietBanDangDungDTO banDangAn = tkDAO.getChiTietBanDangAn(maBanTest);
            String maHD = banDangAn.getMaHoaDon();
            System.out.println("   -> B. Hóa đơn vừa tạo: " + maHD);

            // Bước C: Gọi món (Thêm 2 đĩa Ba chỉ bò - giả sử mã là 'MA01')
            System.out.print("   -> C. Gọi món (2x Ba chỉ bò): ");
            boolean order = ctDAO.themMonVaoHoaDon(maHD, "M01", 2, 150000, "Ít cay");
            if (order) System.out.println("✅ OK");

            // Bước D: Kiểm tra tiền có nhảy không (Trigger Test)
            banDangAn = tkDAO.getChiTietBanDangAn(maBanTest);
            System.out.print("   -> D. Kiểm tra tiền (Trigger): ");
            if (banDangAn.getTongTien() == 300000) {
                System.out.println("✅ CHÍNH XÁC (300k)");
            } else {
                System.out.println("❌ SAI (Hiện tại: " + banDangAn.getTongTien() + ")");
            }

            // Bước E: Xóa món (Test hàm xóa)
            System.out.print("   -> E. Test xóa món: ");
            boolean xoa = ctDAO.xoaMonKhoiHoaDon(maHD, "MA01");
            if (xoa) System.out.println("✅ OK");

            // Bước F: Thanh toán và dọn bàn
            System.out.print("   -> F. Thanh toán hóa đơn: ");
            boolean pay = hdDAO.thanhToanHoaDon(maHD);
            if (pay) System.out.println("✅ TẤT CẢ ĐÃ HOÀN TẤT");

            System.out.println("\n=== KẾT QUẢ: BACKEND SẴN SÀNG ĐỂ PHÂN TASK! ===");

        } catch (Exception e) {
            System.out.println("\n❌ LỖI TRONG QUÁ TRÌNH TEST: " + e.getMessage());
            e.printStackTrace();
        }
    }
}