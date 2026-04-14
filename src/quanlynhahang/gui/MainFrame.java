package quanlynhahang.gui;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import quanlynhahang.dao.*;
import quanlynhahang.dto.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Khung chính của ứng dụng POS quản lý nhà hàng.
 * Chứa các panel điều hướng, sơ đồ bàn, thực đơn, đặt bàn và khu vực hóa đơn.
 * Phiên bản: Hoàn thiện luồng kết nối Database và Gọi món.
 * Cập nhật UX/UI: Sidebar chuẩn phong cách Flat Mac (Trắng, bo góc mượt, nịnh mắt).
 */
public class MainFrame extends JFrame {

    private static final int DEFAULT_DIVIDER_LOCATION = 850;

    /**
     * Panel tổng chứa các màn hình chức năng (Sơ đồ bàn, Đặt bàn, Thực đơn).
     */
    private JPanel leftCardPanel;

    /**
     * Layout dùng để quản lý và chuyển đổi qua lại giữa các card (màn hình) bên trái.
     */
    private CardLayout leftCardLayout;

    /**
     * Panel hiển thị sơ đồ bàn ăn trực tuyến.
     */
    private SoDoBanPanel soDoBanPanel;

    /**
     * Panel hiển thị thực đơn và các thao tác gọi món.
     */
    private ThucDonPanel thucDonPanel;

    /**
     * Panel quản lý thông tin đặt bàn của khách hàng.
     */
    private DatBanPanel datBanPanel;

    /**
     * Panel cài đặt, chứa các lối vào chức năng quản trị như khách hàng và menu.
     */
    private JPanel caiDatPanel;

    /**
     * Panel quản lý bàn ăn dùng cho thêm, xóa, sửa thuộc tính bàn.
     */
    private QuanLyBanPanel quanLyBanPanel;

    /**
     * Panel thống kê doanh thu theo món.
     */
    private ThongKePanel thongKePanel;

    /**
     * Panel tạo mới khách hàng phục vụ luồng đặt bàn.
     */
    private KhachHangPanel khachHangPanel;

    // --- COMPONENTS BÊN PHẢI (BILL) ---
    /**
     * Nhãn hiển thị tên bàn đang được thao tác hiện tại.
     */
    private JLabel lblTenBanDangChon;

    /**
     * Model quản lý cấu trúc và dữ liệu cho bảng hóa đơn.
     */
    private DefaultTableModel billTableModel;

    /**
     * Bảng hiển thị danh sách các món ăn đã được gọi của bàn hiện tại.
     */
    private JTable billTable;

    /**
     * Danh sách chi tiết hóa đơn đang hiển thị trên bảng bill.
     */
    private ArrayList<ChiTietHoaDonDTO> currentBillItems = new ArrayList<>();

    /**
     * Nhãn hiển thị tổng số tiền của hóa đơn đang mở.
     */
    private JLabel lblTotal;

    /**
     * Nút bấm thực hiện chức năng chốt và thanh toán hóa đơn.
     */
    private JButton btnThanhToan;

    private JSplitPane splitPane;
    private JPanel rightBillPanel;

    // --- BIẾN LƯU TRẠNG THÁI HIỆN TẠI ---
    /**
     * Lưu trữ mã bàn đang được chọn và thao tác trên giao diện.
     */
    protected String currentMaBan = null;

    /**
     * Lưu trữ mã hóa đơn liên kết với bàn đang được chọn (nếu có).
     */
    protected String currentMaHoaDon = null;

    /**
     * SĐT đang chờ xử lý trong luồng thanh toán sau khi tạo khách hàng mới.
     */
    private String pendingCheckoutPhone = null;

    /**
     * Khởi tạo giao diện MainFrame.
     * Thiết lập các thuộc tính cơ bản của cửa sổ và chia layout làm hai phần chính
     * (Nghiệp vụ bên trái và Hóa đơn bên phải).
     */
    public MainFrame() {
        setTitle("Hệ Thống Quản Lý Nhà Hàng - TUN BBQ");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sử dụng SplitPane để phân tách khu vực chức năng và khu vực Bill
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(850);
        splitPane.setDividerSize(3); // Thu nhỏ vách ngăn cho tinh tế
        splitPane.setBorder(null); // Bỏ viền mặc định của SplitPane

        // Khởi tạo các panel nghiệp vụ
        soDoBanPanel = new SoDoBanPanel();
        thucDonPanel = new ThucDonPanel();
        datBanPanel = new DatBanPanel();
        khachHangPanel = new KhachHangPanel();
        quanLyBanPanel = new QuanLyBanPanel();
        thongKePanel = new ThongKePanel();
        caiDatPanel = createCaiDatPanel();
        khachHangPanel.setMainFrame(this);

        splitPane.setLeftComponent(createLeftContentPanel());
        rightBillPanel = createRightBillPanel();
        splitPane.setRightComponent(rightBillPanel);

        add(splitPane, BorderLayout.CENTER);

        // Mặc định ẩn bill, chỉ hiện khi đã vào luồng order của một bàn có hóa đơn.
        capNhatHienThiBill(false);
    }

    // =========================================
    // NỬA BÊN TRÁI (SƠ ĐỒ BÀN / THỰC ĐƠN / ĐẶT BÀN)
    // =========================================

    /**
     * Tạo khu vực bên trái của giao diện, bao gồm thanh điều hướng Sidebar
     * và khu vực hiển thị nội dung chính (Card Panel).
     *
     * @return JPanel chứa cấu trúc khu vực bên trái đã hoàn thiện.
     */
    private JPanel createLeftContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // --- SIDEBAR ĐIỀU HƯỚNG BÊN TRÁI (UI/UX OPTIMIZED) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        // Màu nền xám cực nhạt, tạo cảm giác thanh lịch
        sidebar.setBackground(new Color(248, 250, 252));
        // Tạo một đường viền xám mờ bên phải để phân tách với nội dung
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 0, 1, new Color(226, 232, 240)),
                new EmptyBorder(25, 12, 25, 12)
        ));
        sidebar.setPreferredSize(new Dimension(190, 0)); // Mở rộng để nút bấm rộng rãi hơn

        // --- Logo / Thương hiệu ---
        JLabel lblBrand = new JLabel("TUN BBQ");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBrand.setForeground(new Color(15, 23, 42)); // Đen tuyền sang trọng
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("RESTAURANT SYSTEM");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(lblBrand);
        sidebar.add(lblSub);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40))); // Khoảng cách tới nút đầu tiên

        // --- Nút điều hướng ---
        JButton btnPhongBan = createSidebarButton("Sơ đồ bàn");
        JButton btnDatBan = createSidebarButton("Đặt bàn");
        JButton btnThucDon = createSidebarButton("Gọi món");
        JButton btnThongKe = createSidebarButton("Thống kê");
        JButton btnCaiDat = createSidebarButton("Cài đặt");

        sidebar.add(btnPhongBan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnDatBan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnThucDon);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnThongKe);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnCaiDat);
        sidebar.add(Box.createVerticalGlue()); // Đẩy tất cả lên trên

        panel.add(sidebar, BorderLayout.WEST);

        // --- KHU VỰC HIỂN THỊ CHÍNH (CENTER) ---
        leftCardLayout = new CardLayout();
        leftCardPanel = new JPanel(leftCardLayout);
        leftCardPanel.setBackground(Color.WHITE); // Đảm bảo đồng bộ màu

        // Gán mã card cho các panel con
        leftCardPanel.add(soDoBanPanel, "Phòng bàn");
        leftCardPanel.add(datBanPanel, "Đặt bàn");
        leftCardPanel.add(thucDonPanel, "Thực đơn");
        leftCardPanel.add(thongKePanel, "Thống kê");
        leftCardPanel.add(caiDatPanel, "Cài đặt");
        leftCardPanel.add(quanLyBanPanel, "Quản lý bàn");
        leftCardPanel.add(khachHangPanel, "Khách hàng");

        panel.add(leftCardPanel, BorderLayout.CENTER);

        // Đăng ký sự kiện chuyển card cho các nút Sidebar
        btnPhongBan.addActionListener(e -> chuyenTrang("Phòng bàn"));
        btnDatBan.addActionListener(e -> chuyenTrang("Đặt bàn"));
        btnThucDon.addActionListener(e -> {
            thucDonPanel.setQuanLyMenuMode(false);
            chuyenTrang("Thực đơn");
        });
        btnThongKe.addActionListener(e -> {
            thongKePanel.loadData();
            chuyenTrang("Thống kê");
        });
        btnCaiDat.addActionListener(e -> chuyenTrang("Cài đặt"));

        return panel;
    }

    /**
     * Tạo màn Cài đặt để gom các chức năng quản trị hệ thống.
     * Bao gồm lối vào Quản lý khách hàng và Quản lý menu.
     */
    private JPanel createCaiDatPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(241, 245, 249));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("CÀI ĐẶT HỆ THỐNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 64, 175));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        content.setOpaque(false);

        JButton btnKhachHang = createSettingActionButton("QL khách hàng");
        btnKhachHang.addActionListener(e -> {
            khachHangPanel.batCheDoQuanLy();
            chuyenTrang("Khách hàng");
        });

        JButton btnMenu = createSettingActionButton("QL menu");
        btnMenu.addActionListener(e -> {
            thucDonPanel.setQuanLyMenuMode(true);
            chuyenTrang("Thực đơn");
        });

        JButton btnQuanLyBan = createSettingActionButton("QL bàn");
        btnQuanLyBan.addActionListener(e -> {
            quanLyBanPanel.loadData();
            chuyenTrang("Quản lý bàn");
        });

        content.add(btnKhachHang);
        content.add(btnMenu);
        content.add(btnQuanLyBan);
        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private JButton createSettingActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(130, 36));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(51, 65, 85));
        btn.putClientProperty("JButton.buttonType", "square");
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Hàm hỗ trợ tạo cấu hình giao diện cho các nút bấm nằm trên Sidebar.
     * Căn lề trái, bỏ viền, cấu hình font chữ và bo góc chuẩn Mac.
     *
     * @param text Tên hiển thị của nút bấm (có thể kèm emoji).
     * @return JButton Đối tượng nút bấm đã được cấu hình hoàn chỉnh.
     */
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(new Color(51, 65, 85)); // Màu xám slate đậm, không bị chói như đen
        btn.setBackground(Color.WHITE);

        // Căn lề trái để chữ thẳng hàng, nhìn gọn gàng hơn
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));

        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 48));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dùng FlatLaf để bo góc mềm mại cho nút
        btn.putClientProperty("JButton.buttonType", "roundRect");

        return btn;
    }

    // =========================================
    // NỬA BÊN PHẢI (KHU VỰC HÓA ĐƠN - BILL)
    // =========================================

    /**
     * Tạo khu vực hiển thị hóa đơn cố định ở bên phải màn hình.
     * Gồm thông tin bàn hiện tại, bảng chi tiết gọi món và tổng cộng thanh toán.
     *
     * @return JPanel chứa cấu trúc khu vực hiển thị Bill.
     */
    private JPanel createRightBillPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // 1. Header Bill
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        lblTenBanDangChon = new JLabel("BÀN: CHƯA CHỌN");
        lblTenBanDangChon.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTenBanDangChon.setForeground(new Color(15, 23, 42));

        JLabel lblUser = new JLabel("Nhân viên: Thu ngân 1");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(100, 116, 139));

        headerPanel.add(lblTenBanDangChon, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // 2. Center: Bảng hiển thị danh sách món
        String[] columns = {"Món ăn", "SL", "Đơn giá", "Thành tiền"};
        billTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        billTable = new JTable(billTableModel);
        billTable.setRowHeight(40); // Cho chiều cao dòng rộng rãi dễ nhìn
        billTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        billTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        billTable.setShowVerticalLines(false); // Ẩn viền dọc bảng theo style hiện đại
        billTable.setGridColor(new Color(241, 245, 249));

        // Căn lề
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        billTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(0).setPreferredWidth(170);

        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // 3. Footer: Tổng cộng và Nhóm nút chức năng
        JPanel footerPanel = new JPanel(new BorderLayout(0, 15));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        lblTotal = new JLabel("Tổng cộng: 0 VNĐ", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTotal.setForeground(new Color(220, 38, 38)); // Đỏ nổi bật
        footerPanel.add(lblTotal, BorderLayout.NORTH);

        JPanel btnGroup = new JPanel(new GridLayout(1, 3, 15, 0));
        btnGroup.setOpaque(false);

        JButton btnGiamMon = new JButton("XÓA 1 PHẦN");
        btnGiamMon.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGiamMon.setBackground(new Color(251, 146, 60));
        btnGiamMon.setForeground(Color.WHITE);
        btnGiamMon.putClientProperty("JButton.buttonType", "roundRect");
        btnGiamMon.addActionListener(e -> xuLyGiamMonTrongBill());

        JButton btnReload = new JButton("LÀM MỚI BILL");
        btnReload.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnReload.putClientProperty("JButton.buttonType", "roundRect");
        btnReload.addActionListener(e -> {
            if (currentMaBan != null) openTable(currentMaBan);
        });

        btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnThanhToan.setBackground(new Color(34, 197, 94)); // Màu xanh lá Success
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.putClientProperty("JButton.buttonType", "roundRect");
        btnThanhToan.addActionListener(e -> xuLyThanhToan());

        btnGroup.add(btnGiamMon);
        btnGroup.add(btnReload);
        btnGroup.add(btnThanhToan);
        btnGroup.setPreferredSize(new Dimension(0, 55)); // Nút to bấm cho dễ

        footerPanel.add(btnGroup, BorderLayout.SOUTH);
        panel.add(footerPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================
    // HÀM XỬ LÝ LOGIC NGHIỆP VỤ (KẾT NỐI DATA)
    // =========================================

    /**
     * Mở và tải dữ liệu hóa đơn của một bàn cụ thể lên giao diện Bill bên phải.
     * Tự động tính toán lại tổng tiền dựa trên các món ăn đang có trong bàn.
     *
     * @param maBan Mã của bàn cần xem/thao tác hóa đơn.
     */
    public void openTable(String maBan) {
        this.currentMaBan = maBan;
        this.currentMaHoaDon = null;

        BanAnDTO ban = new BanAnDAO().getById(maBan);
        if (ban != null) {
            lblTenBanDangChon.setText("BÀN ĐANG CHỌN: " + ban.getTenBan());
        }
        billTableModel.setRowCount(0);
        currentBillItems = new ArrayList<>();
        lblTotal.setText("Tổng cộng: 0 VNĐ");

        ThongKeDAO tkDAO = new ThongKeDAO();
        ChiTietBanDangDungDTO dangAn = tkDAO.getChiTietBanDangAn(maBan);
        if (dangAn != null) {
            currentMaHoaDon = dangAn.getMaHoaDon();
            lblTotal.setText(String.format("Tổng cộng: %,.3f VNĐ", dangAn.getThanhTienTamTinh()));

            currentBillItems = new ChiTietHoaDonDAO().getByHoaDonId(currentMaHoaDon);
            for (ChiTietHoaDonDTO ct : currentBillItems) {
                MonAnDTO mon = new MonAnDAO().getById(ct.getMaMon());
                String tenMon = (mon != null) ? mon.getTenMon() : ct.getMaMon();
                double thanhTien = ct.getSoLuong() * ct.getGiaBan();

                billTableModel.addRow(new Object[]{
                        tenMon,
                        ct.getSoLuong(),
                        String.format("%,.3f", ct.getGiaBan()),
                        String.format("%,.3f", thanhTien)
                });
            }
        }

        capNhatHienThiBill(currentMaHoaDon != null && "Thực đơn".equals(getCardDangHienThi()));
    }

    /**
     * Giảm số lượng của món đang được chọn trong bill đi 1 phần.
     * Nếu món chỉ còn 1 phần thì dòng đó sẽ bị xóa khỏi hóa đơn.
     */
    private void xuLyGiamMonTrongBill() {
        if (currentMaHoaDon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng mở bàn trước khi xóa món!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = billTable.getSelectedRow();
        if (row < 0 || row >= currentBillItems.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa bớt trong bill!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChiTietHoaDonDTO ct = currentBillItems.get(row);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa bớt 1 phần món '" + ct.getMaMon() + "' khỏi hóa đơn?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (new ChiTietHoaDonDAO().giamSoLuongMonTrongHoaDon(currentMaHoaDon, ct.getMaMon(), 1)) {
            openTable(currentMaBan);
            Toast.success(this, "Đã giảm 1 phần món khỏi hóa đơn");
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xóa món khỏi hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Thực hiện thêm một món ăn mới vào hóa đơn của bàn đang được chọn hiện tại.
     * Sau khi thêm thành công, hàm sẽ gọi lại openTable để tự động tải lại chi tiết bảng hóa đơn.
     *
     * @param maMon Mã món ăn cần gọi.
     * @param soLuong Số lượng gọi (thường mặc định là 1 khi bấm nút).
     * @param giaBan Đơn giá của món ăn tại thời điểm gọi.
     */
    public void themMonVaoBill(String maMon, int soLuong, double giaBan) {
        if (currentMaHoaDon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng mở bàn hoặc nhận bàn trước khi gọi món!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();
        boolean thanhCong = ctDAO.themMonVaoHoaDon(currentMaHoaDon, maMon, soLuong, giaBan, "");

        if (thanhCong) {
            openTable(currentMaBan);
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm món vào hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xử lý nghiệp vụ thanh toán (Checkout) hóa đơn đang mở trên màn hình.
     * Sau khi thanh toán, hệ thống sẽ gọi BUS để hoàn tất giao dịch, dọn bàn và làm mới giao diện.
     */
    private void xuLyThanhToan() {
        if (currentMaHoaDon == null) {
            JOptionPane.showMessageDialog(this, "Bàn này hiện chưa có hóa đơn để thanh toán!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextField txtSoDienThoai = new JTextField();
        String soDienThoaiMacDinh = laySoDienThoaiKhachTrongHoaDonHienTai();
        if (soDienThoaiMacDinh != null && !soDienThoaiMacDinh.isEmpty()) {
            txtSoDienThoai.setText(soDienThoaiMacDinh);
        }
        JLabel lblHuongDan = new JLabel("Tự điền SĐT nếu bàn đã gắn khách. Để trống nếu khách vãng lai.");

        JPanel pnlNhap = new JPanel(new BorderLayout(0, 8));
        JPanel pnlForm = new JPanel(new GridLayout(2, 1, 0, 6));
        pnlForm.add(new JLabel("Số điện thoại khách hàng:"));
        pnlForm.add(txtSoDienThoai);
        pnlNhap.add(lblHuongDan, BorderLayout.NORTH);
        pnlNhap.add(pnlForm, BorderLayout.CENTER);

        int nhapKetQua = JOptionPane.showConfirmDialog(
                this,
                pnlNhap,
                "Thanh toán - Thông tin khách hàng",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (nhapKetQua != JOptionPane.OK_OPTION) {
            return;
        }

        String soDienThoai = txtSoDienThoai.getText().trim();

        if (!soDienThoai.isEmpty() && !new quanlynhahang.bus.KhachHangBUS().validatePhone(soDienThoai)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (cần đúng 10 chữ số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!soDienThoai.isEmpty()) {
            KhachHangDAO khDAO = new KhachHangDAO();
            if (khDAO.getById(soDienThoai) == null) {
                pendingCheckoutPhone = soDienThoai;
                moManKhachHangTuSDTChoThanhToan(soDienThoai);
                return;
            }
        }

        thucHienThanhToanVoiSoDienThoai(soDienThoai);
    }

    /**
     * Thực hiện phần thanh toán còn lại sau khi đã có SĐT khách hàng.
     * @param soDienThoai số điện thoại khách hàng, có thể rỗng
     */
    private void thucHienThanhToanVoiSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            soDienThoai = laySoDienThoaiKhachTrongHoaDonHienTai();
        }

        quanlynhahang.bus.HoaDonBUS hdBUS = new quanlynhahang.bus.HoaDonBUS();
        if (!hdBUS.capNhatGiamGiaTheoSoDienThoai(currentMaHoaDon, soDienThoai)) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng theo số điện thoại hoặc không thể áp dụng giảm giá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HoaDonDTO hd = new HoaDonDAO().getById(currentMaHoaDon);
        String noiDungXacNhan = "Xác nhận thanh toán cho " + currentMaBan + "?";
        if (hd != null) {
            noiDungXacNhan = String.format(
                    "Xác nhận thanh toán cho %s?\n\nTổng tiền: %,.0f VNĐ\nGiảm giá: %,.0f VNĐ\nThành tiền: %,.0f VNĐ",
                    currentMaBan,
                    hd.getTongTien(),
                    hd.getTienGiamGia(),
                    hd.getThanhTien()
            );
        }

        int confirm = JOptionPane.showConfirmDialog(this, noiDungXacNhan, "Thanh toán", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (hdBUS.thanhToanHoaDon(currentMaHoaDon)) {
                Toast.success(this, "Thanh toán thành công! Bàn đã được dọn.");
                soDoBanPanel.loadSoDoBan();
                openTable(currentMaBan);
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thực hiện thanh toán. Vui lòng kiểm tra lại!", "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lấy số điện thoại khách hàng đang gắn với hóa đơn hiện tại (nếu có).
     * @return số điện thoại khách hàng hoặc chuỗi rỗng khi không có
     */
    private String laySoDienThoaiKhachTrongHoaDonHienTai() {
        if (currentMaHoaDon == null) {
            return "";
        }

        HoaDonDTO hd = new HoaDonDAO().getById(currentMaHoaDon);
        if (hd == null || hd.getMaKhachHang() == null || hd.getMaKhachHang().trim().isEmpty()) {
            return "";
        }

        KhachHangDTO kh = new KhachHangDAO().getById(hd.getMaKhachHang());
        if (kh == null || kh.getSoDienThoai() == null) {
            return "";
        }

        return kh.getSoDienThoai().trim();
    }

    /**
     * Tiện ích giúp chuyển đổi giữa các màn hình bên trái từ các component con (như ThucDonPanel).
     *
     * @param name Mã String của panel cần hiển thị (vd: "Phòng bàn", "Thực đơn").
     */
    public void chuyenTrang(String name) {
        if (!"Thực đơn".equals(name)) {
            capNhatHienThiBill(false);
        } else {
            capNhatHienThiBill(currentMaHoaDon != null);
        }
        leftCardLayout.show(leftCardPanel, name);
    }

    private String getCardDangHienThi() {
        for (Component component : leftCardPanel.getComponents()) {
            if (component.isVisible()) {
                if (component == thucDonPanel) return "Thực đơn";
                if (component == soDoBanPanel) return "Phòng bàn";
                if (component == datBanPanel) return "Đặt bàn";
                if (component == caiDatPanel) return "Cài đặt";
                if (component == quanLyBanPanel) return "Quản lý bàn";
                if (component == khachHangPanel) return "Khách hàng";
                if (component == thongKePanel) return "Thống kê";
            }
        }
        return "";
    }

    private void capNhatHienThiBill(boolean hienThi) {
        if (splitPane == null || rightBillPanel == null) {
            return;
        }

        rightBillPanel.setVisible(hienThi);
        splitPane.setDividerSize(hienThi ? 3 : 0);

        if (hienThi) {
            splitPane.setDividerLocation(DEFAULT_DIVIDER_LOCATION);
        } else {
            splitPane.setDividerLocation(1.0);
        }

        splitPane.revalidate();
        splitPane.repaint();
    }

    /**
     * Làm mới sơ đồ bàn để cập nhật màu và trạng thái mới nhất.
     */
    public void refreshSoDoBan() {
        soDoBanPanel.loadSoDoBan();
    }

    /**
     * Mở màn hình đặt bàn và chọn sẵn bàn cần đặt.
     * @param maBan mã bàn muốn đặt trước
     */
    public void moManDatBanChoBan(String maBan) {
        datBanPanel.chuanBiDatBanChoBan(maBan);
        chuyenTrang("Đặt bàn");
    }

    /**
     * Mở trang tạo khách hàng và điền trước số điện thoại.
     * @param soDienThoai số điện thoại khách cần tạo
     */
    public void moManKhachHangTuSDT(String soDienThoai) {
        pendingCheckoutPhone = null;
        khachHangPanel.chuanBiThemKhachMoi(soDienThoai);
        khachHangPanel.setChuyenVeThanhToan(false);
        chuyenTrang("Khách hàng");
    }

    /**
     * Mở trang tạo khách hàng từ luồng thanh toán và ghi nhớ SĐT đang chờ.
     * @param soDienThoai số điện thoại cần tạo
     */
    public void moManKhachHangTuSDTChoThanhToan(String soDienThoai) {
        pendingCheckoutPhone = soDienThoai != null ? soDienThoai.trim() : null;
        khachHangPanel.chuanBiThemKhachMoi(soDienThoai);
        khachHangPanel.setChuyenVeThanhToan(true);
        chuyenTrang("Khách hàng");
    }

    /**
     * Tiếp tục thanh toán sau khi vừa tạo xong khách hàng mới.
     * @param soDienThoai số điện thoại khách vừa tạo
     */
    public void tiepTucThanhToanSauKhiTaoKhach(String soDienThoai) {
        String phone = soDienThoai != null ? soDienThoai.trim() : "";
        if (phone.isEmpty() && pendingCheckoutPhone != null) {
            phone = pendingCheckoutPhone;
        }
        pendingCheckoutPhone = null;
        if (phone.isEmpty() || !new quanlynhahang.bus.KhachHangBUS().validatePhone(phone)) {
            return;
        }
        thucHienThanhToanVoiSoDienThoai(phone);
    }

    /**
     * Quay lại màn đặt bàn và điền lại số điện thoại khách.
     * @param soDienThoai số điện thoại khách
     */
    public void quayLaiDatBanVoiSoDienThoai(String soDienThoai) {
        datBanPanel.datSoDienThoaiKhach(soDienThoai);
        chuyenTrang("Đặt bàn");
    }

    /**
     * Trả về thực thể MainFrame hiện tại, giúp các Panel con dễ dàng giao tiếp và gọi hàm của MainFrame.
     *
     * @return Đối tượng MainFrame hiện tại.
     */
    public MainFrame getOrderPanel() {
        return this;
    }

    /**
     * Điểm bắt đầu (Entry point) của ứng dụng POS Quán Nướng.
     * Áp dụng UI FlatMacLightLaf toàn cục trước khi khởi tạo giao diện.
     *
     * @param args Tham số đầu vào dòng lệnh (không sử dụng).
     */
    public static void main(String[] args) {
        try {
            // Khởi tạo Look and Feel chuẩn
            FlatMacLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        } catch( Exception ex ) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}