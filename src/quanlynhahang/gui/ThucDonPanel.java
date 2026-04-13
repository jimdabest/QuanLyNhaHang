package quanlynhahang.gui;

import quanlynhahang.bus.MonAnBUS;
import quanlynhahang.dao.MonAnDAO;
import quanlynhahang.dto.MonAnDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel quản lý thực đơn và danh sách món ăn.
 * Chức năng: Hiển thị thực đơn, Quản lý (Thêm/Sửa/Xóa) và Gọi món vào Bill.
 */
public class ThucDonPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;

    // --- COMPONENTS NHẬP LIỆU ---
    private JTextField txtMaMon;
    private JTextField txtTenMon;
    private JTextField txtGiaBan;
    private JComboBox<String> cbxPhanLoai;
    private JComboBox<String> cbxTrangThai;

    /**
     * Khởi tạo giao diện ThucDonPanel với đầy đủ tính năng.
     */
    public ThucDonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Header: Tiêu đề trang
        JLabel lblTitle = new JLabel("QUẢN LÝ THỰC ĐƠN & GỌI MÓN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(30, 64, 175));
        add(lblTitle, BorderLayout.NORTH);

        // 2. Center: Khu vực hiển thị bảng dữ liệu
        createTableArea();

        // 3. East: Khu vực Form nhập liệu và Nút bấm
        createManagementArea();

        // Tải dữ liệu từ DB lên bảng
        loadData();
    }

    /**
     * Tạo khu vực bảng hiển thị danh sách món ăn.
     */
    private void createTableArea() {
        String[] columns = {"Mã Món", "Tên Món", "Phân Loại", "Giá Bán (VNĐ)", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp trên ô của bảng
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sự kiện khi click chọn một dòng trong bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMaMon.setText(tableModel.getValueAt(row, 0).toString());
                txtTenMon.setText(tableModel.getValueAt(row, 1).toString());
                cbxPhanLoai.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                // Xử lý giá: Bỏ dấu phẩy định dạng để đưa vào TextField
                String giaStr = tableModel.getValueAt(row, 3).toString().replace(",", "");
                txtGiaBan.setText(giaStr);
                cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 4).toString());

                txtMaMon.setEditable(false); // Đã chọn món thì không cho sửa mã
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Tạo khu vực quản lý thông tin món ăn (Form & Buttons).
     */
    private void createManagementArea() {
        JPanel pnlRight = new JPanel(new BorderLayout(10, 10));
        pnlRight.setPreferredSize(new Dimension(350, 0));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(" Thông tin món ăn "),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // --- FORM NHẬP LIỆU ---
        JPanel pnlForm = new JPanel(new GridLayout(10, 1, 5, 5));
        pnlForm.setBackground(Color.WHITE);

        txtMaMon = new JTextField();
        txtTenMon = new JTextField();
        cbxPhanLoai = new JComboBox<>(new String[]{"Thịt nướng", "Hải sản", "Combo", "Khai vị", "Đồ ăn kèm", "Nước uống"});
        txtGiaBan = new JTextField();
        cbxTrangThai = new JComboBox<>(new String[]{"Còn phục vụ", "Hết hàng"});

        pnlForm.add(new JLabel("Mã món ăn:")); pnlForm.add(txtMaMon);
        pnlForm.add(new JLabel("Tên món:")); pnlForm.add(txtTenMon);
        pnlForm.add(new JLabel("Phân loại:")); pnlForm.add(cbxPhanLoai);
        pnlForm.add(new JLabel("Giá bán (VNĐ):")); pnlForm.add(txtGiaBan);
        pnlForm.add(new JLabel("Trạng thái phục vụ:")); pnlForm.add(cbxTrangThai);

        pnlRight.add(pnlForm, BorderLayout.NORTH);

        // --- HÀNH ĐỘNG (BUTTONS) ---
        JPanel pnlActions = new JPanel(new GridLayout(3, 2, 8, 8));
        pnlActions.setBackground(Color.WHITE);

        JButton btnThem = new JButton("Thêm món");
        JButton btnSua = new JButton("Cập nhật");
        JButton btnXoa = new JButton("Ngừng bán");
        JButton btnLamMoi = new JButton("Làm mới Form");

        // NÚT MỐI NỐI: Đẩy món sang hóa đơn
        JButton btnOrder = new JButton("CHO VÀO BILL");
        btnOrder.setBackground(new Color(255, 153, 0));
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFont(new Font("Segoe UI", Font.BOLD, 15));

        pnlActions.add(btnThem);
        pnlActions.add(btnSua);
        pnlActions.add(btnXoa);
        pnlActions.add(btnLamMoi);
        pnlActions.add(new JLabel("")); // Ô trống để căn chỉnh
        pnlActions.add(btnOrder);

        pnlRight.add(pnlActions, BorderLayout.SOUTH);
        add(pnlRight, BorderLayout.EAST);

        // --- ĐĂNG KÝ SỰ KIỆN NÚT BẤM ---

        // 1. Thêm món mới
        btnThem.addActionListener(e -> {
            try {
                String ma = txtMaMon.getText().trim();
                String ten = txtTenMon.getText().trim();
                double gia = Double.parseDouble(txtGiaBan.getText().trim());
                boolean status = cbxTrangThai.getSelectedIndex() == 0;

                MonAnBUS bus = new MonAnBUS();
                if (ma.isEmpty() || ten.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không được để trống mã hoặc tên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (bus.checkDuplicate(ma)) {
                    JOptionPane.showMessageDialog(this, "Mã món đã tồn tại!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                MonAnDTO mon = new MonAnDTO(ma, ten, cbxPhanLoai.getSelectedItem().toString(), gia, status);
                if (new MonAnDAO().insert(mon)) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    Toast.success(parentFrame, "Thêm món thành công!");
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: Vui lòng kiểm tra lại giá bán!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Cập nhật món
        btnSua.addActionListener(e -> {
            String ma = txtMaMon.getText().trim();
            if (ma.isEmpty()) return;

            try {
                double gia = Double.parseDouble(txtGiaBan.getText().trim());
                MonAnDTO mon = new MonAnDTO(ma, txtTenMon.getText().trim(), cbxPhanLoai.getSelectedItem().toString(), gia, cbxTrangThai.getSelectedIndex() == 0);

                if (new MonAnDAO().update(mon)) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    Toast.success(parentFrame, "Đã cập nhật thông tin!");
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: Vui lòng kiểm tra lại giá bán!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 3. Ngừng phục vụ (Xóa mềm)
        btnXoa.addActionListener(e -> {
            String ma = txtMaMon.getText().trim();
            if (ma.isEmpty()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận ngừng phục vụ món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (new MonAnDAO().xoaMem(ma)) {
                    JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    Toast.success(parentFrame, "Đã ngừng phục vụ món!");
                    loadData();
                }
            }
        });

        // 4. Làm mới form
        btnLamMoi.addActionListener(e -> {
            txtMaMon.setEditable(true);
            txtMaMon.setText("");
            txtTenMon.setText("");
            txtGiaBan.setText("");
            cbxPhanLoai.setSelectedIndex(0);
            cbxTrangThai.setSelectedIndex(0);
            table.clearSelection();
        });

        // 5. THỰC HIỆN GỌI MÓN (ORDER)
        btnOrder.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món nướng cần gọi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maMon = tableModel.getValueAt(row, 0).toString();
            String tenMon = tableModel.getValueAt(row, 1).toString();
            double gia = Double.parseDouble(tableModel.getValueAt(row, 3).toString().replace(",", ""));

            // Tìm MainFrame cha để đẩy dữ liệu sang Bill
            MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
            main.themMonVaoBill(maMon, 1, gia); // Mặc định thêm 1 phần
            Toast.success(main, "Đã thêm " + tenMon + " vào hóa đơn");
        });
    }

    /**
     * Tải danh sách món ăn từ Database lên JTable.
     */
    public void loadData() {
        tableModel.setRowCount(0);
        ArrayList<MonAnDTO> list = new MonAnDAO().getAll();
        for (MonAnDTO m : list) {
            tableModel.addRow(new Object[]{
                    m.getMaMon(),
                    m.getTenMon(),
                    m.getPhanLoai(),
                    String.format("%,.0f", m.getGiaHienTai()),
                    m.isTrangThaiPhucVu() ? "Còn phục vụ" : "Hết hàng"
            });
        }
    }
}