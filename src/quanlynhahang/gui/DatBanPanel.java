package quanlynhahang.gui;

import quanlynhahang.bus.PhieuDatBanBUS;
import quanlynhahang.dao.PhieuDatBanDAO;
import quanlynhahang.dto.PhieuDatBanDTO;
import quanlynhahang.bus.HoaDonBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Panel quản lý nghiệp vụ Đặt bàn của nhà hàng.
 * Chức năng chính: Quản lý danh sách đặt chỗ, tiếp nhận khách nhận bàn,
 * và xử lý hủy đặt bàn khi có yêu cầu.
 */
public class DatBanPanel extends JPanel {

    /**
     * Model quản lý dữ liệu cho bảng hiển thị phiếu đặt.
     */
    private DefaultTableModel tableModel;

    /**
     * Bảng hiển thị danh sách các phiếu đặt bàn hiện có trong cơ sở dữ liệu.
     */
    private JTable table;

    // --- CÁC THÀNH PHẦN NHẬP LIỆU ---
    private JTextField txtMaDatBan;
    private JTextField txtMaKhachHang;
    private JTextField txtGhiChu;
    private JComboBox<String> cbxMaBan;
    private JComboBox<String> cbxTrangThai;
    private JSpinner spnSoLuong;
    private JSpinner spnThoiGianHen;

    /**
     * Khởi tạo giao diện DatBanPanel với các thành phần nghiệp vụ hoàn chỉnh.
     */
    public DatBanPanel() {
        // Thiết lập Layout chính
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Khu vực Tiêu đề (North)
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ ĐẶT BÀN & LỊCH HẸN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 64, 175));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Khu vực Bảng dữ liệu (Center)
        createTableArea();

        // 3. Khu vực Form nhập liệu và Điều khiển (East)
        createManagementArea();

        // Nạp dữ liệu ban đầu
        loadData();
    }

    /**
     * Thiết lập bảng hiển thị danh sách phiếu đặt.
     */
    private void createTableArea() {
        String[] columns = {"Mã Đặt Bàn", "Mã KH", "Mã Bàn", "Giờ Hẹn", "Số Khách", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên ô
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Đăng ký sự kiện lắng nghe click chọn dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                fillFormFromSelectedRow(row);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Thiết lập khu vực Form chi tiết và các nút xử lý nghiệp vụ.
     */
    private void createManagementArea() {
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setPreferredSize(new Dimension(380, 0));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)), " Thông tin chi tiết "),
                BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));

        // --- FORM FIELDS ---
        JPanel pnlForm = new JPanel(new GridLayout(14, 1, 5, 2));
        pnlForm.setBackground(Color.WHITE);

        txtMaDatBan = new JTextField();
        txtMaKhachHang = new JTextField();
        cbxMaBan = new JComboBox<>(new String[]{"VIP01", "VIP02", "B01", "B02", "B03", "B04", "B05"});

        spnSoLuong = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));

        // Cấu hình Spinner thời gian hẹn
        spnThoiGianHen = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnThoiGianHen, "dd/MM/yyyy HH:mm");
        spnThoiGianHen.setEditor(dateEditor);

        cbxTrangThai = new JComboBox<>(new String[]{"Chờ nhận", "Đã nhận", "Đã hủy"});
        txtGhiChu = new JTextField();

        pnlForm.add(new JLabel("Mã phiếu đặt (Tự sinh nếu trống):")); pnlForm.add(txtMaDatBan);
        pnlForm.add(new JLabel("Mã khách hàng:")); pnlForm.add(txtMaKhachHang);
        pnlForm.add(new JLabel("Vị trí bàn:")); pnlForm.add(cbxMaBan);
        pnlForm.add(new JLabel("Thời điểm nhận bàn:")); pnlForm.add(spnThoiGianHen);
        pnlForm.add(new JLabel("Số lượng khách dự kiến:")); pnlForm.add(spnSoLuong);
        pnlForm.add(new JLabel("Trạng thái phiếu:")); pnlForm.add(cbxTrangThai);
        pnlForm.add(new JLabel("Ghi chú bổ sung:")); pnlForm.add(txtGhiChu);

        pnlRight.add(pnlForm, BorderLayout.NORTH);

        // --- HÀNH ĐỘNG (BUTTONS) ---
        JPanel pnlActions = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlActions.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Tạo phiếu đặt");
        JButton btnCheckIn = new JButton("Khách nhận bàn");
        JButton btnCancel = new JButton("Hủy lịch đặt");
        JButton btnReset = new JButton("Làm mới Form");

        // Styling nút nghiệp vụ quan trọng
        btnCheckIn.setBackground(new Color(34, 197, 94));
        btnCheckIn.setForeground(Color.WHITE);
        btnCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancel.setBackground(new Color(239, 68, 68));
        btnCancel.setForeground(Color.WHITE);

        pnlActions.add(btnAdd);
        pnlActions.add(btnCheckIn);
        pnlActions.add(btnCancel);
        pnlActions.add(btnReset);

        pnlRight.add(pnlActions, BorderLayout.SOUTH);
        add(pnlRight, BorderLayout.EAST);

        // --- ĐĂNG KÝ SỰ KIỆN NÚT BẤM ---

        // Tạo phiếu mới
        btnAdd.addActionListener(e -> handleAddBooking());

        // Nghiệp vụ Check-in
        btnCheckIn.addActionListener(e -> handleCheckIn());

        // Nghiệp vụ Hủy phiếu
        btnCancel.addActionListener(e -> handleCancelBooking());

        // Làm mới
        btnReset.addActionListener(e -> {
            txtMaDatBan.setEditable(true);
            txtMaDatBan.setText("");
            txtMaKhachHang.setText("");
            txtGhiChu.setText("");
            spnSoLuong.setValue(2);
            spnThoiGianHen.setValue(new Date());
            cbxTrangThai.setSelectedIndex(0);
            table.clearSelection();
        });
    }

    /**
     * Truy xuất dữ liệu từ cơ sở dữ liệu và hiển thị lên bảng.
     */
    public void loadData() {
        tableModel.setRowCount(0);
        ArrayList<PhieuDatBanDTO> dsPhieu = new PhieuDatBanDAO().getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (PhieuDatBanDTO p : dsPhieu) {
            tableModel.addRow(new Object[]{
                    p.getMaDatBan(),
                    p.getMaKhachHang(),
                    p.getMaBan(),
                    p.getThoiGianNhanBan() != null ? sdf.format(p.getThoiGianNhanBan()) : "",
                    p.getSoLuongKhach(),
                    p.getTrangThai()
            });
        }
    }

    /**
     * Điền thông tin vào Form từ dòng được chọn trong bảng.
     * @param row Chỉ số dòng được chọn trong JTable
     */
    private void fillFormFromSelectedRow(int row) {
        txtMaDatBan.setText(tableModel.getValueAt(row, 0).toString());
        txtMaKhachHang.setText(tableModel.getValueAt(row, 1).toString());
        cbxMaBan.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        try {
            Date dateHen = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tableModel.getValueAt(row, 3).toString());
            spnThoiGianHen.setValue(dateHen);
        } catch (Exception ex) {
            spnThoiGianHen.setValue(new Date());
        }
        spnSoLuong.setValue(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
        cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        txtMaDatBan.setEditable(false);
    }

    /**
     * Xử lý logic tạo phiếu đặt bàn mới sau khi kiểm tra tính hợp lệ.
     */
    private void handleAddBooking() {
        try {
            String ma = txtMaDatBan.getText().trim();
            if (ma.isEmpty()) ma = "DB" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());

            String kh = txtMaKhachHang.getText().trim();
            if (kh.isEmpty()) {
                JOptionPane.showMessageDialog(this, " Vui lòng cung cấp mã khách hàng!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date hen = (Date) spnThoiGianHen.getValue();
            PhieuDatBanBUS bus = new PhieuDatBanBUS();

            // Kiểm tra ràng buộc thời gian (Tối thiểu 30 phút sau thời điểm hiện tại)
            if (!bus.checkThoiGian(hen)) {
                JOptionPane.showMessageDialog(this, "Thời gian hẹn phải cách hiện tại ít nhất 30 phút!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PhieuDatBanDTO phieu = new PhieuDatBanDTO(
                    ma, kh, cbxMaBan.getSelectedItem().toString(),
                    new Date(), hen, (int) spnSoLuong.getValue(), "Chờ nhận", txtGhiChu.getText().trim()
            );

            if (new PhieuDatBanDAO().insert(phieu)) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                Toast.success(parentFrame, "Đã tạo phiếu đặt bàn thành công!");
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }

    /**
     * Xử lý nghiệp vụ khi khách đến nhận bàn thực tế.
     * Cập nhật trạng thái phiếu và tự động kích hoạt chức năng mở hóa đơn.
     */
    private void handleCheckIn() {
        String ma = txtMaDatBan.getText().trim();
        if (ma.isEmpty()) return;

        String currentStatus = cbxTrangThai.getSelectedItem().toString();
        if (!currentStatus.equalsIgnoreCase("Chờ nhận")) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể nhận bàn cho phiếu đang ở trạng thái 'Chờ nhận'!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận khách đã đến và muốn nhận bàn " + cbxMaBan.getSelectedItem() + "?", "Nhận bàn", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            PhieuDatBanDAO dao = new PhieuDatBanDAO();
            PhieuDatBanDTO phieu = dao.getById(ma);

            if (phieu != null) {
                phieu.setTrangThai("Đã nhận");
                if (dao.update(phieu)) {
                    // MỐI NỐI NGHIỆP VỤ: Tự động mở bàn trên MainFrame
                    MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    HoaDonBUS hdBUS = new HoaDonBUS();

                    if (hdBUS.moBanMoi(phieu.getMaBan(), phieu.getMaKhachHang())) {
                        Toast.success(main, "Xác nhận thành công! Bàn đã mở.");
                        loadData();
                        main.openTable(phieu.getMaBan()); // Nạp bill lên màn hình
                        main.chuyenTrang("Thực đơn");    // Chuyển sang Menu gọi món
                    }
                }
            }
        }
    }

    /**
     * Xử lý nghiệp vụ hủy đặt bàn (Xóa mềm trạng thái).
     */
    private void handleCancelBooking() {
        String ma = txtMaDatBan.getText().trim();
        if (ma.isEmpty()) return;

        int ask = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn hủy phiếu đặt bàn này?", "Hủy đặt", JOptionPane.YES_NO_OPTION);
        if (ask == JOptionPane.YES_OPTION) {
            if (new PhieuDatBanDAO().delete(ma)) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                Toast.success(parentFrame, "Đã hủy phiếu thành công!");
                loadData();
            }
        }
    }
}