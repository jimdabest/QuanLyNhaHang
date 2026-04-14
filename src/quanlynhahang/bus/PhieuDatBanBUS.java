package quanlynhahang.bus;

import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;
import java.util.Date;

/**
 * Lớp xử lý nghiệp vụ cho phiếu đặt bàn.
 * Bao gồm các kiểm tra thời gian và trạng thái bàn trước khi đặt.
 */
public class PhieuDatBanBUS {
    private BanAnDAO banAnDAO = new BanAnDAO();

    /**
     * Kiểm tra thời gian đặt bàn phải cách hiện tại tối thiểu 30 phút.
     * @param thoiGianDat thời điểm dự kiến khách nhận bàn
     * @return true nếu thời gian hợp lệ, false nếu quá sớm hoặc null
     */
    public boolean checkThoiGian(Date thoiGianDat) {
        if (thoiGianDat == null) return false;
        long currentTime = System.currentTimeMillis();
        long diffInMillis = thoiGianDat.getTime() - currentTime;
        long diffInMinutes = diffInMillis / (60 * 1000);

        return diffInMinutes >= 30;
    }

    /**
     * Kiểm tra xem bàn có đang ở trạng thái "Trống" hay không.
     * @param maBan mã bàn cần kiểm tra
     * @return true nếu bàn trống, false nếu không tìm thấy hoặc không trống
     */
    public boolean checkBanTrong(String maBan) {
        BanAnDTO ban = banAnDAO.getById(maBan);
        return ban != null && ban.getTrangThai().equalsIgnoreCase("Trống");
    }
}