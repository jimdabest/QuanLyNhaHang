package quanlynhahang.gui;

import quanlynhahang.bus.KhachHangBUS;
import quanlynhahang.bus.PhieuDatBanBUS;
import quanlynhahang.dao.KhachHangDAO;
import quanlynhahang.dao.PhieuDatBanDAO;
import quanlynhahang.dto.KhachHangDTO;
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
    private JComboBox<String> cbxBoLocPhieu;

    /**
     * Bảng hiển thị danh sách các phiếu đặt bàn hiện có trong cơ sở dữ liệu.
     */
    private JTable table;

    // --- CÁC THÀNH PHẦN NHẬP LIỆU ---
    private JTextField txtSoDienThoaiKhach;
    private JTextField txtGhiChu;
    private JComboBox<String> cbxMaBan;
    private JComboBox<String> cbxTrangThai;
    private JSpinner spnSoLuong;
    private JSpinner spnThoiGianHen;
    private String selectedMaDatBan;

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
        JPanel pnlTableArea = new JPanel(new BorderLayout(8, 8));
        pnlTableArea.setBackground(getBackground());

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFilter.setBackground(getBackground());
        pnlFilter.add(new JLabel("Hiển thị: "));
        cbxBoLocPhieu = new JComboBox<>(new String[]{"Phiếu hôm nay", "Tất cả phiếu"});
        cbxBoLocPhieu.setSelectedIndex(0);
        cbxBoLocPhieu.addActionListener(e -> loadData());
        pnlFilter.add(cbxBoLocPhieu);

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

        pnlTableArea.add(pnlFilter, BorderLayout.NORTH);
        pnlTableArea.add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnlTableArea, BorderLayout.CENTER);
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
        JPanel pnlForm = new JPanel(new GridLayout(12, 1, 5, 2));
        pnlForm.setBackground(Color.WHITE);

        txtSoDienThoaiKhach = new JTextField();
        JButton btnChonKhachHang = new JButton("Chọn khách...");
        JPanel pnlKhachHang = new JPanel(new BorderLayout(5, 0));
        pnlKhachHang.setBackground(Color.WHITE);
        pnlKhachHang.add(txtSoDienThoaiKhach, BorderLayout.CENTER);
        pnlKhachHang.add(btnChonKhachHang, BorderLayout.EAST);
        cbxMaBan = new JComboBox<>(new String[]{"VIP01", "VIP02", "B01", "B02", "B03", "B04", "B05"});

        spnSoLuong = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));

        // Cấu hình Spinner thời gian hẹn
        spnThoiGianHen = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnThoiGianHen, "dd/MM/yyyy HH:mm");
        spnThoiGianHen.setEditor(dateEditor);

        cbxTrangThai = new JComboBox<>(new String[]{"Chờ nhận", "Đã nhận", "Đã hủy"});
        txtGhiChu = new JTextField();

        pnlForm.add(new JLabel("Số điện thoại khách:")); pnlForm.add(pnlKhachHang);
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

        // Chọn khách hàng từ danh sách
        btnChonKhachHang.addActionListener(e -> moDialogChonKhachHang());

        // Nghiệp vụ Check-in
        btnCheckIn.addActionListener(e -> handleCheckIn());

        // Nghiệp vụ Hủy phiếu
        btnCancel.addActionListener(e -> handleCancelBooking());

        // Làm mới
        btnReset.addActionListener(e -> {
            selectedMaDatBan = null;
            txtSoDienThoaiKhach.setText("");
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
        boolean chiHomNay = cbxBoLocPhieu == null || cbxBoLocPhieu.getSelectedIndex() == 0;
        ArrayList<PhieuDatBanDTO> dsPhieu = chiHomNay
            ? new PhieuDatBanDAO().getPhieuDatBanHomNay()
            : new PhieuDatBanDAO().getAll();
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
        selectedMaDatBan = tableModel.getValueAt(row, 0).toString();
        String maKhachHang = tableModel.getValueAt(row, 1).toString();
        KhachHangDTO kh = new KhachHangDAO().getById(maKhachHang);
        txtSoDienThoaiKhach.setText(kh != null ? kh.getSoDienThoai() : "");
        cbxMaBan.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        try {
            Date dateHen = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tableModel.getValueAt(row, 3).toString());
            spnThoiGianHen.setValue(dateHen);
        } catch (Exception ex) {
            spnThoiGianHen.setValue(new Date());
        }
        spnSoLuong.setValue(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
        cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
    }

    /**
     * Xử lý logic tạo phiếu đặt bàn mới sau khi kiểm tra tính hợp lệ.
     */
    private void handleAddBooking() {
        try {
            String ma = "DB" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());

            String soDienThoai = txtSoDienThoaiKhach.getText().trim();
            if (soDienThoai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại khách hàng!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!new KhachHangBUS().validatePhone(soDienThoai)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (cần đúng 10 chữ số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KhachHangDTO khach = new KhachHangDAO().getById(soDienThoai);
            if (khach == null) {
                JOptionPane.showMessageDialog(this, "Số điện thoại chưa có trong hệ thống. Chuyển sang trang tạo khách hàng mới.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                main.moManKhachHangTuSDT(soDienThoai);
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
                    ma, khach.getMaKhachHang(), cbxMaBan.getSelectedItem().toString(),
                    new Date(), hen, (int) spnSoLuong.getValue(), "Chờ nhận", txtGhiChu.getText().trim()
            );

            if (new PhieuDatBanDAO().insert(phieu)) {
                new quanlynhahang.dao.BanAnDAO().capNhatTrangThai(phieu.getMaBan(), "Đã đặt");
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                Toast.success(parentFrame, "Đã tạo phiếu đặt bàn thành công!");
                loadData();

                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                if (main != null) {
                    main.refreshSoDoBan();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }

    /**
     * Mở danh sách khách hàng để người dùng chọn nhanh vào phiếu đặt bàn.
     */
    private void moDialogChonKhachHang() {
        ArrayList<KhachHangDTO> dsKhach = new KhachHangDAO().getAll();
        if (dsKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiện chưa có khách hàng nào trong hệ thống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"Mã KH", "Tên khách", "SĐT", "Hạng"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (KhachHangDTO kh : dsKhach) {
            model.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getTenKH(),
                    kh.getSoDienThoai(),
                    kh.getMaHang()
            });
        }

        JTable tblKhach = new JTable(model);
        tblKhach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblKhach.setRowHeight(30);

        if (!txtSoDienThoaiKhach.getText().trim().isEmpty()) {
            String soDienThoaiDangChon = txtSoDienThoaiKhach.getText().trim();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (soDienThoaiDangChon.equals(model.getValueAt(i, 2).toString())) {
                    tblKhach.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }

        JScrollPane scroll = new JScrollPane(tblKhach);
        scroll.setPreferredSize(new Dimension(560, 280));

        int result = JOptionPane.showConfirmDialog(
                this,
                scroll,
                "Chọn khách hàng",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int row = tblKhach.getSelectedRow();
            if (row >= 0) {
                txtSoDienThoaiKhach.setText(model.getValueAt(row, 2).toString());
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng.", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Xử lý nghiệp vụ khi khách đến nhận bàn thực tế.
     * Cập nhật trạng thái phiếu và tự động kích hoạt chức năng mở hóa đơn.
     */
    private void handleCheckIn() {
        String ma = selectedMaDatBan;
        if (ma == null || ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt trong danh sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
        String ma = selectedMaDatBan;
        if (ma == null || ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt trong danh sách!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ask = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn hủy phiếu đặt bàn này?", "Hủy đặt", JOptionPane.YES_NO_OPTION);
        if (ask == JOptionPane.YES_OPTION) {
            if (new PhieuDatBanDAO().delete(ma)) {
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                Toast.success(parentFrame, "Đã hủy phiếu thành công!");
                loadData();
            }
        }
    }

    /**
     * Chuẩn bị form đặt bàn mới và chọn sẵn bàn được truyền vào.
     * @param maBan mã bàn muốn đặt trước
     */
    public void chuanBiDatBanChoBan(String maBan) {
        selectedMaDatBan = null;
        txtSoDienThoaiKhach.setText("");
        txtGhiChu.setText("");
        spnSoLuong.setValue(2);
        spnThoiGianHen.setValue(new Date());
        cbxTrangThai.setSelectedItem("Chờ nhận");

        if (maBan != null && !maBan.trim().isEmpty()) {
            cbxMaBan.setSelectedItem(maBan);
        }
        table.clearSelection();
    }

    /**
     * Điền số điện thoại khách vào form đặt bàn hiện tại.
     * @param soDienThoai số điện thoại cần điền
     */
    public void datSoDienThoaiKhach(String soDienThoai) {
        if (soDienThoai != null) {
            txtSoDienThoaiKhach.setText(soDienThoai.trim());
        }
    }
}