package quanlynhahang.gui;

import quanlynhahang.bus.PhieuDatBanBUS;
import quanlynhahang.dao.PhieuDatBanDAO;
import quanlynhahang.dto.PhieuDatBanDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Panel quản lý phiếu đặt bàn.
 * Cho phép tạo mới, nhận bàn và hủy đặt bàn.
 */
public class DatBanPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    // Các thành phần form nhập liệu
    private JTextField txtMaDatBan, txtMaKhachHang, txtGhiChu;
    private JComboBox<String> cbxMaBan, cbxTrangThai;
    private JSpinner spnSoLuong, spnThoiGianNhan;

    public DatBanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // 1. TIÊU ĐỀ
        // ==========================================
        JLabel lblTitle = new JLabel("📅 QUẢN LÝ ĐẶT BÀN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // ==========================================
        // 2. BẢNG HIỂN THỊ PHIẾU ĐẶT BÀN
        // ==========================================
        String[] columns = {"Mã Đặt Bàn", "Mã KH", "Mã Bàn", "Giờ Hẹn", "Số Khách", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên bảng
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ==========================================
        // 3. FORM NHẬP LIỆU (Bên phải)
        // ==========================================
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setPreferredSize(new Dimension(350, 0));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu đặt"));

        JPanel pnlForm = new JPanel(new GridLayout(14, 1, 5, 2));
        pnlForm.setBackground(Color.WHITE);

        txtMaDatBan = new JTextField();
        txtMaKhachHang = new JTextField();

        // Load động danh sách bàn vào đây, em hardcode tạm vài bàn mẫu
        cbxMaBan = new JComboBox<>(new String[]{"VIP01", "B01", "B02", "B03"});

        // Spinner cho số lượng khách
        spnSoLuong = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));

        // Spinner cho ngày giờ nhận bàn
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spnThoiGianNhan = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnThoiGianNhan, "dd/MM/yyyy HH:mm");
        spnThoiGianNhan.setEditor(dateEditor);

        cbxTrangThai = new JComboBox<>(new String[]{"Chờ nhận", "Đã nhận", "Đã hủy"});
        txtGhiChu = new JTextField();

        pnlForm.add(new JLabel("Mã đặt bàn (Tự sinh/Nhập):")); pnlForm.add(txtMaDatBan);
        pnlForm.add(new JLabel("Mã khách hàng:")); pnlForm.add(txtMaKhachHang);
        pnlForm.add(new JLabel("Chọn bàn:")); pnlForm.add(cbxMaBan);
        pnlForm.add(new JLabel("Giờ hẹn nhận bàn:")); pnlForm.add(spnThoiGianNhan);
        pnlForm.add(new JLabel("Số lượng khách:")); pnlForm.add(spnSoLuong);
        pnlForm.add(new JLabel("Trạng thái:")); pnlForm.add(cbxTrangThai);
        pnlForm.add(new JLabel("Ghi chú (Tùy chọn):")); pnlForm.add(txtGhiChu);

        pnlRight.add(pnlForm, BorderLayout.NORTH);

        // ==========================================
        // 4. KHU VỰC NÚT BẤM
        // ==========================================
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton btnThem = new JButton("Tạo phiếu đặt");
        JButton btnNhanBan = new JButton("Khách nhận bàn");
        JButton btnHuy = new JButton("Hủy đặt bàn");
        JButton btnLamMoi = new JButton("Làm mới Form");

        btnThem.setBackground(new Color(0, 122, 255));
        btnThem.setForeground(Color.WHITE);

        btnNhanBan.setBackground(new Color(40, 167, 69));
        btnNhanBan.setForeground(Color.WHITE);

        btnHuy.setBackground(new Color(220, 53, 69));
        btnHuy.setForeground(Color.WHITE);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnNhanBan);
        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLamMoi);

        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);

        // ==========================================
        // 5. XỬ LÝ SỰ KIỆN CƠ BẢN
        // ==========================================
        loadData();

        // Click chuột vào bảng để load dữ liệu lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                txtMaDatBan.setText(tableModel.getValueAt(row, 0).toString());
                txtMaKhachHang.setText(tableModel.getValueAt(row, 1).toString());
                cbxMaBan.setSelectedItem(tableModel.getValueAt(row, 2).toString());

                // Parse ngày giờ từ bảng lên Spinner
                try {
                    String dateString = tableModel.getValueAt(row, 3).toString();
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateString);
                    spnThoiGianNhan.setValue(date);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                spnSoLuong.setValue(Integer.parseInt(tableModel.getValueAt(row, 4).toString()));
                cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                txtMaDatBan.setEditable(false);
            }
        });

        // Xóa trắng form
        btnLamMoi.addActionListener(e -> {
            txtMaDatBan.setEditable(true);
            txtMaDatBan.setText("");
            txtMaKhachHang.setText("");
            txtGhiChu.setText("");
            spnSoLuong.setValue(2);
            spnThoiGianNhan.setValue(new Date());
            cbxTrangThai.setSelectedIndex(0);
            table.clearSelection();
        });

        // ==========================================
        // 6. XỬ LÝ SỰ KIỆN CÁC NÚT BẤM (CRUD)
        // ==========================================

        // 1. SỰ KIỆN: TẠO PHIẾU ĐẶT BÀN MỚI
        btnThem.addActionListener(e -> {
            try {
                // Lấy dữ liệu từ form
                String maDatBan = txtMaDatBan.getText().trim();
                // Nếu để trống mã, hệ thống tự sinh mã theo ngày giờ (VD: DB2604041530)
                if (maDatBan.isEmpty()) {
                    maDatBan = "DB" + new SimpleDateFormat("yyMMddHHmmss").format(new Date());
                }

                String maKhachHang = txtMaKhachHang.getText().trim();
                if (maKhachHang.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String maBan = cbxMaBan.getSelectedItem().toString();
                Date thoiGianNhan = (Date) spnThoiGianNhan.getValue();
                int soLuong = (Integer) spnSoLuong.getValue();
                String ghiChu = txtGhiChu.getText().trim();

                // Gọi BUS để kiểm tra nghiệp vụ (Validate)
                PhieuDatBanBUS bus = new PhieuDatBanBUS();

                // Ràng buộc 1: Giờ nhận bàn phải sau giờ hiện tại ít nhất 30 phút
                if (!bus.checkThoiGian(thoiGianNhan)) {
                    JOptionPane.showMessageDialog(this, "Giờ nhận bàn phải sau giờ hiện tại ít nhất 30 phút!", "Lỗi thời gian", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ràng buộc 2: Bàn đó phải đang "Trống" mới cho đặt (Tùy chọn thêm)
                if (!bus.checkBanTrong(maBan)) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Bàn này hiện không trống. Anh/chị vẫn muốn đặt chèn lịch?", "Cảnh báo", JOptionPane.YES_NO_OPTION);
                    if (confirm != JOptionPane.YES_OPTION) return;
                }

                // Đóng gói thành DTO và gọi DAO chèn vào CSDL
                PhieuDatBanDTO phieu = new PhieuDatBanDTO(
                        maDatBan, maKhachHang, maBan, new Date(), thoiGianNhan, soLuong, "Chờ nhận", ghiChu
                );

                if (new PhieuDatBanDAO().insert(phieu)) {
                    JOptionPane.showMessageDialog(this, "✅ Tạo phiếu đặt bàn thành công!");
                    loadData(); // Tải lại bảng
                    btnLamMoi.doClick(); // Reset form cho sạch
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Lỗi hệ thống! Không thể tạo phiếu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. SỰ KIỆN: KHÁCH ĐẾN NHẬN BÀN
        btnNhanBan.addActionListener(e -> {
            String maDatBan = txtMaDatBan.getText().trim();
            if (maDatBan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu trên bảng để thao tác!", "Nhắc nhở", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Chỉ cho phép nhận bàn nếu trạng thái đang là "Chờ nhận"
            if (!cbxTrangThai.getSelectedItem().toString().equalsIgnoreCase("Chờ nhận")) {
                JOptionPane.showMessageDialog(this, "Chỉ phiếu đang ở trạng thái 'Chờ nhận' mới có thể nhận bàn!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận khách đã đến và nhận bàn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                PhieuDatBanDAO dao = new PhieuDatBanDAO();
                PhieuDatBanDTO phieu = dao.getById(maDatBan); // Kéo dữ liệu cũ lên

                if (phieu != null) {
                    phieu.setTrangThai("Đã nhận"); // Thay đổi trạng thái

                    if (dao.update(phieu)) {
                        JOptionPane.showMessageDialog(this, "✅ Khách đã nhận bàn thành công!");
                        loadData();

                        // Ở đây anh có thể nâng cấp thêm: Gọi lệnh HoaDonBUS.moBanMoi(phieu.getMaBan(), phieu.getMaKhachHang())
                        // để hệ thống tự động mở bàn bên SoDoBanPanel luôn cho tiện ạ.
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ Cập nhật trạng thái thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 3. SỰ KIỆN: HỦY ĐẶT BÀN (Xóa mềm)
        btnHuy.addActionListener(e -> {
            String maDatBan = txtMaDatBan.getText().trim();
            if (maDatBan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu cần hủy!", "Nhắc nhở", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Chỉ cho phép hủy nếu trạng thái đang là "Chờ nhận"
            if (!cbxTrangThai.getSelectedItem().toString().equalsIgnoreCase("Chờ nhận")) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể hủy các phiếu đang chờ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Anh/chị có chắc chắn muốn hủy phiếu đặt bàn này?", "Hủy đặt bàn", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Trong PhieuDatBanDAO của anh, lệnh delete() thực chất là UPDATE TrangThai = 'Đã hủy'
                if (new PhieuDatBanDAO().delete(maDatBan)) {
                    JOptionPane.showMessageDialog(this, "✅ Đã hủy phiếu đặt bàn!");
                    loadData();
                    btnLamMoi.doClick();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Hủy thất bại do lỗi cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    /**
     * Tải danh sách phiếu đặt bàn từ cơ sở dữ liệu lên bảng.
     */
    private void loadData() {
        tableModel.setRowCount(0);
        ArrayList<PhieuDatBanDTO> dsPhieu = new PhieuDatBanDAO().getAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        for (PhieuDatBanDTO p : dsPhieu) {
            String thoiGian = (p.getThoiGianNhanBan() != null) ? sdf.format(p.getThoiGianNhanBan()) : "";
            tableModel.addRow(new Object[]{
                    p.getMaDatBan(),
                    p.getMaKhachHang(),
                    p.getMaBan(),
                    thoiGian,
                    p.getSoLuongKhach(),
                    p.getTrangThai()
            });
        }
    }
}