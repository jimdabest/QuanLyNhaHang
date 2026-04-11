package quanlynhahang.gui;

import quanlynhahang.bus.HoaDonBUS;
import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel hiển thị sơ đồ bàn ăn trực tuyến.
 * Cho phép nhân viên xem trạng thái bàn và mở bàn mới.
 */
public class SoDoBanPanel extends JPanel {

    /**
     * Panel chứa các nút biểu diễn từng bàn.
     */
    private JPanel pnlTables;

    public SoDoBanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==========================================
        // 1. HEADER: TIÊU ĐỀ & NÚT LÀM MỚI
        // ==========================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(241, 245, 249));

        JLabel lblTitle = new JLabel("🔲 SƠ ĐỒ BÀN ĂN TRỰC TUYẾN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 64, 175));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("🔄 Làm mới sơ đồ");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadSoDoBan()); // Nút tải lại sơ đồ
        pnlHeader.add(btnRefresh, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // ==========================================
        // 2. MAIN: KHU VỰC HIỂN THỊ CÁC BÀN
        // ==========================================
        // Dùng FlowLayout để các bàn tự động rớt dòng khi hết chỗ
        pnlTables = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlTables.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(pnlTables);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Lăn chuột cho mượt
        add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. CHÚ THÍCH TRẠNG THÁI BÀN (Dưới cùng)
        // ==========================================
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlFooter.setBackground(new Color(241, 245, 249));
        pnlFooter.add(taoChuThich("Trống", new Color(34, 197, 94)));
        pnlFooter.add(taoChuThich("Đang ăn", new Color(239, 68, 68)));
        pnlFooter.add(taoChuThich("Đã đặt", new Color(245, 158, 11)));
        add(pnlFooter, BorderLayout.SOUTH);

        // Gọi hàm vẽ sơ đồ bàn ngay khi vừa mở form
        loadSoDoBan();
    }

    // ==========================================
    // CÁC HÀM XỬ LÝ LOGIC
    // ==========================================

    /**
     * Tải dữ liệu bàn từ cơ sở dữ liệu và tạo các nút hiển thị sơ đồ bàn.
     * Mỗi nút sẽ có màu sắc tương ứng với trạng thái bàn.
     */
    public void loadSoDoBan() {
        pnlTables.removeAll(); // Xóa sạch các bàn cũ trên màn hình

        ArrayList<BanAnDTO> listBan = new BanAnDAO().getAll();

        for (BanAnDTO ban : listBan) {
            // Thiết kế giao diện cho từng nút Bàn
            String text = "<html><center><b>" + ban.getTenBan() + "</b><br/>"
                    + "<span style='font-size:10px'>" + ban.getKhuVuc() + " (" + ban.getSucChua() + " ghế)</span><br/><br/>"
                    + "<i>" + ban.getTrangThai() + "</i></center></html>";

            JButton btnBan = new JButton(text);
            btnBan.setPreferredSize(new Dimension(140, 100));
            btnBan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnBan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnBan.setForeground(Color.WHITE);
            btnBan.setFocusPainted(false);

            // Set màu theo trạng thái bàn
            switch (ban.getTrangThai()) {
                case "Trống":
                    btnBan.setBackground(new Color(34, 197, 94)); // Xanh lá
                    break;
                case "Đang ăn":
                    btnBan.setBackground(new Color(239, 68, 68)); // Đỏ
                    break;
                case "Đã đặt":
                    btnBan.setBackground(new Color(245, 158, 11)); // Cam
                    break;
                default:
                    btnBan.setBackground(Color.GRAY);
                    break;
            }

            // Xử lý sự kiện khi Click vào Bàn (QUAN TRỌNG)
            btnBan.addActionListener(e -> {
                // Lấy MainFrame hiện tại để có thể chuyển trang
                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

                if (ban.getTrangThai().equalsIgnoreCase("Đang ăn")) {
                    // 1. Bàn đang có khách -> Chuyển thẳng sang trang Order để tính tiền/gọi thêm món
                    main.getOrderPanel().openTable(ban.getMaBan());
                    main.chuyenTrang("CardOrder");

                } else if (ban.getTrangThai().equalsIgnoreCase("Trống")) {
                    // 2. Bàn trống -> Hỏi xem có muốn mở bàn mới không
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Bàn " + ban.getTenBan() + " đang trống. Bạn có muốn MỞ BÀN cho khách vào không?",
                            "Xác nhận Mở Bàn", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Gọi SP mở bàn thông qua HoaDonBUS (Chuẩn mô hình 3 lớp)
                        HoaDonBUS hdBUS = new HoaDonBUS();
                        if (hdBUS.moBanMoi(ban.getMaBan(), null)) {
                            JOptionPane.showMessageDialog(this, "✅ Mở bàn thành công! Chuyển sang Gọi món.");
                            loadSoDoBan(); // Làm mới lại sơ đồ để bàn chuyển sang màu Đỏ

                            // Trượt ngay sang trang OrderPanel
                            main.getOrderPanel().openTable(ban.getMaBan());
                            main.chuyenTrang("CardOrder");
                        } else {
                            JOptionPane.showMessageDialog(this, "❌ Lỗi hệ thống, không thể mở bàn!");
                        }
                    }
                } else {
                    // 3. Các trạng thái khác (Đã đặt, Bảo trì...)
                    JOptionPane.showMessageDialog(this, "Bàn này đang ở trạng thái: " + ban.getTrangThai());
                }
            });

            // Nhét nút bàn vào màn hình
            pnlTables.add(btnBan);
        }

        // Vẽ lại giao diện sau khi nhét xong
        pnlTables.revalidate();
        pnlTables.repaint();
    }

    /**
     * Placeholder cho hành động mở bàn từ panel sơ đồ bàn.
     * Hàm có thể được mở rộng để tích hợp trực tiếp với giao diện gọi món.
     * @param maBan mã bàn được mở
     */
    public void openTable(String maBan) {
        System.out.println("Mở bàn gọi món: " + maBan);
    }

    /**
     * Tạo panel chú thích trạng thái bàn với một ô màu và nhãn.
     * @param text nhãn trạng thái
     * @param color màu của trạng thái
     * @return JPanel chứa chú thích
     */
    private JPanel taoChuThich(String text, Color color) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnl.setBackground(new Color(241, 245, 249));

        JLabel box = new JLabel();
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 20));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnl.add(box);
        pnl.add(lbl);
        return pnl;
    }
}