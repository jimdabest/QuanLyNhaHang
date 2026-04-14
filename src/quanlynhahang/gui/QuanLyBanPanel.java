package quanlynhahang.gui;

import quanlynhahang.dao.BanAnDAO;
import quanlynhahang.dto.BanAnDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel quản lý bàn ăn: thêm, sửa, xóa và cập nhật thuộc tính của bàn.
 */
public class QuanLyBanPanel extends JPanel {

	private final DefaultTableModel tableModel;
	private final JTable table;
	private final JTextField txtMaBan;
	private final JTextField txtTenBan;
	private final JTextField txtSucChua;
	private final JTextField txtKhuVuc;
	private final JComboBox<String> cbxTrangThai;

	public QuanLyBanPanel() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(241, 245, 249));
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

		JLabel lblTitle = new JLabel("QUẢN LÝ BÀN ĂN");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(new Color(30, 64, 175));
		add(lblTitle, BorderLayout.NORTH);

		String[] cols = {"Mã bàn", "Tên bàn", "Sức chứa", "Khu vực", "Trạng thái"};
		tableModel = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setRowHeight(34);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());

		add(new JScrollPane(table), BorderLayout.CENTER);

		JPanel pnlForm = new JPanel(new GridLayout(10, 1, 4, 3));
		pnlForm.setBackground(Color.WHITE);
		pnlForm.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225)),
				BorderFactory.createEmptyBorder(10, 12, 10, 12)
		));

		txtMaBan = new JTextField();
		txtTenBan = new JTextField();
		txtSucChua = new JTextField();
		txtKhuVuc = new JTextField();
		cbxTrangThai = new JComboBox<>(new String[]{"Trống", "Đang ăn", "Đã đặt"});

		pnlForm.add(new JLabel("Mã bàn:"));
		pnlForm.add(txtMaBan);
		pnlForm.add(new JLabel("Tên bàn:"));
		pnlForm.add(txtTenBan);
		pnlForm.add(new JLabel("Sức chứa:"));
		pnlForm.add(txtSucChua);
		pnlForm.add(new JLabel("Khu vực:"));
		pnlForm.add(txtKhuVuc);
		pnlForm.add(new JLabel("Trạng thái:"));
		pnlForm.add(cbxTrangThai);

		JPanel pnlButtons = new JPanel(new GridLayout(1, 4, 8, 0));
		pnlButtons.setBackground(getBackground());
		JButton btnThem = new JButton("Thêm");
		JButton btnSua = new JButton("Sửa");
		JButton btnXoa = new JButton("Xóa");
		JButton btnLamMoi = new JButton("Làm mới");

		btnThem.addActionListener(e -> handleAdd());
		btnSua.addActionListener(e -> handleUpdate());
		btnXoa.addActionListener(e -> handleDelete());
		btnLamMoi.addActionListener(e -> clearForm());

		pnlButtons.add(btnThem);
		pnlButtons.add(btnSua);
		pnlButtons.add(btnXoa);
		pnlButtons.add(btnLamMoi);

		JPanel pnlSouth = new JPanel(new BorderLayout(0, 8));
		pnlSouth.setBackground(getBackground());
		pnlSouth.add(pnlForm, BorderLayout.CENTER);
		pnlSouth.add(pnlButtons, BorderLayout.SOUTH);
		add(pnlSouth, BorderLayout.SOUTH);

		loadData();
	}

	public void loadData() {
		tableModel.setRowCount(0);
		ArrayList<BanAnDTO> list = new BanAnDAO().getAll();
		for (BanAnDTO b : list) {
			tableModel.addRow(new Object[]{
					b.getMaBan(),
					b.getTenBan(),
					b.getSucChua(),
					b.getKhuVuc(),
					b.getTrangThai()
			});
		}
	}

	private void fillFormFromSelectedRow() {
		int row = table.getSelectedRow();
		if (row < 0) {
			return;
		}
		txtMaBan.setText(tableModel.getValueAt(row, 0).toString());
		txtTenBan.setText(tableModel.getValueAt(row, 1).toString());
		txtSucChua.setText(tableModel.getValueAt(row, 2).toString());
		txtKhuVuc.setText(tableModel.getValueAt(row, 3).toString());
		cbxTrangThai.setSelectedItem(tableModel.getValueAt(row, 4).toString());
		txtMaBan.setEditable(false);
	}

	private void clearForm() {
		txtMaBan.setText("");
		txtTenBan.setText("");
		txtSucChua.setText("");
		txtKhuVuc.setText("");
		cbxTrangThai.setSelectedItem("Trống");
		txtMaBan.setEditable(true);
		table.clearSelection();
	}

	private BanAnDTO buildFromForm() {
		String maBan = txtMaBan.getText().trim().toUpperCase();
		String tenBan = txtTenBan.getText().trim();
		String khuVuc = txtKhuVuc.getText().trim();
		String trangThai = cbxTrangThai.getSelectedItem().toString();

		if (maBan.isEmpty() || tenBan.isEmpty() || khuVuc.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bàn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		if (maBan.length() > 10) {
			JOptionPane.showMessageDialog(this, "Mã bàn tối đa 10 ký tự.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (tenBan.length() > 50) {
			JOptionPane.showMessageDialog(this, "Tên bàn tối đa 50 ký tự.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		if (khuVuc.length() > 50) {
			JOptionPane.showMessageDialog(this, "Khu vực tối đa 50 ký tự.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		int sucChua;
		try {
			sucChua = Integer.parseInt(txtSucChua.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên dương.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (sucChua <= 0) {
			JOptionPane.showMessageDialog(this, "Sức chứa phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return new BanAnDTO(maBan, tenBan, sucChua, khuVuc, trangThai);
	}

	private void handleAdd() {
		if (!txtMaBan.isEditable()) {
			JOptionPane.showMessageDialog(this, "Bạn đang chọn một bàn để sửa. Bấm 'Làm mới' rồi nhập mã mới để thêm bàn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		BanAnDTO b = buildFromForm();
		if (b == null) {
			return;
		}

		if (new BanAnDAO().getById(b.getMaBan()) != null) {
			JOptionPane.showMessageDialog(this, "Mã bàn đã tồn tại. Vui lòng nhập mã khác.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (new BanAnDAO().insert(b)) {
			loadData();
			clearForm();
			refreshSoDoBanIfNeeded();
			showSuccessToast("Thêm bàn thành công");
		} else {
			JOptionPane.showMessageDialog(this, "Không thể thêm bàn. Mã bàn có thể đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleUpdate() {
		if (table.getSelectedRow() < 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		BanAnDTO b = buildFromForm();
		if (b == null) {
			return;
		}
		if (new BanAnDAO().update(b)) {
			loadData();
			refreshSoDoBanIfNeeded();
			showSuccessToast("Cập nhật bàn thành công");
		} else {
			JOptionPane.showMessageDialog(this, "Không thể cập nhật bàn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void handleDelete() {
		int row = table.getSelectedRow();
		if (row < 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String maBan = tableModel.getValueAt(row, 0).toString();
		int confirm = JOptionPane.showConfirmDialog(this, "Xóa bàn " + maBan + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}
		if (new BanAnDAO().delete(maBan)) {
			loadData();
			clearForm();
			refreshSoDoBanIfNeeded();
			showSuccessToast("Xóa bàn thành công");
		} else {
			JOptionPane.showMessageDialog(this, "Không thể xóa bàn. Có thể bàn đang liên kết dữ liệu hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void refreshSoDoBanIfNeeded() {
		Window w = SwingUtilities.getWindowAncestor(this);
		if (w instanceof MainFrame) {
			((MainFrame) w).refreshSoDoBan();
		}
	}

	private void showSuccessToast(String message) {
		Window w = SwingUtilities.getWindowAncestor(this);
		if (w instanceof JFrame) {
			Toast.success((JFrame) w, message);
		}
	}
}
