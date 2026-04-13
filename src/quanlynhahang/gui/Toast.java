package quanlynhahang.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp hiển thị thông báo (Toast) chuẩn UX hiện đại phong cách macOS.
 * Hỗ trợ: Đổ bóng mềm, hiệu ứng trượt mượt mà (Easing), xếp chồng thông báo (Stacking),
 * Hover để tạm dừng, Click để đóng, và Đa chủ đề (Success, Warning, Error, Info).
 */
public class Toast extends JWindow {

    // --- CÁC LOẠI THÔNG BÁO ---
    public enum ToastType {
        SUCCESS(new Color(34, 197, 94), "✓"),
        WARNING(new Color(245, 158, 11), "!"),
        ERROR(new Color(239, 68, 68), "✕"),
        INFO(new Color(59, 130, 246), "i");

        Color color;
        String iconText;

        ToastType(Color color, String iconText) {
            this.color = color;
            this.iconText = iconText;
        }
    }

    // --- QUẢN LÝ XẾP CHỒNG (STACKING) ---
    private static final List<Toast> activeToasts = new ArrayList<>();
    private static final int TOAST_MARGIN = 15; // Khoảng cách giữa các Toast

    // --- THUỘC TÍNH TOAST ---
    private String message;
    private ToastType type;
    private JFrame parent;

    // --- ANIMATION CONTROLS ---
    private float opacity = 0.0f;
    private int targetY;
    private int currentY;
    private Timer animationTimer;
    private Timer displayTimer;
    private final int displayDuration = 3000; // Thời gian hiển thị (3 giây)
    private boolean isHovered = false;

    private enum State { SLIDING_IN, VISIBLE, SLIDING_OUT }
    private State currentState = State.SLIDING_IN;

    /**
     * Khởi tạo một Toast mới.
     * @param parent Cửa sổ cha để canh giữa
     * @param message Nội dung thông báo
     * @param type Loại thông báo (SUCCESS, ERROR, WARNING, INFO)
     */
    public Toast(JFrame parent, String message, ToastType type) {
        super(parent);
        this.parent = parent;
        this.message = message;
        this.type = type;

        initComponents();
        setupMouseInteractions();
    }

    private void initComponents() {
        setBackground(new Color(0, 0, 0, 0)); // Cửa sổ trong suốt

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Kích thước thật của khối nền (Chừa lề để vẽ bóng đổ)
                int shadowSize = 10;
                int w = getWidth() - shadowSize * 2;
                int h = getHeight() - shadowSize * 2;

                // 1. Vẽ bóng đổ mềm (Soft Shadow)
                for (int i = 0; i < shadowSize; i++) {
                    float alpha = 0.1f * (1.0f - (float) i / shadowSize); // Mờ dần ra xa
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.fillRoundRect(shadowSize - i, shadowSize - i + 2, w + i * 2, h + i * 2, 20, 20);
                }

                // 2. Vẽ nền chính của Toast (Màu tối trong suốt chuẩn Dark Mode/Mac)
                g2.setColor(new Color(30, 41, 59, 240));
                g2.fillRoundRect(shadowSize, shadowSize, w, h, 15, 15);

                // 3. Vẽ Icon hình tròn có màu tương ứng
                g2.setColor(type.color);
                g2.fillOval(shadowSize + 15, shadowSize + (h - 24) / 2, 24, 24);

                // Vẽ chữ trong Icon
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textX = shadowSize + 15 + (24 - fm.stringWidth(type.iconText)) / 2;
                int textY = shadowSize + (h - 24) / 2 + fm.getAscent() + (24 - fm.getHeight()) / 2 + 1;
                g2.drawString(type.iconText, textX, textY);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        // Lable hiển thị nội dung
        JLabel lblMessage = new JLabel(message);
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblMessage.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 20)); // Căn lề né cái Shadow và Icon

        panel.add(lblMessage, BorderLayout.CENTER);
        add(panel);
        pack(); // Tự giãn theo text
    }

    /**
     * Cài đặt các tương tác vuốt, chạm của chuột.
     */
    private void setupMouseInteractions() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                setOpacity(1.0f); // Rõ 100% khi rê chuột vào
                if (displayTimer != null) displayTimer.stop(); // Tạm dừng đếm ngược
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                if (displayTimer != null) displayTimer.restart(); // Chạy lại đếm ngược
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Click để tắt ngay lập tức
                if (currentState == State.VISIBLE) {
                    startSlideOut();
                }
            }
        });
    }

    /**
     * Tính toán vị trí hiển thị và bắt đầu chu trình hiệu ứng.
     */
    public void showToast() {
        // Cập nhật hệ thống hàng đợi
        synchronized (activeToasts) {
            activeToasts.add(this);
            recalculateTargetY();
        }

        // Tọa độ X canh giữa Frame cha
        int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;

        // Trượt từ dưới lên (Cách vị trí đích 30px)
        currentY = targetY + 30;
        setLocation(x, currentY);
        setOpacity(0.0f);
        setVisible(true);

        // Hiệu ứng Fade-in và Slide-up (60 FPS)
        animationTimer = new Timer(16, e -> animate());
        animationTimer.start();
    }

    /**
     * Engine xử lý hiệu ứng vật lý.
     */
    private void animate() {
        if (currentState == State.SLIDING_IN) {
            opacity += 0.1f;
            currentY -= (currentY - targetY) * 0.2; // Easing trượt mượt

            if (opacity >= 1.0f && Math.abs(currentY - targetY) <= 1) {
                opacity = 1.0f;
                currentY = targetY;
                currentState = State.VISIBLE;
                animationTimer.stop();
                startDisplayTimer();
            }
        }
        else if (currentState == State.SLIDING_OUT) {
            opacity -= 0.1f;
            currentY -= 2; // Trượt dần lên trên khi biến mất

            if (opacity <= 0.0f) {
                opacity = 0.0f;
                animationTimer.stop();
                closeToast();
            }
        }

        // Nếu Frame cha bị di chuyển, Toast sẽ trượt theo một chút
        if (parent != null) {
            int currentX = getX();
            int targetX = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            setLocation(currentX + (targetX - currentX) / 3, currentY);
        }

        // Try-catch đề phòng OS không hỗ trợ Opacity
        try { setOpacity(opacity); } catch (Exception ignored) {}
    }

    private void startDisplayTimer() {
        displayTimer = new Timer(displayDuration, e -> {
            if (!isHovered) {
                startSlideOut();
            }
        });
        displayTimer.setRepeats(false);
        displayTimer.start();
    }

    private void startSlideOut() {
        currentState = State.SLIDING_OUT;
        if (displayTimer != null) displayTimer.stop();
        animationTimer.start();
    }

    private void closeToast() {
        dispose();
        synchronized (activeToasts) {
            activeToasts.remove(this);
            // Cập nhật lại vị trí các Toast còn lại trên màn hình
            for (Toast t : activeToasts) {
                t.recalculateTargetY();
            }
        }
    }

    /**
     * Tính toán vị trí Y của Toast dựa trên hàng đợi (Stacking).
     */
    private void recalculateTargetY() {
        int baseY = parent.getY() + parent.getHeight() - 60; // Gốc ở cạnh dưới Frame
        int newTargetY = baseY;

        synchronized (activeToasts) {
            for (int i = 0; i < activeToasts.size(); i++) {
                if (activeToasts.get(i) == this) {
                    targetY = newTargetY - getHeight();
                    break;
                }
                newTargetY -= (activeToasts.get(i).getHeight() - 20 + TOAST_MARGIN); // Trừ đi phần shadow
            }
        }

        // Nếu Toast đang hiển thị, cho phép nó trượt tới vị trí mới
        if (currentState == State.VISIBLE) {
            currentState = State.SLIDING_IN;
            animationTimer.start();
        }
    }

    // =========================================================================
    // CÁC HÀM TIỆN ÍCH STATIC ĐỂ GỌI NHANH TRONG DỰ ÁN
    // =========================================================================

    public static void success(JFrame parent, String message) {
        new Toast(parent, message, ToastType.SUCCESS).showToast();
    }

    public static void warning(JFrame parent, String message) {
        new Toast(parent, message, ToastType.WARNING).showToast();
    }

    public static void error(JFrame parent, String message) {
        new Toast(parent, message, ToastType.ERROR).showToast();
    }

    public static void info(JFrame parent, String message) {
        new Toast(parent, message, ToastType.INFO).showToast();
    }
}