# Dự án Quản lý Thiết bị Phòng học (Java Thymeleaf)

Đây là một ứng dụng web được xây dựng bằng **Java Spring Boot** và **Thymeleaf**, giúp quản lý hiệu quả các thiết bị trong các phòng học, phòng thí nghiệm và phòng đào tạo. Ứng dụng cung cấp giao diện người dùng hiện đại, dễ sử dụng cùng với một backend mạnh mẽ, bảo mật và có khả năng mở rộng.

## 🖼️ Hình ảnh minh họa

<table>
  <tr>
    <td align="center"><strong>Trang danh sách thiết bị</strong></td>
    <td align="center"><strong>Form thêm/sửa thiết bị</strong></td>
  </tr>
  <tr>
    <td><img src="./img/danh-sach-thiet-bi.png" alt="Trang danh sách thiết bị" /></td>
    <td><img src="./img/form-thiet-bi.png" alt="Form thêm/sửa thiết bị" /></td>
  </tr>
  <tr>
    <td align="center"><strong>Xem thiết bị theo phòng</strong></td>
    <td align="center"><strong>Thông báo lỗi validation</strong></td>
  </tr>
  <tr>
    <td><img src="./img/thiet-bi-theo-phong.png" alt="Xem thiết bị theo phòng" /></td>
    <td><img src="./img/validation-error.png" alt="Thông báo lỗi validation" /></td>
  </tr>
</table>

## ✨ Tính năng nổi bật

-   **Quản lý Thiết bị Toàn diện:** Thêm, sửa, xóa và xem chi tiết danh sách thiết bị.
-   **Tìm kiếm & Lọc Linh hoạt:** Dễ dàng tìm kiếm thiết bị theo tên và lọc danh sách thiết bị theo từng phòng học cụ thể thông qua menu dropdown.
-   **Validation Dữ liệu Chặt chẽ:** Đảm bảo tính toàn vẹn dữ liệu với validation cả ở Frontend (HTML5, JavaScript) và Backend (Jakarta Validation).
-   **Quản lý Kho & Phân bổ:** Cho phép gán thiết bị vào các phòng học cụ thể hoặc lưu trữ trong "Kho" trung tâm khi chưa được lắp đặt.
-   **Giao diện Hiện đại & Responsive:** Giao diện người dùng được thiết kế bằng Bootstrap, thân thiện và tương thích với nhiều kích thước màn hình.
-   **Bảo mật Tăng cường:** Tích hợp các biện pháp chống lại các lỗ hổng phổ biến như SQL Injection, XSS và sử dụng **CSRF token** cho các yêu cầu POST để đảm bảo an toàn.
-   **Triển khai Dễ dàng với Docker:** Đóng gói toàn bộ ứng dụng (Spring Boot App + MySQL Database) vào **Docker Compose**, cho phép chạy dự án chỉ với một lệnh duy nhất.
-   **Logging Chi tiết:** Sử dụng SLF4J để ghi lại các hoạt động quan trọng và lỗi phát sinh trong hệ thống.
-   **Điều hướng Thông minh:** Tự động chuyển hướng tất cả các đường dẫn không hợp lệ về trang danh sách thiết bị chính (`/thietbi`).

## 🛠️ Công nghệ sử dụng

-   **Backend:** Java 17+, Spring Boot, Spring Data JPA, Jakarta Validation, SLF4J
-   **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 5, JavaScript
-   **Cơ sở dữ liệu:** MySQL
-   **Build & Deployment:** Maven, Docker, Docker Compose

## 🚀 Cài đặt và Chạy dự án

### Yêu cầu

-   [Docker](https://www.docker.com/get-started) và [Docker Compose](https://docs.docker.com/compose/install/) đã được cài đặt trên máy.

### Hướng dẫn chạy bằng Docker Compose (Khuyến khích)

Đây là cách nhanh nhất và đơn giản nhất để khởi chạy dự án mà không cần cài đặt MySQL hay cấu hình môi trường thủ công.

1.  **Clone repository về máy:**
    ```bash
    git clone <your-repository-url>
    cd <your-project-directory>
    ```

2.  **Khởi chạy ứng dụng với Docker Compose:**
    ```bash
    docker-compose up --build -d
    ```
    Lệnh này sẽ tự động build image cho ứng dụng Spring Boot và khởi tạo một container MySQL với dữ liệu mẫu.

3.  **Truy cập ứng dụng:**
    Mở trình duyệt và truy cập vào địa chỉ: `http://localhost:8080/thietbi`

### Hướng dẫn chạy thủ công

1.  **Cài đặt MySQL:**
    -   Đảm bảo bạn đã cài đặt và khởi động MySQL server.
    -   Tạo một database mới (ví dụ: `quanlythietbi`).
    -   Chạy các script SQL trong project (nếu có) để tạo bảng và chèn dữ liệu mẫu.

2.  **Cấu hình kết nối:**
    -   Mở file `src/main/resources/application.properties`.
    -   Cập nhật các thông tin `spring.datasource.url`, `spring.datasource.username`, và `spring.datasource.password` cho phù hợp với cấu hình MySQL của bạn.

3.  **Build và chạy ứng dụng:**
    Sử dụng Maven để build và khởi chạy ứng dụng:
    ```bash
    mvn spring-boot:run
    ```

4.  **Truy cập ứng dụng:**
    Mở trình duyệt và truy cập vào địa chỉ: `http://localhost:8080/thietbi`

## 📖 Chức năng Chi tiết

### 1. Quản lý Thiết bị

| Chức năng | Method | Đường dẫn | Mô tả |
| :--- | :---: | :--- | :--- |
| **Hiển thị danh sách** | `GET` | `/thietbi` | Hiển thị toàn bộ thiết bị, hỗ trợ tìm kiếm theo `keyword`. |
| **Form thêm mới** | `GET` | `/thietbi/add` | Hiển thị form để nhập thông tin thiết bị mới. |
| **Lưu thiết bị** | `POST` | `/thietbi/save` | Lưu thông tin thiết bị mới hoặc cập nhật thiết bị đã có. |
| **Form chỉnh sửa** | `GET` | `/thietbi/edit/{id}` | Lấy thông tin thiết bị theo `id` và hiển thị trên form. |
| **Xóa thiết bị** | `GET` | `/thietbi/delete/{id}` | Xóa thiết bị khỏi hệ thống (có xác nhận từ người dùng). |
| **Xem theo phòng** | `GET` | `/thietbi/phong/{idPhong}` | Hiển thị danh sách thiết bị của một phòng học cụ thể. |

### 2. Validation và Ràng buộc

-   **Tên thiết bị:** Bắt buộc, từ 3 đến 100 ký tự.
-   **Mã thiết bị:** Bắt buộc, duy nhất, chỉ chứa chữ cái, số, và dấu gạch ngang (`[A-Za-z0-9-]+`).
-   **Số lượng:** Bắt buộc, phải là số nguyên lớn hơn 0.
-   **Tình trạng:** Bắt buộc, phải là một trong các giá trị: "Tốt", "Cần bảo trì", "Hỏng".
-   **Ngày lắp đặt:**
    -   Bắt buộc khi thiết bị được gán cho một phòng học (khác kho).
    -   Không được là một ngày trong tương lai.
    -   Để trống (NULL) nếu thiết bị nằm trong kho (`K00`).

### 3. Quản lý Phòng học

-   Dữ liệu phòng học (bao gồm cả loại phòng) được tải và hiển thị trong các menu dropdown để người dùng dễ dàng lựa chọn.
-   Hệ thống định nghĩa một phòng đặc biệt là **"Kho" (mã `K00`)** để chứa các thiết bị chưa được lắp đặt.