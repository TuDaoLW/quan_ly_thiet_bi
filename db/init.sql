-- =====================================================
-- HỆ THỐNG QUẢN LÝ PHÒNG HỌC
-- Database Schema với dữ liệu mẫu
-- =====================================================
SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci';
-- Xóa database nếu tồn tại và tạo mới
DROP DATABASE IF EXISTS quan_ly_phong_hoc;
CREATE DATABASE quan_ly_phong_hoc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quan_ly_phong_hoc;

-- =====================================================
-- 1. BẢNG VAI_TRO (Roles/Permissions)
-- =====================================================
CREATE TABLE VAI_TRO (
    id_vai_tro INT PRIMARY KEY AUTO_INCREMENT,
    ten_vai_tro VARCHAR(50) NOT NULL UNIQUE,
    mo_ta TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu vai trò
INSERT INTO VAI_TRO (ten_vai_tro, mo_ta) VALUES
('Quản trị viên', 'Toàn quyền quản lý hệ thống, thêm/sửa/xóa người dùng, phòng học, thiết bị'),
('Giáo viên', 'Đăng ký sử dụng phòng, xem thông tin phòng và thiết bị'),
('Phòng ĐT', 'Quản lý phòng đào tạo, xem tình trạng phòng, phê duyệt đăng ký');

-- =====================================================
-- 2. BẢNG NGUOI_DUNG (Users)
-- =====================================================
CREATE TABLE NGUOI_DUNG (
    id_nguoi_dung INT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    sdt VARCHAR(15),
    id_vai_tro INT NOT NULL,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    trang_thai BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_vai_tro) REFERENCES VAI_TRO(id_vai_tro),
    INDEX idx_email (email),
    INDEX idx_vai_tro (id_vai_tro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu người dùng (mật khẩu: password123 đã được hash)
INSERT INTO NGUOI_DUNG (ho_ten, email, mat_khau, sdt, id_vai_tro) VALUES
('Nguyễn Văn An', 'admin@actvn.edu.vn', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0901234567', 1),
('Vũ Thị Vân', 'vuvan@actvn.edu.vn', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0912345678', 2),
('Lê Văn Cường', 'lecuong@actvn.edu.vn', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0923456789', 2),
('Phạm Thị Dung', 'phamdt@actvn.edu.vn', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0934567890', 3);

-- =====================================================
-- 3. BẢNG LOAI_PHONG (Room Types)
-- =====================================================
CREATE TABLE LOAI_PHONG (
    id_loai_phong INT PRIMARY KEY AUTO_INCREMENT,
    ten_loai VARCHAR(50) NOT NULL UNIQUE,
    mo_ta TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu loại phòng
INSERT INTO LOAI_PHONG (ten_loai, mo_ta) VALUES
('Phòng học thường', 'Phòng học lý thuyết, giảng dạy thông thường'),
('Phòng thí nghiệm', 'Phòng thực hành, thí nghiệm với thiết bị chuyên dụng'),
('Phòng đào tạo', 'Phòng dành cho các khóa đào tạo, có kế hoạch học tập');

-- =====================================================
-- 4. BẢNG phong_hoc (Rooms)
-- =====================================================
CREATE TABLE phong_hoc (
    id_phong INT PRIMARY KEY AUTO_INCREMENT,
    ma_phong VARCHAR(20) UNIQUE NOT NULL,
    ten_phong VARCHAR(100) NOT NULL,
    suc_chua INT,
    vi_tri VARCHAR(200),
    id_loai_phong INT NOT NULL,
    trang_thai ENUM('Trống', 'Đang sử dụng', 'Bảo trì') DEFAULT 'Trống',
    mo_ta TEXT,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_loai_phong) REFERENCES LOAI_PHONG(id_loai_phong),
    INDEX idx_ma_phong (ma_phong),
    INDEX idx_trang_thai (trang_thai),
    INDEX idx_loai_phong (id_loai_phong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu phòng học
INSERT INTO phong_hoc (ma_phong, ten_phong, suc_chua, vi_tri, id_loai_phong, trang_thai, mo_ta) VALUES
('A101', 'Phòng học A101', 50, 'Tầng 1, Nhà A', 1, 'Trống', 'Phòng học lý thuyết có máy chiếu'),
('A102', 'Phòng học A102', 45, 'Tầng 1, Nhà A', 1, 'Trống', 'Phòng học có điều hòa'),
('B201', 'Phòng thí nghiệm Hóa học', 30, 'Tầng 2, Nhà B', 2, 'Trống', 'Phòng TN hóa học với đầy đủ thiết bị'),
('B202', 'Phòng thí nghiệm Vật lý', 35, 'Tầng 2, Nhà B', 2, 'Bảo trì', 'Phòng TN vật lý, đang bảo trì hệ thống điện'),
('C301', 'Phòng đào tạo C301', 40, 'Tầng 3, Nhà C', 3, 'Trống', 'Phòng dành cho khóa đào tạo dài hạn'),
('K00', 'Nhà kho thiết bị', 0, 'Tầng 1, Nhà A', 2, 'Đang sử dụng', 'Phòng chứa thiết bị chưa dùng'),
('C302', 'Phòng đào tạo C302', 38, 'Tầng 3, Nhà C', 3, 'Đang sử dụng', 'Phòng đang có khóa học');

-- =====================================================
-- 5. BẢNG PHONG_TB (Lab Rooms)
-- =====================================================
CREATE TABLE PHONG_TB (
    id_phong_tb INT PRIMARY KEY AUTO_INCREMENT,
    id_phong INT UNIQUE NOT NULL,
    so_sinh_vien_toi_da INT,
    loai_thiet_bi TEXT,
    FOREIGN KEY (id_phong) REFERENCES phong_hoc(id_phong) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu phòng thí nghiệm
INSERT INTO PHONG_TB (id_phong, so_sinh_vien_toi_da, loai_thiet_bi) VALUES
(3, 30, 'Bếp điện, ống nghiệm, cân điện tử, tủ hút'),
(4, 35, 'Máy đo dao động, nguồn điện, ampe kế, vôn kế');

-- =====================================================
-- 6. BẢNG PHONG_DT (Training Rooms)
-- =====================================================
CREATE TABLE PHONG_DT (
    id_phong_dt INT PRIMARY KEY AUTO_INCREMENT,
    id_phong INT UNIQUE NOT NULL,
    chuyen_nganh VARCHAR(100),
    FOREIGN KEY (id_phong) REFERENCES phong_hoc(id_phong) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu phòng đào tạo
INSERT INTO PHONG_DT (id_phong, chuyen_nganh) VALUES
(5, 'Công nghệ thông tin'),
(6, 'Kế toán - Tài chính');

-- =====================================================
-- 7. BẢNG thiet_bi (Equipment)
-- =====================================================
CREATE TABLE thiet_bi (
    id_thiet_bi INT PRIMARY KEY AUTO_INCREMENT,
    id_phong INT NOT NULL,
    ten_thiet_bi VARCHAR(100) NOT NULL,
    ma_thiet_bi VARCHAR(50) UNIQUE NOT NULL,
    so_luong INT DEFAULT 1,
    tinh_trang ENUM('Tốt', 'Cần bảo trì', 'Hỏng') DEFAULT 'Tốt',
    ngay_lap_dat DATE,
    mo_ta TEXT,
    FOREIGN KEY (id_phong) REFERENCES phong_hoc(id_phong) ON DELETE CASCADE,
    INDEX idx_ma_thiet_bi (ma_thiet_bi),
    INDEX idx_phong (id_phong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu thiết bị
INSERT INTO thiet_bi (id_phong, ten_thiet_bi, ma_thiet_bi, so_luong, tinh_trang, ngay_lap_dat, mo_ta) VALUES
(1, 'Máy chiếu', 'MC-A101-001', 1, 'Tốt', '2023-01-15', 'Máy chiếu Epson EB-X41'),
(1, 'Bảng tương tác', 'BT-A101-001', 1, 'Tốt', '2023-01-15', 'Bảng tương tác 85 inch'),
(2, 'Máy chiếu', 'MC-A102-001', 1, 'Tốt', '2023-02-20', 'Máy chiếu Sony VPL-DX221'),
(2, 'Điều hòa', 'DH-A102-001', 2, 'Tốt', '2023-02-20', 'Điều hòa Daikin 2 chiều'),
(3, 'Bếp điện', 'BD-B201-001', 10, 'Tốt', '2022-09-10', 'Bếp điện đơn công suất 1000W'),
(3, 'Cân điện tử', 'CD-B201-001', 5, 'Tốt', '2022-09-10', 'Cân điện tử chính xác 0.01g'),
(4, 'Nguồn điện', 'ND-B202-001', 8, 'Cần bảo trì', '2022-08-15', 'Nguồn điện DC 0-30V'),
(5, 'Máy tính', 'MT-C301-001', 40, 'Tốt', '2023-03-01', 'Dell Optiplex 7090'),
(6, 'Máy tính', 'MT-C302-001', 38, 'Tốt', '2023-04-15', 'HP ProDesk 400 G7');

-- =====================================================
-- 8. BẢNG KE_HOACH_HOC_TAP (Study Plans)
-- =====================================================
CREATE TABLE KE_HOACH_HOC_TAP (
    id_ke_hoach INT PRIMARY KEY AUTO_INCREMENT,
    id_phong_dt INT NOT NULL,
    ten_khoa_hoc VARCHAR(200) NOT NULL,
    ngay_bat_dau DATE,
    ngay_ket_thuc DATE,
    file_excel_url VARCHAR(500),
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_phong_dt) REFERENCES PHONG_DT(id_phong_dt) ON DELETE CASCADE,
    INDEX idx_ngay_bat_dau (ngay_bat_dau),
    INDEX idx_phong_dt (id_phong_dt)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu kế hoạch học tập
INSERT INTO KE_HOACH_HOC_TAP (id_phong_dt, ten_khoa_hoc, ngay_bat_dau, ngay_ket_thuc, file_excel_url) VALUES
(1, 'Lập trình Web với PHP và MySQL', '2025-01-10', '2025-03-15', '/uploads/ke-hoach/web-php-mysql.xlsx'),
(2, 'Kế toán tài chính cơ bản', '2025-02-01', '2025-04-30', '/uploads/ke-hoach/ke-toan-co-ban.xlsx');

-- =====================================================
-- 9. BẢNG DANG_KY_PHONG (Room Registrations)
-- =====================================================
CREATE TABLE DANG_KY_PHONG (
    id_dang_ky INT PRIMARY KEY AUTO_INCREMENT,
    id_nguoi_dung INT NOT NULL,
    id_phong INT NOT NULL,
    ngay_su_dung DATE NOT NULL,
    gio_bat_dau TIME NOT NULL,
    gio_ket_thuc TIME NOT NULL,
    muc_dich TEXT,
    trang_thai ENUM('Chờ duyệt', 'Đã duyệt', 'Từ chối', 'Hủy') DEFAULT 'Chờ duyệt',
    ngay_dang_ky DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_nguoi_dung) REFERENCES NGUOI_DUNG(id_nguoi_dung),
    FOREIGN KEY (id_phong) REFERENCES phong_hoc(id_phong),
    CHECK (gio_ket_thuc > gio_bat_dau),
    INDEX idx_ngay_su_dung (ngay_su_dung),
    INDEX idx_trang_thai_dk (trang_thai),
    INDEX idx_nguoi_dung (id_nguoi_dung),
    INDEX idx_phong_dk (id_phong)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu đăng ký phòng
INSERT INTO DANG_KY_PHONG (id_nguoi_dung, id_phong, ngay_su_dung, gio_bat_dau, gio_ket_thuc, muc_dich, trang_thai) VALUES
(2, 1, '2025-10-10', '08:00:00', '10:00:00', 'Giảng dạy môn Toán học', 'Đã duyệt'),
(3, 2, '2025-10-10', '13:00:00', '15:00:00', 'Giảng dạy môn Vật lý', 'Đã duyệt'),
(2, 1, '2025-10-15', '14:00:00', '16:00:00', 'Ôn tập cuối kỳ', 'Chờ duyệt'),
(3, 3, '2025-10-12', '09:00:00', '11:00:00', 'Thí nghiệm hóa học', 'Từ chối');

-- =====================================================
-- 10. BẢNG PHE_DUYET (Approvals)
-- =====================================================
CREATE TABLE PHE_DUYET (
    id_phe_duyet INT PRIMARY KEY AUTO_INCREMENT,
    id_dang_ky INT NOT NULL,
    id_nguoi_phe_duyet INT NOT NULL,
    ngay_phe_duyet DATETIME DEFAULT CURRENT_TIMESTAMP,
    ket_qua ENUM('Chấp nhận', 'Từ chối') NOT NULL,
    ghi_chu TEXT,
    FOREIGN KEY (id_dang_ky) REFERENCES DANG_KY_PHONG(id_dang_ky) ON DELETE CASCADE,
    FOREIGN KEY (id_nguoi_phe_duyet) REFERENCES NGUOI_DUNG(id_nguoi_dung),
    INDEX idx_dang_ky (id_dang_ky),
    INDEX idx_nguoi_phe_duyet (id_nguoi_phe_duyet)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dữ liệu mẫu phê duyệt
INSERT INTO PHE_DUYET (id_dang_ky, id_nguoi_phe_duyet, ket_qua, ghi_chu) VALUES
(1, 4, 'Chấp nhận', 'Đã duyệt, phòng trống trong thời gian này'),
(2, 4, 'Chấp nhận', 'Đã duyệt'),
(4, 4, 'Từ chối', 'Phòng thí nghiệm đang bảo trì');

-- =====================================================
-- CÁC TRIGGER VÀ STORED PROCEDURES HỖ TRỢ
-- =====================================================

-- Trigger: Tự động cập nhật trạng thái đăng ký khi phê duyệt
DELIMITER //
CREATE TRIGGER trg_cap_nhat_trang_thai_dang_ky 
AFTER INSERT ON PHE_DUYET
FOR EACH ROW
BEGIN
    IF NEW.ket_qua = 'Chấp nhận' THEN
        UPDATE DANG_KY_PHONG 
        SET trang_thai = 'Đã duyệt' 
        WHERE id_dang_ky = NEW.id_dang_ky;
    ELSE
        UPDATE DANG_KY_PHONG 
        SET trang_thai = 'Từ chối' 
        WHERE id_dang_ky = NEW.id_dang_ky;
    END IF;
END//
DELIMITER ;

-- Stored Procedure: Kiểm tra phòng có trống trong khoảng thời gian không
DELIMITER //
CREATE PROCEDURE sp_kiem_tra_phong_trong(
    IN p_id_phong INT,
    IN p_ngay_su_dung DATE,
    IN p_gio_bat_dau TIME,
    IN p_gio_ket_thuc TIME,
    OUT p_ket_qua BOOLEAN
)
BEGIN
    DECLARE v_count INT;
    
    SELECT COUNT(*) INTO v_count
    FROM DANG_KY_PHONG
    WHERE id_phong = p_id_phong
        AND ngay_su_dung = p_ngay_su_dung
        AND trang_thai IN ('Chờ duyệt', 'Đã duyệt')
        AND (
            (p_gio_bat_dau >= gio_bat_dau AND p_gio_bat_dau < gio_ket_thuc)
            OR (p_gio_ket_thuc > gio_bat_dau AND p_gio_ket_thuc <= gio_ket_thuc)
            OR (p_gio_bat_dau <= gio_bat_dau AND p_gio_ket_thuc >= gio_ket_thuc)
        );
    
    IF v_count = 0 THEN
        SET p_ket_qua = TRUE;
    ELSE
        SET p_ket_qua = FALSE;
    END IF;
END//
DELIMITER ;

-- Stored Procedure: Lấy danh sách phòng trống theo ngày và giờ
DELIMITER //
CREATE PROCEDURE sp_lay_phong_trong(
    IN p_ngay_su_dung DATE,
    IN p_gio_bat_dau TIME,
    IN p_gio_ket_thuc TIME
)
BEGIN
    SELECT 
        ph.id_phong,
        ph.ma_phong,
        ph.ten_phong,
        ph.suc_chua,
        ph.vi_tri,
        lp.ten_loai,
        ph.trang_thai
    FROM phong_hoc ph
    JOIN LOAI_PHONG lp ON ph.id_loai_phong = lp.id_loai_phong
    WHERE ph.trang_thai = 'Trống'
        AND ph.id_phong NOT IN (
            SELECT id_phong
            FROM DANG_KY_PHONG
            WHERE ngay_su_dung = p_ngay_su_dung
                AND trang_thai IN ('Chờ duyệt', 'Đã duyệt')
                AND (
                    (p_gio_bat_dau >= gio_bat_dau AND p_gio_bat_dau < gio_ket_thuc)
                    OR (p_gio_ket_thuc > gio_bat_dau AND p_gio_ket_thuc <= gio_ket_thuc)
                    OR (p_gio_bat_dau <= gio_bat_dau AND p_gio_ket_thuc >= gio_ket_thuc)
                )
        )
    ORDER BY ph.ma_phong;
END//
DELIMITER ;

-- =====================================================
-- CÁC VIEW HỖ TRỢ TRUY VẤN
-- =====================================================

-- View: Thông tin đầy đủ phòng học
CREATE VIEW v_thong_tin_phong_hoc AS
SELECT 
    ph.id_phong,
    ph.ma_phong,
    ph.ten_phong,
    ph.suc_chua,
    ph.vi_tri,
    lp.ten_loai AS loai_phong,
    ph.trang_thai,
    ph.mo_ta,
    CASE 
        WHEN ptb.id_phong_tb IS NOT NULL THEN 'Phòng thí nghiệm'
        WHEN pdt.id_phong_dt IS NOT NULL THEN 'Phòng đào tạo'
        ELSE 'Phòng học thường'
    END AS phan_loai,
    ptb.so_sinh_vien_toi_da,
    ptb.loai_thiet_bi,
    pdt.chuyen_nganh
FROM phong_hoc ph
JOIN LOAI_PHONG lp ON ph.id_loai_phong = lp.id_loai_phong
LEFT JOIN PHONG_TB ptb ON ph.id_phong = ptb.id_phong
LEFT JOIN PHONG_DT pdt ON ph.id_phong = pdt.id_phong;

-- View: Lịch sử đăng ký phòng
CREATE VIEW v_lich_su_dang_ky AS
SELECT 
    dk.id_dang_ky,
    nd.ho_ten AS nguoi_dang_ky,
    nd.email,
    ph.ma_phong,
    ph.ten_phong,
    dk.ngay_su_dung,
    dk.gio_bat_dau,
    dk.gio_ket_thuc,
    dk.muc_dich,
    dk.trang_thai,
    dk.ngay_dang_ky,
    pd.ket_qua AS ket_qua_phe_duyet,
    nd_pd.ho_ten AS nguoi_phe_duyet,
    pd.ngay_phe_duyet,
    pd.ghi_chu
FROM DANG_KY_PHONG dk
JOIN NGUOI_DUNG nd ON dk.id_nguoi_dung = nd.id_nguoi_dung
JOIN phong_hoc ph ON dk.id_phong = ph.id_phong
LEFT JOIN PHE_DUYET pd ON dk.id_dang_ky = pd.id_dang_ky
LEFT JOIN NGUOI_DUNG nd_pd ON pd.id_nguoi_phe_duyet = nd_pd.id_nguoi_dung;

-- View: Thống kê thiết bị theo phòng
CREATE VIEW v_thong_ke_thiet_bi AS
SELECT 
    ph.id_phong,
    ph.ma_phong,
    ph.ten_phong,
    COUNT(tb.id_thiet_bi) AS tong_so_thiet_bi,
    SUM(tb.so_luong) AS tong_so_luong,
    SUM(CASE WHEN tb.tinh_trang = 'Tốt' THEN tb.so_luong ELSE 0 END) AS so_luong_tot,
    SUM(CASE WHEN tb.tinh_trang = 'Cần bảo trì' THEN tb.so_luong ELSE 0 END) AS so_luong_can_bao_tri,
    SUM(CASE WHEN tb.tinh_trang = 'Hỏng' THEN tb.so_luong ELSE 0 END) AS so_luong_hong
FROM phong_hoc ph
LEFT JOIN thiet_bi tb ON ph.id_phong = tb.id_phong
GROUP BY ph.id_phong, ph.ma_phong, ph.ten_phong;

-- =====================================================
-- CÁC QUERY MẪU HỮU ÍCH
-- =====================================================

-- Query 1: Lấy danh sách phòng trống vào ngày 2025-10-15 từ 08:00-10:00
-- CALL sp_lay_phong_trong('2025-10-15', '08:00:00', '10:00:00');

-- Query 2: Xem thông tin đầy đủ tất cả phòng học
-- SELECT * FROM v_thong_tin_phong_hoc;

-- Query 3: Xem lịch sử đăng ký của giáo viên
-- SELECT * FROM v_lich_su_dang_ky WHERE email = 'tranbinh@phong.edu.vn';

-- Query 4: Thống kê thiết bị trong các phòng
-- SELECT * FROM v_thong_ke_thiet_bi;

-- Query 5: Lấy danh sách đăng ký đang chờ phê duyệt
-- SELECT * FROM v_lich_su_dang_ky WHERE trang_thai = 'Chờ duyệt';

-- =====================================================
-- KẾT THÚC SCRIPT
-- =====================================================

SELECT 'Database đã được tạo thành công!' AS message;