package quanlynhahang.gui;

import quanlynhahang.bus.KhachHangBUS;
import quanlynhahang.dao.HangThanhVienDAO;
import quanlynhahang.dao.KhachHangDAO;
import quanlynhahang.dto.HangThanhVienDTO;
import quanlynhahang.dto.KhachHangDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.ArrayList;

/**
 * Panel quản lý khách hàng và hạng thành viên.
 */
public class KhachHangPanel extends JPanel {

    private MainFrame mainFrame;
    private boolean chuyenVeThanhToan = false;
    private boolean quickCreateMode = false;

    private JTabbedPane tabs;

    private JTable tblKhach;
    private DefaultTableModel modelKhach;
    private JTextField txtMaKhach;
    private JTextField txtTenKhach;
    private JTextField txtSoDienThoai;
    private JComboBox<String> cbxMaHang;
    private JTextField txtTongChiTieu;
    private JCheckBox chkTrangThai;

    private JTable tblHang;
    private DefaultTableModel modelHang;
    private JTextField txtMaHang;
    private JTextField txtTenHang;
    private JTextField txtMucGiam;
    private JTextField txtDieuKien;
    private JTextField txtQuyenLoi;

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final HangThanhVienDAO hangThanhVienDAO = new HangThanhVienDAO();

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG & HẠNG THÀNH VIÊN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 64, 175));
        add(lblTitle, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("Khách hàng", createKhachHangTab());
        tabs.addTab("Hạng thành viên", createHangThanhVienTab());
        add(tabs, BorderLayout.CENTER);

        loadHangCombo();
        loadKhachHangData();
        loadHangThanhVienData();
    }

    private JPanel createKhachHangTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        modelKhach = new DefaultTableModel(
                new String[]{"Mã KH", "Tên khách", "SĐT", "Mã hạng", "Tổng chi tiêu", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKhach = new JTable(modelKhach);
        tblKhach.setRowHeight(32);
        tblKhach.getSelectionModel().addListSelectionListener(e -> fillKhachFormFromTable());
        panel.add(new JScrollPane(tblKhach), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(12, 1, 5, 4));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        txtMaKhach = new JTextField();
        txtMaKhach.setEditable(false);
        txtTenKhach = new JTextField();
        txtSoDienThoai = new JTextField();
        cbxMaHang = new JComboBox<>();
        txtTongChiTieu = new JTextField("0");
        chkTrangThai = new JCheckBox("Đang hoạt động", true);
        chkTrangThai.setBackground(Color.WHITE);

        form.add(new JLabel("Mã khách:"));
        form.add(txtMaKhach);
        form.add(new JLabel("Tên khách hàng:"));
        form.add(txtTenKhach);
        form.add(new JLabel("Số điện thoại:"));
        form.add(txtSoDienThoai);
        form.add(new JLabel("Hạng thành viên:"));
        form.add(cbxMaHang);
        form.add(new JLabel("Tổng chi tiêu:"));
        form.add(txtTongChiTieu);
        form.add(new JLabel("Trạng thái:"));
        form.add(chkTrangThai);

        JPanel actions = new JPanel(new GridLayout(1, 4, 8, 0));
        actions.setBackground(new Color(241, 245, 249));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");
        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnMoi);

        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnMoi.addActionListener(e -> clearKhachForm());

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(340, 0));
        right.add(form, BorderLayout.CENTER);
        right.add(actions, BorderLayout.SOUTH);

        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    private JPanel createHangThanhVienTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        modelHang = new DefaultTableModel(
                new String[]{"Mã hạng", "Tên hạng", "Mức giảm", "Điều kiện", "Quyền lợi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHang = new JTable(modelHang);
        tblHang.setRowHeight(32);
        tblHang.getSelectionModel().addListSelectionListener(e -> fillHangFormFromTable());
        panel.add(new JScrollPane(tblHang), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(10, 1, 5, 4));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        txtMaHang = new JTextField();
        txtTenHang = new JTextField();
        txtMucGiam = new JTextField();
        txtDieuKien = new JTextField();
        txtQuyenLoi = new JTextField();

        form.add(new JLabel("Mã hạng:"));
        form.add(txtMaHang);
        form.add(new JLabel("Tên hạng:"));
        form.add(txtTenHang);
        form.add(new JLabel("Mức giảm (0-1):"));
        form.add(txtMucGiam);
        form.add(new JLabel("Điều kiện tổng chi tiêu:"));
        form.add(txtDieuKien);
        form.add(new JLabel("Quyền lợi khác:"));
        form.add(txtQuyenLoi);

        JPanel actions = new JPanel(new GridLayout(1, 4, 8, 0));
        actions.setBackground(new Color(241, 245, 249));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnMoi = new JButton("Làm mới");
        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnMoi);

        btnThem.addActionListener(e -> themHangThanhVien());
        btnSua.addActionListener(e -> suaHangThanhVien());
        btnXoa.addActionListener(e -> xoaHangThanhVien());
        btnMoi.addActionListener(e -> clearHangForm());

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(340, 0));
        right.add(form, BorderLayout.CENTER);
        right.add(actions, BorderLayout.SOUTH);

        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setChuyenVeThanhToan(boolean chuyenVeThanhToan) {
        this.chuyenVeThanhToan = chuyenVeThanhToan;
    }

    /**
     * Chuẩn bị form tạo khách với SĐT đã nhập từ màn đặt bàn.
     */
    public void chuanBiThemKhachMoi(String soDienThoai) {
        quickCreateMode = true;
        tabs.setSelectedIndex(0);
        clearKhachForm();
        txtTenKhach.setText("");
        txtSoDienThoai.setText(soDienThoai != null ? soDienThoai.trim() : "");
        txtTenKhach.requestFocusInWindow();
    }

    public void batCheDoQuanLy() {
        quickCreateMode = false;
        chuyenVeThanhToan = false;
        tabs.setSelectedIndex(0);
        loadHangCombo();
        loadKhachHangData();
        loadHangThanhVienData();
    }

    private void themKhachHang() {
        String tenKhach = txtTenKhach.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();

        if (tenKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!new KhachHangBUS().validatePhone(soDienThoai)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ (cần đúng 10 chữ số)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (khachHangDAO.getById(soDienThoai) != null) {
            JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại trong hệ thống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (quickCreateMode && mainFrame != null) {
                if (chuyenVeThanhToan) {
                    mainFrame.tiepTucThanhToanSauKhiTaoKhach(soDienThoai);
                } else {
                    mainFrame.quayLaiDatBanVoiSoDienThoai(soDienThoai);
                }
            }
            return;
        }

        KhachHangDTO khachMoi = new KhachHangDTO(
                khachHangDAO.taoMaKhachHangMoi(),
                cbxMaHang.getSelectedItem() == null ? "H01" : cbxMaHang.getSelectedItem().toString(),
                tenKhach,
                soDienThoai,
                parseDoubleSafe(txtTongChiTieu.getText().trim()),
                new Date(),
                chkTrangThai.isSelected()
        );

        if (khachHangDAO.insert(khachMoi)) {
            if (mainFrame != null) {
                Toast.success(mainFrame, "Tạo khách hàng thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Tạo khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
            loadKhachHangData();
            clearKhachForm();
            if (quickCreateMode && mainFrame != null) {
                if (chuyenVeThanhToan) {
                    mainFrame.tiepTucThanhToanSauKhiTaoKhach(soDienThoai);
                } else {
                    mainFrame.quayLaiDatBanVoiSoDienThoai(soDienThoai);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không thể tạo khách hàng mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaKhachHang() {
        String maKhach = txtMaKhach.getText().trim();
        if (maKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tenKhach = txtTenKhach.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (tenKhach.isEmpty() || !new KhachHangBUS().validatePhone(soDienThoai)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng tên và số điện thoại (10 chữ số).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        KhachHangDTO kh = new KhachHangDTO(
                maKhach,
                cbxMaHang.getSelectedItem() == null ? "H01" : cbxMaHang.getSelectedItem().toString(),
                tenKhach,
                soDienThoai,
                parseDoubleSafe(txtTongChiTieu.getText().trim()),
                new Date(),
                chkTrangThai.isSelected()
        );

        if (khachHangDAO.update(kh)) {
            showToastOrDialog("Cập nhật khách hàng thành công!");
            loadKhachHangData();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaKhachHang() {
        String maKhach = txtMaKhach.getText().trim();
        if (maKhach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa mềm khách hàng " + maKhach + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (khachHangDAO.delete(maKhach)) {
            showToastOrDialog("Đã khóa khách hàng thành công!");
            loadKhachHangData();
            clearKhachForm();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xóa khách hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themHangThanhVien() {
        HangThanhVienDTO h = buildHangFromForm();
        if (h == null) return;

        if (hangThanhVienDAO.getById(h.getMaHang()) != null) {
            JOptionPane.showMessageDialog(this, "Mã hạng đã tồn tại.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (hangThanhVienDAO.insert(h)) {
            showToastOrDialog("Thêm hạng thành viên thành công!");
            loadHangThanhVienData();
            loadHangCombo();
            clearHangForm();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm hạng thành viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaHangThanhVien() {
        HangThanhVienDTO h = buildHangFromForm();
        if (h == null) return;

        if (hangThanhVienDAO.update(h)) {
            showToastOrDialog("Cập nhật hạng thành viên thành công!");
            loadHangThanhVienData();
            loadHangCombo();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật hạng thành viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaHangThanhVien() {
        String maHang = txtMaHang.getText().trim();
        if (maHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hạng cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa hạng thành viên " + maHang + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (hangThanhVienDAO.delete(maHang)) {
            showToastOrDialog("Đã xóa hạng thành viên!");
            loadHangThanhVienData();
            loadHangCombo();
            clearHangForm();
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xóa hạng. Có thể đang được khách hàng sử dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private HangThanhVienDTO buildHangFromForm() {
        String ma = txtMaHang.getText().trim();
        String ten = txtTenHang.getText().trim();
        String quyenLoi = txtQuyenLoi.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã hạng và tên hạng không được trống.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        double mucGiam;
        double dieuKien;
        try {
            mucGiam = Double.parseDouble(txtMucGiam.getText().trim());
            dieuKien = Double.parseDouble(txtDieuKien.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mức giảm và điều kiện phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (mucGiam < 0 || mucGiam > 1) {
            JOptionPane.showMessageDialog(this, "Mức giảm phải trong khoảng 0 đến 1.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new HangThanhVienDTO(ma, ten, mucGiam, dieuKien, quyenLoi);
    }

    private void fillKhachFormFromTable() {
        int row = tblKhach.getSelectedRow();
        if (row < 0) return;

        txtMaKhach.setText(modelKhach.getValueAt(row, 0).toString());
        txtTenKhach.setText(modelKhach.getValueAt(row, 1).toString());
        txtSoDienThoai.setText(modelKhach.getValueAt(row, 2).toString());
        cbxMaHang.setSelectedItem(modelKhach.getValueAt(row, 3).toString());
        txtTongChiTieu.setText(modelKhach.getValueAt(row, 4).toString().replace(",", ""));
        chkTrangThai.setSelected("Hoạt động".equals(modelKhach.getValueAt(row, 5).toString()));
    }

    private void fillHangFormFromTable() {
        int row = tblHang.getSelectedRow();
        if (row < 0) return;

        txtMaHang.setText(modelHang.getValueAt(row, 0).toString());
        txtTenHang.setText(modelHang.getValueAt(row, 1).toString());
        txtMucGiam.setText(modelHang.getValueAt(row, 2).toString());
        txtDieuKien.setText(modelHang.getValueAt(row, 3).toString());
        txtQuyenLoi.setText(modelHang.getValueAt(row, 4).toString());
    }

    private void clearKhachForm() {
        txtMaKhach.setText("");
        txtTenKhach.setText("");
        txtSoDienThoai.setText("");
        if (cbxMaHang.getItemCount() > 0) cbxMaHang.setSelectedIndex(0);
        txtTongChiTieu.setText("0");
        chkTrangThai.setSelected(true);
        tblKhach.clearSelection();
    }

    private void clearHangForm() {
        txtMaHang.setText("");
        txtTenHang.setText("");
        txtMucGiam.setText("");
        txtDieuKien.setText("");
        txtQuyenLoi.setText("");
        tblHang.clearSelection();
    }

    private void loadKhachHangData() {
        modelKhach.setRowCount(0);
        ArrayList<KhachHangDTO> list = khachHangDAO.getAll();
        for (KhachHangDTO kh : list) {
            modelKhach.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getTenKH(),
                    kh.getSoDienThoai(),
                    kh.getMaHang(),
                    String.format("%,.0f", kh.getTongChiTieu()),
                    kh.isTrangThai() ? "Hoạt động" : "Khóa"
            });
        }
    }

    private void loadHangThanhVienData() {
        modelHang.setRowCount(0);
        ArrayList<HangThanhVienDTO> list = hangThanhVienDAO.getAll();
        for (HangThanhVienDTO h : list) {
            modelHang.addRow(new Object[]{
                    h.getMaHang(),
                    h.getTenHang(),
                    h.getMucGiamGia(),
                    h.getDieuKienTongChiTieu(),
                    h.getQuyenLoiKhac()
            });
        }
    }

    private void loadHangCombo() {
        cbxMaHang.removeAllItems();
        for (HangThanhVienDTO h : hangThanhVienDAO.getAll()) {
            cbxMaHang.addItem(h.getMaHang());
        }
    }

    private void showToastOrDialog(String message) {
        if (mainFrame != null) {
            Toast.success(mainFrame, message);
        } else {
            JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
            return 0;
        }
    }
}
