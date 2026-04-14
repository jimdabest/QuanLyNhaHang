package quanlynhahang.gui;

import quanlynhahang.dao.ThongKeDAO;
import quanlynhahang.dto.DoanhThuMonDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel thống kê doanh thu theo món ăn.
 */
public class ThongKePanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final JLabel lblTongDoanhThu;
    private final JLabel lblTongSoLuong;
    private final JLabel lblMonBanChay;

    public ThongKePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(241, 245, 249));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(30, 64, 175));

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.putClientProperty("JButton.buttonType", "roundRect");
        btnRefresh.addActionListener(e -> loadData());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel summary = new JPanel(new GridLayout(1, 3, 10, 0));
        summary.setOpaque(false);

        lblTongDoanhThu = createSummaryLabel();
        lblTongSoLuong = createSummaryLabel();
        lblMonBanChay = createSummaryLabel();

        summary.add(createSummaryCard("Tổng doanh thu", lblTongDoanhThu));
        summary.add(createSummaryCard("Tổng số lượng bán", lblTongSoLuong));
        summary.add(createSummaryCard("Món bán chạy", lblMonBanChay));

        String[] cols = {"Mã món", "Tên món", "Phân loại", "SL đã bán", "Doanh thu"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(right);
        table.getColumnModel().getColumn(4).setCellRenderer(right);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(summary, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        loadData();
    }

    public void loadData() {
        ArrayList<DoanhThuMonDTO> list = new ThongKeDAO().layDoanhThuCacMon();
        tableModel.setRowCount(0);

        double tongDoanhThu = 0;
        int tongSoLuong = 0;
        String monBanChay = "-";
        int slMax = -1;

        for (DoanhThuMonDTO item : list) {
            tongDoanhThu += item.getTongDoanhThu();
            tongSoLuong += item.getTongSoLuongBan();
            if (item.getTongSoLuongBan() > slMax) {
                slMax = item.getTongSoLuongBan();
                monBanChay = item.getTenMon();
            }

            tableModel.addRow(new Object[]{
                    item.getMaMon(),
                    item.getTenMon(),
                    item.getPhanLoai(),
                    item.getTongSoLuongBan(),
                    String.format("%,.0f", item.getTongDoanhThu())
            });
        }

        lblTongDoanhThu.setText(String.format("%,.0f VNĐ", tongDoanhThu));
        lblTongSoLuong.setText(String.format("%,d", tongSoLuong));
        lblMonBanChay.setText(monBanChay);
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(71, 85, 105));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createSummaryLabel() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(15, 23, 42));
        return label;
    }
}