# Dự án Quản lý Thiết bị Phòng học (Vanilla PHP)

Đây là một ứng dụng web được xây dựng bằng **Vanilla PHP (PHP thuần)**, được tạo ra cho mục đích giáo dục bằng cách di dời (migrate) từ một dự án Java Spring Boot. Ứng dụng giúp quản lý hiệu quả các thiết bị trong các phòng học, phòng thí nghiệm và phòng đào tạo, sử dụng một kiến trúc **Nginx + PHP-FPM + MySQL** được đóng gói hoàn toàn trong Docker.

## ✨ Tính năng nổi bật

  - **Quản lý Thiết bị Toàn diện**: Thêm, sửa, xóa và xem chi tiết danh sách thiết bị (CRUD).
  - **Tìm kiếm & Lọc Linh hoạt**: Dễ dàng tìm kiếm thiết bị theo tên và lọc danh sách thiết bị theo từng phòng học cụ thể.
  - **Validation Dữ liệu Chặt chẽ**: Đảm bảo tính toàn vẹn dữ liệu với validation cả ở Frontend (HTML5) và Backend (PHP).
  - **Quản lý Kho & Phân bổ**: Cho phép gán thiết bị vào các phòng học cụ thể hoặc lưu trữ trong "Kho" trung tâm (logic nghiệp vụ được giữ nguyên).
  - **Giao diện Hiện đại & Responsive**: Giao diện người dùng được thiết kế bằng Bootstrap, thân thiện và tương thích với nhiều kích thước màn hình.
  - **Bảo mật Tăng cường**:
      - Chống **SQL Injection** bằng cách sử dụng **Prepared Statements** (PDO).
      - Chống **XSS (Cross-Site Scripting)** bằng cách sử dụng `htmlspecialchars` trên mọi dữ liệu đầu ra.
      - Chống **CSRF (Cross-Site Request Forgery)** bằng cách sử dụng `$_SESSION` token cho mọi form `POST` (lưu và xóa).
      - 🔐 Chạy trên giao thức **HTTPS** an toàn với chứng chỉ tự ký (self-signed certificate) cho môi trường phát triển.
  - **Triển khai Dễ dàng với Docker**: Đóng gói toàn bộ ứng dụng (Nginx + PHP-FPM + MySQL Database) vào Docker Compose, cho phép chạy dự án chỉ với một lệnh duy nhất.

## 🛠️ Công nghệ sử dụng

  - **Backend**: PHP 8.2, Nginx (Web Server), PHP-FPM
  - **Frontend**: PHP Templates (dạng thuần), HTML5, CSS3, Bootstrap 5, JavaScript
  - **Cơ sở dữ liệu**: MySQL 8.0
  - **Build & Deployment**: Docker, Docker Compose

## 🚀 Cài đặt và Chạy dự án

### Yêu cầu

  - **Docker** và **Docker Compose** đã được cài đặt trên máy.

### Hướng dẫn chạy bằng Docker Compose (Khuyến khích)

Đây là cách nhanh nhất để khởi chạy toàn bộ dự án với HTTPS đã được cấu hình sẵn.

1.  **Clone repository về máy**:

    ```bash
    git clone https://github.com/TuDaoLW/quan_ly_thiet_bi --branch php_ssl
    cd quan_ly_thiet_bi
    ```

2.  **(Tùy chọn) Tạo chứng chỉ HTTPS (Self-signed Certificate)**:
    Chạy các lệnh sau từ thư mục gốc của dự án để tạo file `cert.pem` và `key.pem` và đặt chúng vào đúng vị trí mà Nginx sẽ sử dụng.

    ```bash
    # Tạo thư mục ssl nếu chưa tồn tại
    mkdir -p nginx/ssl

    # Tạo chứng chỉ
    openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
      -keyout ./nginx/ssl/key.pem \
      -out ./nginx/ssl/cert.pem \
      -subj "/CN=localhost/O=MyOrg/C=VN"
    ```

    **CÓ THỂ BỎ QUA VÀ SỬ DỤNG CERTIFICATE ĐƯỢC TẠO SẴN TRONG REPO (NẾU CÓ)**

3.  **Khởi chạy ứng dụng với Docker Compose**:

    ```bash
    docker compose up --build -d
    ```

    Lệnh này sẽ tự động:

      - Build Docker image cho `php` (đã bao gồm `pdo_mysql`).
      - Khởi tạo container `mysql` với dữ liệu mẫu từ `db/init.sql`.
      - Khởi động `nginx` và `php-fpm`, chạy ứng dụng trên cổng **8443** (HTTPS) và **8080** (HTTP, tự động redirect sang HTTPS).

4.  **Truy cập ứng dụng**:
    Mở trình duyệt và truy cập vào địa chỉ:
    [https://localhost:8443](https://www.google.com/search?q=https://localhost:8443)

### ⚠️ Cảnh báo Bảo mật

Vì bạn đang sử dụng chứng chỉ tự ký, trình duyệt sẽ hiển thị cảnh báo **"Your connection is not private"**. Đây là điều bình thường. Hãy nhấn **"Advanced"** -\> **"Proceed to localhost (unsafe)"** để tiếp tục.

## 📖 Chức năng Chi tiết

### 1\. Quản lý Thiết bị (Router Logic)

Tất cả các yêu cầu được điều hướng qua `index.php` bằng cách sử dụng tham số `action`.

| **Chức năng** | **Method** | **Đường dẫn (ví dụ)** | **Mô tả** |
| :--- | :--- | :--- | :--- |
| Hiển thị danh sách | GET | `index.php?action=list` | Hiển thị toàn bộ thiết bị, hỗ trợ tìm kiếm và lọc. |
| Tìm kiếm/Lọc | GET | `index.php?action=list&keyword=...&id_phong=...` | Lọc danh sách thiết bị. |
| Form thêm mới | GET | `index.php?action=add` | Hiển thị form để nhập thông tin thiết bị mới. |
| Lưu thiết bị | POST | `index.php?action=save` | Lưu thông tin thiết bị mới hoặc cập nhật thiết bị đã có. |
| Form chỉnh sửa | GET | `index.php?action=edit&id={id}` | Lấy thông tin thiết bị theo id và hiển thị trên form. |
| Xóa thiết bị | POST | `index.php?action=delete&id={id}` | Xóa thiết bị khỏi hệ thống (có xác nhận và check CSRF). |

### 2\. Validation và Ràng buộc

Các logic validation từ dự án Java đã được tái triển khai ở phía server (PHP) trong `index.php` (case `action=save`).

  - **Tên thiết bị**:
      - Bắt buộc, từ 3 đến 100 ký tự.
  - **Mã thiết bị**:
      - Bắt buộc, duy nhất, chỉ chứa chữ cái, số, và dấu gạch ngang (`[A-Za-z0-9-]+`).
  - **Số lượng**:
      - Bắt buộc, phải là số nguyên lớn hơn 0.
  - **Tình trạng**:
      - Bắt buộc, phải là một trong các giá trị: "Tốt", "Cần bảo trì", "Hỏng".
  - **Ngày lắp đặt**:
      - Bắt buộc khi thiết bị được gán cho một phòng học (khác Kho).
      - Không được là một ngày trong tương lai.
      - Để trống (`NULL`) nếu thiết bị nằm trong kho (K00).

### 3\. Quản lý Phòng học

  - Dữ liệu phòng học được tải và hiển thị trong các menu dropdown để người dùng dễ dàng lựa chọn.
  - Hệ thống định nghĩa một phòng đặc biệt là **"Kho"** (mã `K00`) để chứa các thiết bị chưa được lắp đặt. Logic này được xử lý trong `index.php` (case `action=save`).
