-- =========================================================================================
-- ĐỒ ÁN: QUẢN LÝ QUÁN NƯỚNG (POS)
-- SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU HOÀN CHỈNH (RUN FROM SCRATCH)
-- =========================================================================================

CREATE DATABASE QuanLyNhaHang;

USE QuanLyNhaHang;

-- =========================================================================================
-- PHẦN 1: TẠO CẤU TRÚC BẢNG (TABLES)
-- =========================================================================================

-- 1. Bảng Bàn Ăn
CREATE TABLE BanAn (
                       MaBan VARCHAR(10) PRIMARY KEY,
                       TenBan NVARCHAR(50) NOT NULL,
                       SucChua INT NOT NULL CHECK (SucChua > 0),
                       KhuVuc NVARCHAR(50) NOT NULL,
                       TrangThai NVARCHAR(50) DEFAULT N'Trống'
);

-- 2. Bảng Món Ăn
CREATE TABLE MonAn (
                       MaMon VARCHAR(10) PRIMARY KEY,
                       TenMon NVARCHAR(100) NOT NULL,
                       PhanLoai NVARCHAR(50) NOT NULL,
                       GiaHienTai DECIMAL(18, 0) NOT NULL CHECK (GiaHienTai >= 0),
                       TrangThaiPhucVu BIT DEFAULT 1 -- 1: Còn phục vụ, 0: Hết hàng
);

-- 3. Bảng Hạng Thành Viên
CREATE TABLE HangThanhVien (
                               MaHang VARCHAR(10) PRIMARY KEY,
                               TenHang NVARCHAR(50) NOT NULL,
                               MucGiamGia FLOAT NOT NULL CHECK (MucGiamGia >= 0),
                               DieuKienTongChiTieu DECIMAL(18, 0) NOT NULL CHECK (DieuKienTongChiTieu >= 0),
                               QuyenLoiKhac NVARCHAR(255) NULL
);

-- 4. Bảng Khách Hàng (Cha là HangThanhVien)
CREATE TABLE KhachHang (
                           MaKhachHang VARCHAR(10) PRIMARY KEY,
                           MaHang VARCHAR(10) NOT NULL,
                           TenKH NVARCHAR(100) NOT NULL,
                           SoDienThoai VARCHAR(15) UNIQUE NOT NULL,
                           TongChiTieu DECIMAL(18, 0) DEFAULT 0,
                           NgayDangKy DATE DEFAULT GETDATE(),
                           TrangThai BIT DEFAULT 1, -- 1: Hoạt động, 0: Khóa
                           FOREIGN KEY (MaHang) REFERENCES HangThanhVien(MaHang)
);

-- 5. Bảng Phiếu Đặt Bàn (Cha là KhachHang và BanAn)
CREATE TABLE PhieuDatBan (
                             MaDatBan VARCHAR(15) PRIMARY KEY,
                             MaKhachHang VARCHAR(10) NOT NULL,
                             MaBan VARCHAR(10) NOT NULL,
                             ThoiGianDat DATETIME NOT NULL,
                             ThoiGianNhanBan DATETIME NULL,
                             SoLuongKhach INT NOT NULL CHECK (SoLuongKhach > 0),
                             TrangThai NVARCHAR(50) DEFAULT N'Chờ nhận',
                             GhiChu NVARCHAR(255) NULL,
                             FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
                             FOREIGN KEY (MaBan) REFERENCES BanAn(MaBan)
);

-- 6. Bảng Hóa Đơn (Cha là BanAn và KhachHang)
CREATE TABLE HoaDon (
                        MaHoaDon VARCHAR(15) PRIMARY KEY,
                        MaBan VARCHAR(10) NOT NULL,
                        MaKhachHang VARCHAR(10) NULL, -- Cho phép NULL nếu khách vãng lai
                        ThoiGianVao DATETIME DEFAULT GETDATE(),
                        ThoiGianRa DATETIME NULL,
                        TongTien DECIMAL(18, 0) DEFAULT 0,
                        TienGiamGia DECIMAL(18, 0) DEFAULT 0,
                        ThanhTien DECIMAL(18, 0) DEFAULT 0,
                        TrangThai NVARCHAR(50) DEFAULT N'Chưa TT',
                        FOREIGN KEY (MaBan) REFERENCES BanAn(MaBan),
                        FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang)
);

-- 7. Bảng Chi Tiết Hóa Đơn (Cha là HoaDon và MonAn)
CREATE TABLE ChiTietHoaDon (
                               MaHoaDon VARCHAR(15) NOT NULL,
                               MaMon VARCHAR(10) NOT NULL,
                               SoLuong INT NOT NULL CHECK (SoLuong > 0),
                               GiaBan DECIMAL(18, 0) NOT NULL CHECK (GiaBan >= 0),
                               GhiChu NVARCHAR(100) NULL,
                               PRIMARY KEY (MaHoaDon, MaMon),
                               FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
                               FOREIGN KEY (MaMon) REFERENCES MonAn(MaMon)
);

-- 8. Bảng Lịch Sử Điểm (Cha là KhachHang và HoaDon)
CREATE TABLE LichSuDiem (
                            MaGiaoDich VARCHAR(20) PRIMARY KEY,
                            MaKhachHang VARCHAR(10) NOT NULL,
                            MaHoaDon VARCHAR(15) NULL,
                            LoaiGiaoDich NVARCHAR(50) NOT NULL,
                            SoDiemThayDoi INT NOT NULL,
                            ThoiGianGiaoDich DATETIME DEFAULT GETDATE(),
                            GhiChu NVARCHAR(255) NULL,
                            FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
                            FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon)
);

-- =========================================================================================
-- PHẦN 2: CHÈN DỮ LIỆU MẪU (MOCK DATA)
-- =========================================================================================

INSERT INTO BanAn (MaBan, TenBan, SucChua, KhuVuc, TrangThai) VALUES
                                                                  ('B01', N'Bàn 01', 4, N'Tầng 1', N'Trống'),
                                                                  ('B02', N'Bàn 02', 4, N'Tầng 1', N'Đang ăn'),
                                                                  ('B03', N'Bàn 03', 6, N'Ngoài trời', N'Trống'),
                                                                  ('VIP01', N'Phòng VIP 1', 10, N'Tầng 2', N'Đã đặt');

INSERT INTO MonAn (MaMon, TenMon, PhanLoai, GiaHienTai, TrangThaiPhucVu) VALUES
                                                                             ('M01', N'Ba chỉ bò Mỹ cuộn nấm', N'Thịt nướng', 89000, 1),
                                                                             ('M02', N'Dẻ sườn bò tươi', N'Thịt nướng', 129000, 1),
                                                                             ('M03', N'Bạch tuộc sa tế', N'Hải sản', 99000, 1),
                                                                             ('M04', N'Combo Thịt nướng gia đình', N'Combo', 399000, 1),
                                                                             ('M05', N'Nấm kim châm', N'Đồ ăn kèm', 30000, 1),
                                                                             ('M06', N'Nước ngọt Coca Cola', N'Nước uống', 20000, 1),
                                                                             ('M07', N'Thịt bò Wagyu A5', N'Thịt nướng', 850000, 0);

INSERT INTO HangThanhVien (MaHang, TenHang, MucGiamGia, DieuKienTongChiTieu, QuyenLoiKhac) VALUES
                                                                                               ('H01', N'Thành viên Đồng', 0.0, 0, N'Tích điểm cơ bản'),
                                                                                               ('H02', N'Thành viên Bạc', 0.05, 2000000, N'Giảm 5% tổng bill'),
                                                                                               ('H03', N'Thành viên Vàng', 0.10, 5000000, N'Giảm 10% tổng bill, tặng tráng miệng'),
                                                                                               ('H04', N'Thành viên Kim Cương', 0.15, 15000000, N'Giảm 15%, ưu tiên đặt phòng VIP');

INSERT INTO KhachHang (MaKhachHang, MaHang, TenKH, SoDienThoai, TongChiTieu, NgayDangKy) VALUES
                                                                                             ('KH01', 'H03', N'Phạm Nguyễn', '0901234567', 5500000, '2025-10-15'),
                                                                                             ('KH02', 'H02', N'Nguyễn Thái Lan', '0987654321', 2500000, '2026-01-20'),
                                                                                             ('KH03', 'H01', N'Nguyễn Vũ Bảo', '0911222333', 500000, '2026-03-01');

INSERT INTO PhieuDatBan (MaDatBan, MaKhachHang, MaBan, ThoiGianDat, ThoiGianNhanBan, SoLuongKhach, TrangThai, GhiChu) VALUES
    ('DB001', 'KH01', 'VIP01', '2026-03-21 19:00:00', NULL, 8, N'Chờ nhận', N'Xếp gần cửa sổ, chuẩn bị ghế trẻ em');

INSERT INTO HoaDon (MaHoaDon, MaBan, MaKhachHang, ThoiGianVao, ThoiGianRa, TongTien, TienGiamGia, ThanhTien, TrangThai) VALUES
                                                                                                                            ('HD001', 'B01', NULL,   '2026-03-20 18:00:00', '2026-03-20 19:30:00', 318000, 0, 318000, N'Đã TT'),
                                                                                                                            ('HD002', 'B02', 'KH02', '2026-03-21 11:00:00', NULL, 307000, 15350, 291650, N'Chưa TT');

INSERT INTO ChiTietHoaDon (MaHoaDon, MaMon, SoLuong, GiaBan, GhiChu) VALUES
                                                                         ('HD001', 'M02', 2, 129000, NULL),
                                                                         ('HD001', 'M06', 3, 20000,  N'Thêm đá'),
                                                                         ('HD002', 'M01', 1, 89000, N'Thái mỏng'),
                                                                         ('HD002', 'M03', 2, 99000, N'Cay nhiều'),
                                                                         ('HD002', 'M06', 1, 20000, NULL);

INSERT INTO LichSuDiem (MaGiaoDich, MaKhachHang, MaHoaDon, LoaiGiaoDich, SoDiemThayDoi, ThoiGianGiaoDich, GhiChu) VALUES
                                                                                                                      ('GD_001', 'KH01', NULL, N'Tặng điểm hệ thống', 100, '2026-01-01 00:00:00', N'Tặng điểm dịp Năm Mới'),
                                                                                                                      ('GD_002', 'KH02', NULL, N'Tích điểm', 25, '2026-01-20 20:00:00', N'Tích điểm lần đầu mở thẻ');


-- =========================================================================================
-- PHẦN 3: CÁC HÀM (FUNCTIONS)
-- =========================================================================================

-- Hàm 1: Tính thành tiền sau khi trừ giảm giá (Tránh số âm)
CREATE FUNCTION fn_TinhThanhTienSauGiam (
    @TongTien DECIMAL(18, 0),
    @TienGiamGia DECIMAL(18, 0)
)
    RETURNS DECIMAL(18, 0)
AS
BEGIN
    DECLARE @ThanhTien DECIMAL(18, 0);
    SET @ThanhTien = @TongTien - @TienGiamGia;
    IF @ThanhTien < 0 SET @ThanhTien = 0;
RETURN @ThanhTien;
END;

-- Hàm 2: Lấy mức giảm giá của khách hàng dựa trên Hạng Thành Viên
CREATE FUNCTION fn_LayGiamGiaKhachHang (@MaKH VARCHAR(10))
    RETURNS FLOAT
AS
BEGIN
    DECLARE @GiamGia FLOAT;
SELECT @GiamGia = h.MucGiamGia
FROM KhachHang k JOIN HangThanhVien h ON k.MaHang = h.MaHang
WHERE k.MaKhachHang = @MaKH;
RETURN ISNULL(@GiamGia, 0);
END;

-- Hàm 3 (MỚI): Tính Tổng Tiền của 1 Hóa Đơn (Số lượng * Đơn giá)
CREATE FUNCTION fn_TongTienHoaDon (@MaHoaDon VARCHAR(15))
    RETURNS DECIMAL(18, 0)
AS
BEGIN
    DECLARE @TongTien DECIMAL(18, 0);

SELECT @TongTien = SUM(SoLuong * GiaBan)
FROM ChiTietHoaDon
WHERE MaHoaDon = @MaHoaDon;

-- Nếu hóa đơn chưa có món nào thì trả về 0
RETURN ISNULL(@TongTien, 0);
END;

-- =========================================================================================
-- PHẦN 4: CÁC KHUNG NHÌN (VIEWS)
-- =========================================================================================

-- View 1: Lấy chi tiết các bàn đang có khách ngồi ăn
CREATE VIEW v_ChiTietBanDangDung AS
SELECT
    b.MaBan,
    b.TenBan,
    b.KhuVuc,
    h.MaHoaDon,
    ISNULL(k.TenKH, N'Khách vãng lai') AS TenKhachHang,
    h.ThoiGianVao,
    h.TongTien,
    h.TienGiamGia,
    dbo.fn_TinhThanhTienSauGiam(h.TongTien, h.TienGiamGia) AS ThanhTienTamtinh
FROM BanAn b
         JOIN HoaDon h ON b.MaBan = h.MaBan
         LEFT JOIN KhachHang k ON h.MaKhachHang = k.MaKhachHang
WHERE b.TrangThai = N'Đang ăn' AND h.TrangThai = N'Chưa TT';

-- View 2: Lọc các phiếu đặt bàn trong ngày hôm nay
CREATE VIEW v_PhieuDatBanHomNay AS
SELECT
    p.MaDatBan,
    p.MaBan,
    p.MaKhachHang,
    k.TenKH AS TenKhachHang,
    k.SoDienThoai,
    p.ThoiGianDat AS GioKhachHen,
    p.SoLuongKhach,
    p.TrangThai,
    p.GhiChu
FROM PhieuDatBan p
         INNER JOIN KhachHang k ON p.MaKhachHang = k.MaKhachHang
WHERE CAST(p.ThoiGianDat AS DATE) = CAST(GETDATE() AS DATE);

-- View 3: Danh sách Thực đơn đang phục vụ (Để hiển thị lên app Order)
CREATE VIEW v_ThucDonHienTai AS
SELECT
    MaMon,
    TenMon,
    PhanLoai,
    GiaHienTai
FROM MonAn
WHERE TrangThaiPhucVu = 1;

-- View 4: Báo cáo doanh thu theo từng món ăn
CREATE VIEW v_DoanhThuTheoMon AS
SELECT
    m.MaMon,
    m.TenMon,
    m.PhanLoai,
    SUM(c.SoLuong) AS TongSoLuongBan,
    SUM(c.SoLuong * c.GiaBan) AS TongDoanhThu
FROM MonAn m
         JOIN ChiTietHoaDon c ON m.MaMon = c.MaMon
         JOIN HoaDon h ON c.MaHoaDon = h.MaHoaDon
WHERE h.TrangThai = N'Đã TT'
GROUP BY m.MaMon, m.TenMon, m.PhanLoai;


-- =========================================================================================
-- PHẦN 5: THỦ TỤC LƯU TRỮ (STORED PROCEDURES)
-- =========================================================================================

-- SP 1: Thanh toán Hóa đơn và Tích điểm
CREATE PROCEDURE sp_ThanhToanVaTichDiem
    @MaHoaDon VARCHAR(15)
AS
BEGIN
    SET XACT_ABORT ON;
BEGIN TRY
BEGIN TRANSACTION;

        DECLARE @MaKhachHang VARCHAR(10);
        DECLARE @ThanhTien DECIMAL(18, 0);
        DECLARE @MaBan VARCHAR(10);
        DECLARE @TrangThaiHD NVARCHAR(50);

SELECT @MaKhachHang = MaKhachHang, @ThanhTien = ThanhTien, @MaBan = MaBan, @TrangThaiHD = TrangThai
FROM HoaDon WHERE MaHoaDon = @MaHoaDon;

IF @TrangThaiHD IS NULL OR @TrangThaiHD = N'Đã TT'
BEGIN
            RAISERROR(N'Lỗi: Hóa đơn không tồn tại hoặc đã thanh toán!', 16, 1);
ROLLBACK TRANSACTION;
RETURN;
END

UPDATE HoaDon SET ThoiGianRa = GETDATE(), TrangThai = N'Đã TT' WHERE MaHoaDon = @MaHoaDon;
UPDATE BanAn SET TrangThai = N'Trống' WHERE MaBan = @MaBan;

-- (Có thể viết thêm logic cộng điểm vào bảng LichSuDiem ở đây)

COMMIT TRANSACTION;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        RAISERROR(@ErrorMessage, 16, 1);
END CATCH
END;

-- SP 2: Mở bàn mới
CREATE PROCEDURE sp_MoBan
    @MaBan VARCHAR(10),
    @MaKhachHang VARCHAR(10) = NULL
AS
BEGIN
    DECLARE @MaHoaDon VARCHAR(15);
    SET @MaHoaDon = 'HD' + FORMAT(GETDATE(), 'yyMMddHHmmss');

INSERT INTO HoaDon (MaHoaDon, MaBan, MaKhachHang, ThoiGianVao, TrangThai)
VALUES (@MaHoaDon, @MaBan, @MaKhachHang, GETDATE(), N'Chưa TT');

UPDATE BanAn SET TrangThai = N'Đang ăn' WHERE MaBan = @MaBan;
END;

-- SP 3: Hủy phiếu đặt bàn
CREATE PROCEDURE sp_HuyPhieuDat
    @MaDatBan VARCHAR(15)
AS
BEGIN
    DECLARE @MaBan VARCHAR(10);
SELECT @MaBan = MaBan FROM PhieuDatBan WHERE MaDatBan = @MaDatBan;

UPDATE PhieuDatBan SET TrangThai = N'Đã hủy' WHERE MaDatBan = @MaDatBan;
UPDATE BanAn SET TrangThai = N'Trống' WHERE MaBan = @MaBan AND TrangThai = N'Đã đặt';
END;


-- =========================================================================================
-- PHẦN 6: CÁC TRIGGER TỰ ĐỘNG
-- =========================================================================================

-- Trigger 1: Tự động nâng hạng Khách Hàng khi tiêu đủ tiền
CREATE TRIGGER trg_TuDongNangHang
    ON KhachHang
    AFTER UPDATE
              AS
BEGIN
    IF UPDATE(TongChiTieu)
BEGIN
UPDATE KhachHang
SET MaHang = (
    SELECT TOP 1 h.MaHang
    FROM HangThanhVien h
    WHERE inserted.TongChiTieu >= h.DieuKienTongChiTieu
    ORDER BY h.DieuKienTongChiTieu DESC
)
    FROM KhachHang
        INNER JOIN inserted ON KhachHang.MaKhachHang = inserted.MaKhachHang;
END
END;

-- Trigger 2: Đồng bộ trạng thái Bàn Ăn khi Hóa Đơn thay đổi trạng thái
CREATE TRIGGER trg_CapNhatTrangThaiBan
    ON HoaDon
    AFTER INSERT, UPDATE
                      AS
BEGIN
UPDATE b
SET b.TrangThai = CASE
                      WHEN i.TrangThai = N'Chưa TT' THEN N'Đang ăn'
                      WHEN i.TrangThai = N'Đã TT' THEN N'Trống'
                      ELSE b.TrangThai
    END
    FROM BanAn b
    INNER JOIN inserted i ON b.MaBan = i.MaBan;
END;

-- Trigger 3 (ĐÃ CẢI TIẾN): Tự động cập nhật Tổng Tiền Hóa Đơn khi Thêm/Sửa/Xóa Món
-- Cải tiến: Sử dụng hàm fn_TongTienHoaDon để code nhìn cực kỳ gọn gàng và chuyên nghiệp.
CREATE TRIGGER trg_DongBoTongTien
    ON ChiTietHoaDon
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    -- Lấy danh sách các Mã Hóa Đơn bị tác động
WITH AffectedHoaDon AS (
    SELECT DISTINCT MaHoaDon FROM inserted
    UNION
    SELECT DISTINCT MaHoaDon FROM deleted
)

-- Cập nhật lại số tiền cho bảng HoaDon, gọi hàm fn_TongTienHoaDon để tính
UPDATE hd
SET
    hd.TongTien = dbo.fn_TongTienHoaDon(hd.MaHoaDon),
    hd.ThanhTien = CASE
                       WHEN dbo.fn_TongTienHoaDon(hd.MaHoaDon) - hd.TienGiamGia < 0 THEN 0
                       ELSE dbo.fn_TongTienHoaDon(hd.MaHoaDon) - hd.TienGiamGia
        END
    FROM HoaDon hd
    INNER JOIN AffectedHoaDon a ON hd.MaHoaDon = a.MaHoaDon;
END;


-- =========================================================================================
-- PHẦN KHU VỰC TEST & DEBUG (ĐÃ ĐƯỢC COMMENT ĐỂ CHẠY SCRIPT AN TOÀN)
-- =========================================================================================
/*
-- 1. Test Function giảm giá
SELECT dbo.fn_TinhThanhTienSauGiam(500000, 50000) AS ThanhTienThucTe;

-- 2. Test View Bàn đang sử dụng
SELECT * FROM v_ChiTietBanDangDung;

-- 3. Test View Thực đơn & Doanh thu mới tạo
SELECT * FROM v_ThucDonHienTai;
SELECT * FROM v_DoanhThuTheoMon;

-- 4. Test SP Thanh toán
EXEC sp_ThanhToanVaTichDiem @MaHoaDon = 'HD002';
SELECT TrangThai FROM BanAn WHERE MaBan = 'B02';

-- 5. Test Trigger Nâng hạng
UPDATE KhachHang SET TongChiTieu = 15500000 WHERE MaKhachHang = 'KH03';
SELECT TenKH, MaHang, TongChiTieu FROM KhachHang WHERE MaKhachHang = 'KH03';

-- 6. Test SP Mở Bàn
EXEC sp_MoBan 'B01', 'KH01';
SELECT TOP 1 * FROM HoaDon ORDER BY ThoiGianVao DESC;

-- 7. Test Trigger Tính Tổng Tiền (gọi hàm fn_TongTienHoaDon)
INSERT INTO HoaDon (MaHoaDon, MaBan, TrangThai) VALUES ('HD_TEST_01', 'B03', N'Chưa TT');
INSERT INTO ChiTietHoaDon (MaHoaDon, MaMon, SoLuong, GiaBan) VALUES ('HD_TEST_01', 'M01', 2, 89000);
SELECT MaHoaDon, TongTien, ThanhTien FROM HoaDon WHERE MaHoaDon = 'HD_TEST_01'; -- Phải ra 178,000
*/