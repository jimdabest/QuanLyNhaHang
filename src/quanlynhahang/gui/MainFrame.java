package quanlynhahang.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import quanlynhahang.dao.*;
import quanlynhahang.dto.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Khung chính của ứng dụng POS quản lý nhà hàng.
 * Chứa các panel điều hướng, sơ đồ bàn, thực đơn, đặt bàn và khu vực hóa đơn.
 */
public class MainFrame extends JFrame {

    /**
     * Panel tổng chứa các màn hình Bàn, Đặt bàn, Thực đơn.
     */
    private JPanel leftCardPanel;
    /**
     * Layout dùng để chuyển đổi các card trong bên trái.
     */
    private CardLayout leftCardLayout;
    private SoDoBanPanel soDoBanPanel;
    private ThucDonPanel thucDonPanel;
    private DatBanPanel datBanPanel;

    // --- COMPONENTS BÊN PHẢI (BILL) ---
    /**
     * Nhãn hiển thị tên bàn đang chọn.
     */
    private JLabel lblTenBanDangChon;
    private DefaultTableModel billTableModel;
    private JTable billTable;
    private JLabel lblTotal;
    private JButton btnThanhToan;

    // --- BIẾN LƯU TRẠNG THÁI HIỆN TẠI ---
    /**
     * Mã bàn đang mở trong khu vực hóa đơn.
     */
    private String currentMaBan = null;
    /**
     * Mã hóa đơn đang được thao tác trong khu vực hóa đơn.
     */
    private String currentMaHoaDon = null;

    public MainFrame() {
        setTitle("Hệ Thống Quản Lý POS - Đã nối Database");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(850);
        splitPane.setDividerSize(3);

        soDoBanPanel = new SoDoBanPanel();
        thucDonPanel = new ThucDonPanel();
        datBanPanel = new DatBanPanel();

        splitPane.setLeftComponent(createLeftContentPanel());
        splitPane.setRightComponent(createRightBillPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    // =========================================
    // NỬA BÊN TRÁI (SƠ ĐỒ BÀN / THỰC ĐƠN)
    // =========================================
    /**
     * Tạo nửa trái của giao diện chứa các tab Sơ đồ bàn, Đặt bàn và Thực đơn.
     * @return JPanel chứa nội dung điều hướng bên trái
     */
    private JPanel createLeftContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton btnPhongBan = new JButton("SƠ ĐỒ BÀN");
        JButton btnDatBan = new JButton("ĐẶT BÀN"); // <-- THÊM NÚT NÀY
        JButton btnThucDon = new JButton("THỰC ĐƠN");

        Font tabFont = new Font("Segoe UI", Font.BOLD, 14);
        btnPhongBan.setFont(tabFont);
        btnDatBan.setFont(tabFont); // <-- SET FONT CHO NÚT
        btnThucDon.setFont(tabFont);

        toolBar.add(btnPhongBan);
        toolBar.addSeparator(new Dimension(10, 0));
        toolBar.add(btnDatBan); // <-- ADD NÚT VÀO TOOLBAR
        toolBar.addSeparator(new Dimension(10, 0)); // <-- KHOẢNG CÁCH DẤU GẠCH CHIA
        toolBar.add(btnThucDon);

        panel.add(toolBar, BorderLayout.NORTH);

        leftCardLayout = new CardLayout();
        leftCardPanel = new JPanel(leftCardLayout);
        leftCardPanel.add(soDoBanPanel, "Phòng bàn");
        leftCardPanel.add(datBanPanel, "Đặt bàn"); // <-- ADD PANEL VÀO CARDLAYOUT
        leftCardPanel.add(thucDonPanel, "Thực đơn");

        panel.add(leftCardPanel, BorderLayout.CENTER);

        // --- GẮN SỰ KIỆN CHUYỂN TRANG ---
        btnPhongBan.addActionListener(e -> leftCardLayout.show(leftCardPanel, "Phòng bàn"));
        btnDatBan.addActionListener(e -> leftCardLayout.show(leftCardPanel, "Đặt bàn")); // <-- SỰ KIỆN CLICK CHUYỂN SANG ĐẶT BÀN
        btnThucDon.addActionListener(e -> leftCardLayout.show(leftCardPanel, "Thực đơn"));

        return panel;
    }

    // =========================================
    // NỬA BÊN PHẢI (BILL - HÓA ĐƠN)
    // =========================================
    /**
     * Tạo nửa phải của giao diện hiển thị hóa đơn và nút thanh toán.
     * @return JPanel chứa khu vực bill
     */
    private JPanel createRightBillPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 15, 15));

        // 1. Header Bill
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        lblTenBanDangChon = new JLabel("BÀN: CHƯA CHỌN");
        lblTenBanDangChon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTenBanDangChon.setForeground(new Color(0, 122, 255));

        JLabel lblUser = new JLabel("Nhân viên: Thu ngân 1");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        headerPanel.add(lblTenBanDangChon, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // 2. Bảng hiển thị Món ăn
        String[] columns = {"Món ăn", "SL", "Đơn giá", "Thành tiền"};
        billTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        billTable = new JTable(billTableModel);
        billTable.setRowHeight(35);
        billTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        billTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(0).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(billTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 3. Footer (Tổng tiền & Nút Action)
        JPanel footerPanel = new JPanel(new BorderLayout(0, 15));
        footerPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        lblTotal = new JLabel("Tổng cộng: 0 VNĐ", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(new Color(40, 167, 69));
        footerPanel.add(lblTotal, BorderLayout.NORTH);

        JPanel btnGroup = new JPanel(new GridLayout(1, 2, 15, 0));

        JButton btnLuu = new JButton("LÀM MỚI BILL");
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLuu.addActionListener(e -> {
            if (currentMaBan != null) openTable(currentMaBan); // Load lại bill
        });

        btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThanhToan.setBackground(new Color(0, 122, 255));
        btnThanhToan.setForeground(Color.WHITE);

        // --- SỰ KIỆN THANH TOÁN (KẾT NỐI DATABASE) ---
        btnThanhToan.addActionListener(e -> xuLyThanhToan());

        btnGroup.add(btnLuu);
        btnGroup.add(btnThanhToan);
        btnGroup.setPreferredSize(new Dimension(0, 45));

        footerPanel.add(btnGroup, BorderLayout.SOUTH);
        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================
    // HÀM XỬ LÝ LOGIC CHÍNH (THỰC SỰ HOẠT ĐỘNG)
    // =========================================

    /**
     * Mở bàn và nạp thông tin hóa đơn tương ứng lên bảng bill.
     * @param maBan mã bàn được chọn
     */
    public void openTable(String maBan) {
        this.currentMaBan = maBan;
        this.currentMaHoaDon = null;

        // Cập nhật tên bàn lên Header
        BanAnDTO ban = new BanAnDAO().getById(maBan);
        if (ban != null) {
            lblTenBanDangChon.setText("BÀN ĐANG CHỌN: " + ban.getTenBan());
        }

        // Xóa sạch Bill cũ trên màn hình
        billTableModel.setRowCount(0);
        lblTotal.setText("Tổng cộng: 0 VNĐ");

        // Gọi DB kiểm tra xem bàn này có đang ăn (có Hóa Đơn) không?
        ThongKeDAO tkDAO = new ThongKeDAO();
        ChiTietBanDangDungDTO dangAn = tkDAO.getChiTietBanDangAn(maBan);

        if (dangAn != null) {
            // Lưu lại mã hóa đơn để lát nữa thanh toán
            currentMaHoaDon = dangAn.getMaHoaDon();

            // Cập nhật tổng tiền (đã format dấu phẩy)
            lblTotal.setText(String.format("Tổng cộng: %,.0f VNĐ", dangAn.getThanhTienTamTinh()));

            // Lấy danh sách các món ăn của hóa đơn này
            ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();
            MonAnDAO monDAO = new MonAnDAO();
            ArrayList<ChiTietHoaDonDTO> listMon = ctDAO.getByHoaDonId(currentMaHoaDon);

            // Đổ món ăn vào JTable
            for (ChiTietHoaDonDTO ct : listMon) {
                // Phải Query tên món vì ctDAO chỉ lưu MaMon
                MonAnDTO mon = monDAO.getById(ct.getMaMon());
                String tenMon = (mon != null) ? mon.getTenMon() : ct.getMaMon();

                double thanhTien = ct.getSoLuong() * ct.getGiaBan();

                billTableModel.addRow(new Object[]{
                        tenMon,
                        ct.getSoLuong(),
                        String.format("%,.0f", ct.getGiaBan()),
                        String.format("%,.0f", thanhTien)
                });
            }
        }
    }

    /**
     * Xử lý nghiệp vụ thanh toán khi nhân viên nhấn nút THANH TOÁN.
     * Gồm kiểm tra trạng thái hóa đơn và gọi BUS để chốt bill.
     */
    private void xuLyThanhToan() {
        if (currentMaHoaDon == null) {
            JOptionPane.showMessageDialog(this, "Bàn này đang trống hoặc chưa gọi món!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        quanlynhahang.bus.HoaDonBUS hdBUS = new quanlynhahang.bus.HoaDonBUS();

        // 1. Kiểm tra trạng thái hóa đơn từ BUS xem có bị thanh toán trùng không
        if (!hdBUS.checkBillStatus(currentMaHoaDon)) {
            JOptionPane.showMessageDialog(this, "Hóa đơn này không hợp lệ hoặc đã được thanh toán rồi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận in bill và thanh toán cho " + currentMaBan + "?",
                "Thanh toán", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // 2. Chốt bill thông qua BUS thay vì chọc thẳng DAO
            if (hdBUS.thanhToanHoaDon(currentMaHoaDon)) {
                JOptionPane.showMessageDialog(this, "✅ Thanh toán thành công! Bàn đã được dọn.");

                // Refresh lại sơ đồ bàn (Bàn sẽ đổi về màu xanh lá)
                soDoBanPanel.loadSoDoBan();

                // Clear bill trên màn hình
                openTable(currentMaBan);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Lỗi hệ thống, không thể thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================
    // API KẾT NỐI GIỮA CÁC PANEL
    // =========================================
    /**
     * Chuyển sang một tab trong vùng card bên trái.
     * @param name tên card cần hiển thị
     */
    public void chuyenTrang(String name) {
        leftCardLayout.show(leftCardPanel, name);
    }

    /**
     * Trả về tham chiếu MainFrame cho các panel con khi cần gọi lại hàm.
     * @return đối tượng MainFrame hiện tại
     */
    public MainFrame getOrderPanel() {
        return this;
    }

    // =========================================
    // HÀM KHỞI CHẠY
    // =========================================
    /**
     * Điểm vào của ứng dụng POS.
     * Thiết lập giao diện phẳng và khởi tạo cửa sổ chính.
     * @param args tham số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        try {
            FlatMacLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        } catch( Exception ex ) {}

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}