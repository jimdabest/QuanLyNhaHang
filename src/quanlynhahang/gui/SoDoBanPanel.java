package quanlynhahang.gui;

import quanlynhahang.bus.HoaDonBUS;
import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dao.PhieuDatBanDAO;
import quanlynhahang.dto.BanAnDTO;
import quanlynhahang.dto.PhieuDatBanDTO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel hiển thị sơ đồ bàn ăn trực tuyến.
 * Đã fix lỗi tràn hàng ngang bằng GridLayout và ScrollPane tối ưu.
 */
public class SoDoBanPanel extends JPanel {

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

        JLabel lblTitle = new JLabel("SƠ ĐỒ BÀN ĂN TRỰC TUYẾN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 64, 175));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("Làm mới sơ đồ");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadSoDoBan());
        pnlHeader.add(btnRefresh, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        // ==========================================
        // 2. MAIN: KHU VỰC HIỂN THỊ CÁC BÀN (FIXED)
        // ==========================================
        // Sử dụng GridLayout với 5 cột, số hàng tự động (0)
        pnlTables = new JPanel(new GridLayout(0, 4, 15, 15));
        pnlTables.setBackground(Color.WHITE);

        // Panel đệm giúp pnlTables không bị giãn chiều dọc khi ít bàn
        JPanel pnlContainer = new JPanel(new BorderLayout());
        pnlContainer.setBackground(Color.WHITE);
        pnlContainer.add(pnlTables, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(pnlContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Chặn thanh cuộn ngang tuyệt đối
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. CHÚ THÍCH TRẠNG THÁI BÀN
        // ==========================================
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlFooter.setBackground(new Color(241, 245, 249));
        pnlFooter.add(taoChuThich("Trống", new Color(34, 197, 94)));
        pnlFooter.add(taoChuThich("Đang ăn", new Color(239, 68, 68)));
        pnlFooter.add(taoChuThich("Đã đặt", new Color(245, 158, 11)));
        add(pnlFooter, BorderLayout.SOUTH);

        loadSoDoBan();
    }

    public void loadSoDoBan() {
        pnlTables.removeAll();

        ArrayList<BanAnDTO> listBan = new BanAnDAO().getAll();

        for (BanAnDTO ban : listBan) {
            String text = "<html><center><b>" + ban.getTenBan() + "</b><br/>"
                    + "<span style='font-size:10px'>" + ban.getKhuVuc() + " (" + ban.getSucChua() + " ghế)</span><br/><br/>"
                    + "<i>" + ban.getTrangThai() + "</i></center></html>";

            JButton btnBan = new JButton(text);
            btnBan.setPreferredSize(new Dimension(140, 100));
            btnBan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnBan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnBan.setForeground(Color.WHITE);
            btnBan.setFocusPainted(false);

            // Set màu theo trạng thái
            switch (ban.getTrangThai()) {
                case "Trống":
                    btnBan.setBackground(new Color(34, 197, 94));
                    break;
                case "Đang ăn":
                    btnBan.setBackground(new Color(239, 68, 68));
                    break;
                case "Đã đặt":
                    btnBan.setBackground(new Color(245, 158, 11));
                    break;
                default:
                    btnBan.setBackground(Color.GRAY);
                    break;
            }

            // Sự kiện Click bàn
            btnBan.addActionListener(e -> {
                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

                if (ban.getTrangThai().equalsIgnoreCase("Đang ăn")) {
                    main.openTable(ban.getMaBan());
                    // Lưu ý: Chuyển sang card layout chứa bill nếu cần
                } else if (ban.getTrangThai().equalsIgnoreCase("Trống")) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Mở bàn " + ban.getTenBan() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (new HoaDonBUS().moBanMoi(ban.getMaBan(), null)) {
                            loadSoDoBan();
                            main.openTable(ban.getMaBan());
                        }
                    }
                } else if (ban.getTrangThai().equalsIgnoreCase("Đã đặt")) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Khách đặt bàn " + ban.getTenBan() + " đã đến?", "Nhận bàn", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        PhieuDatBanDAO phieuDAO = new PhieuDatBanDAO();
                        PhieuDatBanDTO phieu = null;
                        for (PhieuDatBanDTO p : phieuDAO.getAll()) {
                            if (p.getMaBan().equals(ban.getMaBan()) && p.getTrangThai().equalsIgnoreCase("Chờ nhận")) {
                                phieu = p; break;
                            }
                        }
                        if (phieu != null) {
                            phieu.setTrangThai("Đã nhận");
                            phieuDAO.update(phieu);
                            if (new HoaDonBUS().moBanMoi(ban.getMaBan(), phieu.getMaKhachHang())) {
                                loadSoDoBan();
                                main.openTable(ban.getMaBan());
                            }
                        }
                    }
                }
            });

            pnlTables.add(btnBan);
        }

        pnlTables.revalidate();
        pnlTables.repaint();
    }

    private JPanel taoChuThich(String text, Color color) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnl.setBackground(new Color(241, 245, 249));
        JLabel box = new JLabel();
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(20, 20));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnl.add(box); pnl.add(lbl);
        return pnl;
    }
}