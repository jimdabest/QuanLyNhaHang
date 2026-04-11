package quanlynhahang.gui;

import quanlynhahang.bus.MonAnBUS;
import quanlynhahang.dao.MonAnDAO;
import quanlynhahang.dto.MonAnDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel quản lý thực đơn và các món ăn.
 * Cho phép thêm mới, cập nhật, xóa mềm và xem danh sách món.
 */
public class ThucDonPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    // Các ô nhập liệu
    private JTextField txtMaMon, txtTenMon, txtGiaBan;
    private JComboBox<String> cbxPhanLoai, cbxTrangThai;

    public ThucDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // 1. TIÊU ĐỀ
        // ==========================================
        JLabel lblTitle = new JLabel("🍔 QUẢN LÝ THỰC ĐƠN & MÓN ĂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        // ==========================================
        // 2. BẢNG HIỂN THỊ MÓN ĂN
        // ==========================================
        String[] columns = {"Mã Món", "Tên Món", "Phân Loại", "Giá Bán (VNĐ)", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ==========================================
        // 3. FORM NHẬP LIỆU (Bên phải)
        // ==========================================
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setPreferredSize(new Dimension(320, 0));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder("Thông tin món ăn"));

        JPanel pnlForm = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlForm.setBackground(Color.WHITE);

        txtMaMon = new JTextField();
        txtTenMon = new JTextField();
        cbxPhanLoai = new JComboBox<>(new String[]{"Thịt nướng", "Hải sản", "Combo", "Khai vị", "Đồ ăn kèm", "Nước uống"});
        txtGiaBan = new JTextField();
        cbxTrangThai = new JComboBox<>(new String[]{"Còn phục vụ", "Hết hàng"});

        pnlForm.add(new JLabel("Mã món:")); pnlForm.add(txtMaMon);
        pnlForm.add(new JLabel("Tên món:")); pnlForm.add(txtTenMon);
        pnlForm.add(new JLabel("Phân loại:")); pnlForm.add(cbxPhanLoai);
        pnlForm.add(new JLabel("Giá bán (VNĐ):")); pnlForm.add(txtGiaBan);
        pnlForm.add(new JLabel("Trạng thái:")); pnlForm.add(cbxTrangThai);

        pnlRight.add(pnlForm, BorderLayout.NORTH);

        // Nút bấm
        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlButtons.setBackground(Color.WHITE);
        JButton btnThem = new JButton("Thêm mới");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Xóa (Mềm)");
        JButton btnLamMoi = new JButton("Làm mới Form");

        pnlButtons.add(btnThem); pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa); pnlButtons.add(btnLamMoi);
        pnlRight.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);

        // ==========================================
        // 4. XỬ LÝ SỰ KIỆN
        // ==========================================
        // Load dữ liệu lên bảng
        loadData();

        // Click vào bảng -> Đổ dữ liệu sang Form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                txtMaMon.setText(tableModel.getValueAt(row, 0).toString());
                txtTenMon.setText(tableModel.getValueAt(row, 1).toString());
                cbxPhanLoai.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                txtGiaBan.setText(tableModel.getValueAt(row, 3).toString());
                cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                txtMaMon.setEditable(false); // Không cho sửa mã món
            }
        });

        // Nút Thêm Mới
        btnThem.addActionListener(e -> {
            try {
                String maMon = txtMaMon.getText().trim();
                String tenMon = txtTenMon.getText().trim();

                // FIX: Xóa hết dấu chấm, dấu phẩy trước khi ép sang số
                String chuoiGia = txtGiaBan.getText().replace(".", "").replace(",", "");
                double giaBan = Double.parseDouble(chuoiGia);

                MonAnBUS monBUS = new MonAnBUS();

                // Validate logic
                if (!monBUS.checkTenMon(tenMon)) {
                    JOptionPane.showMessageDialog(this, "Tên món ăn không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!monBUS.checkGia(giaBan)) {
                    JOptionPane.showMessageDialog(this, "Giá món ăn không được là số âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (monBUS.checkDuplicate(maMon)) {
                    JOptionPane.showMessageDialog(this, "Mã món [" + maMon + "] đã tồn tại! Vui lòng nhập mã khác.", "Lỗi Trùng Lặp", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chèn vào CSDL
                MonAnDTO mon = new MonAnDTO(maMon, tenMon, cbxPhanLoai.getSelectedItem().toString(), giaBan, cbxTrangThai.getSelectedIndex() == 0);
                if (new MonAnDAO().insert(mon)) {
                    JOptionPane.showMessageDialog(this, "Thêm món thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi! Không thể thêm vào CSDL.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá bán phải là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Cập Nhật
        btnSua.addActionListener(e -> {
            try {
                String maMon = txtMaMon.getText().trim();
                String tenMon = txtTenMon.getText().trim();

                // FIX: Xóa hết dấu chấm, dấu phẩy trước khi ép sang số
                String chuoiGia = txtGiaBan.getText().replace(".", "").replace(",", "");
                double giaBan = Double.parseDouble(chuoiGia);

                if (maMon.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn một món ăn trên bảng để cập nhật!");
                    return;
                }

                MonAnBUS monBUS = new MonAnBUS();

                // Validate logic
                if (!monBUS.checkTenMon(tenMon)) {
                    JOptionPane.showMessageDialog(this, "Tên món ăn không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!monBUS.checkGia(giaBan)) {
                    JOptionPane.showMessageDialog(this, "Giá bán không được là số âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Cập nhật CSDL
                MonAnDTO mon = new MonAnDTO(maMon, tenMon, cbxPhanLoai.getSelectedItem().toString(), giaBan, cbxTrangThai.getSelectedIndex() == 0);
                String resultMsg = monBUS.updateMonAn(mon);
                JOptionPane.showMessageDialog(this, resultMsg);

                if (resultMsg.equals("Cập nhật thành công")) {
                    loadData();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá bán phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Xóa Mềm (Ngừng phục vụ)
        btnXoa.addActionListener(e -> {
            String maMon = txtMaMon.getText().trim();
            if (maMon.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
                return;
            }

            if (new MonAnDAO().xoaMem(maMon)) {
                JOptionPane.showMessageDialog(this, "Đã chuyển món về trạng thái Hết hàng!");
                loadData();
            }
        });

        // Nút Làm mới
        btnLamMoi.addActionListener(e -> {
            txtMaMon.setEditable(true);
            txtMaMon.setText("");
            txtTenMon.setText("");
            txtGiaBan.setText("");
            table.clearSelection();
        });
    }

    /**
     * Tải danh sách món ăn từ database lên bảng hiển thị.
     */
    private void loadData() {
        tableModel.setRowCount(0);
        ArrayList<MonAnDTO> dsMon = new MonAnDAO().getAll();
        for (MonAnDTO m : dsMon) {
            tableModel.addRow(new Object[]{
                    m.getMaMon(), m.getTenMon(), m.getPhanLoai(),
                    String.format("%,.0f", m.getGiaHienTai()), // Format số tiền hiển thị có dấu phẩy cho đẹp
                    m.isTrangThaiPhucVu() ? "Còn phục vụ" : "Hết hàng"
            });
        }
    }
}