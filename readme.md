1. Quản lý thiết bịa. Danh sách thiết bịĐường dẫn: /thietbi (GET)
Chức năng:Hiển thị danh sách tất cả thiết bị trong hệ thống (index.html).
Hỗ trợ tìm kiếm thiết bị theo từ khóa (trường tenThietBi) thông qua tham số keyword.
Bảng hiển thị các thông tin:ID thiết bị
Tên thiết bị
Mã thiết bị
Phòng học (tên phòng)
Loại phòng (tên loại phòng từ bảng LOAI_PHONG)
Số lượng
Tình trạng (Tốt, Cần bảo trì, Hỏng)
Ngày lắp đặt (định dạng dd/MM/yyyy)
Mô tả

Các hành động:Sửa: Chuyển đến form chỉnh sửa thiết bị (/thietbi/edit/{id}).
Xóa: Xóa thiết bị với xác nhận JavaScript (/thietbi/delete/{id}).

b. Thêm thiết bịĐường dẫn: /thietbi/add (GET) và /thietbi/save (POST)
Chức năng:Mở form để thêm thiết bị mới (form.html).
Các trường nhập liệu:Tên thiết bị: Bắt buộc, từ 3 đến 100 ký tự.
Mã thiết bị: Bắt buộc, duy nhất, chỉ chứa chữ cái, số, hoặc dấu gạch ngang ([A-Za-z0-9-]+).
Phòng học: Dropdown, có thể không chọn (gán vào kho K00) hoặc chọn phòng từ danh sách.
Số lượng: Bắt buộc, phải là số nguyên dương (> 0).
Tình trạng: Dropdown với các giá trị Tốt, Cần bảo trì, Hỏng, bắt buộc.
Ngày lắp đặt: Bắt buộc nếu chọn phòng học (không phải kho), không được là ngày trong tương lai.
Mô tả: Không bắt buộc, tối đa 500 ký tự.

Logic đặc biệt:Nếu không chọn phòng hoặc chọn phòng kho (ma_phong = 'K00'), ngayLapDat được đặt là null.
Validation cả ở frontend (HTML5, JavaScript) và backend (jakarta.validation).

c. Sửa thiết bịĐường dẫn: /thietbi/edit/{id} (GET) và /thietbi/save (POST)
Chức năng:Tải thông tin thiết bị theo id và hiển thị trong form (form.html).
Cho phép chỉnh sửa tất cả các trường giống như khi thêm mới.
Validation tương tự như thêm thiết bị.
Kiểm tra tính duy nhất của maThietBi (trừ trường hợp giữ nguyên mã cũ).

d. Xóa thiết bịĐường dẫn: /thietbi/delete/{id} (GET)
Chức năng:Xóa thiết bị khỏi cơ sở dữ liệu dựa trên id.
Yêu cầu xác nhận qua JavaScript (confirm).
Tự động xóa bản ghi liên quan trong bảng thiet_bi nhờ ON DELETE CASCADE trong cơ sở dữ liệu.

e. Xem thiết bị theo phòngĐường dẫn: /thietbi/phong/{idPhong} (GET)
Chức năng:Hiển thị danh sách thiết bị trong một phòng cụ thể (phong_thietbi.html).
Hiển thị thông tin phòng (tên phòng, loại phòng).
Bảng hiển thị các trường tương tự index.html (trừ cột Phòng học và Loại phòng).
Hỗ trợ các hành động sửa/xóa như danh sách thiết bị.

2. ValidationBackend (BE):Sử dụng jakarta.validation.constraints để validate:tenThietBi: Không trống, 3-100 ký tự.
maThietBi: Không trống, định dạng [A-Za-z0-9-]+, duy nhất.
soLuong: Không null, > 0.
tinhTrang: Không null, chỉ được là Tốt, Cần bảo trì, Hỏng.
ngayLapDat: Không được là ngày trong tương lai (nếu có).
moTa: Tối đa 500 ký tự.

Kiểm tra tính duy nhất của maThietBi trong ThietBiService trước khi lưu.
Xử lý lỗi DataIntegrityViolationException để thông báo khi maThietBi trùng.

Frontend (FE):HTML5 validation: required, minlength, maxlength, pattern, min.
JavaScript:Kiểm tra định dạng maThietBi và soLuong.
Kiểm tra bắt buộc nhập ngayLapDat khi chọn phòng học (không phải kho).
Giới hạn ngayLapDat không vượt quá ngày hiện tại.
Dropdown tinh_trang giới hạn các giá trị hợp lệ.

Hiển thị thông báo lỗi từ backend (validation errors hoặc lỗi trùng mã).

3. Quản lý phòng họcDanh sách phòng học:Được tải và hiển thị trong dropdown của form.html và layout.html (cho tìm kiếm theo phòng).
Hiển thị tên phòng kèm loại phòng (ví dụ: "Phòng học A101 (Phòng học thường)").

Thông tin phòng:Bao gồm: ma_phong, ten_phong, suc_chua, vi_tri, loai_phong, trang_thai, mo_ta, ngay_tao.
Mối quan hệ với bảng LOAI_PHONG được ánh xạ qua LoaiPhong entity.

Phòng kho:Phòng có ma_phong = 'K00' được dùng để chứa thiết bị chưa gán vào phòng học.
Thiết bị trong kho có ngay_lap_dat = null.

4. Quản lý loại phòngEntity LoaiPhong:Ánh xạ bảng LOAI_PHONG với các trường id_loai_phong, ten_loai, mo_ta.
Validation: ten_loai không trống, tối đa 50 ký tự, duy nhất.

Chức năng:Hiển thị ten_loai trong dropdown phòng học và các bảng danh sách (index.html, phong_thietbi.html).
Không có giao diện quản lý trực tiếp (thêm/sửa/xóa loại phòng), nhưng có thể mở rộng.

5. Quản lý phòng thí nghiệm và phòng đào tạoPhòng thí nghiệm (PHONG_TB):Entity PhongTB ánh xạ bảng PHONG_TB với các trường:id_phong_tb
id_phong (liên kết với phong_hoc)
so_sinh_vien_toi_da
loai_thiet_bi

Validation: so_sinh_vien_toi_da >= 0.
Dữ liệu mẫu: Phòng B201 (Hóa học) và B202 (Vật lý).

Phòng đào tạo (PHONG_DT):Entity PhongDT ánh xạ bảng PHONG_DT với các trường:id_phong_dt
id_phong (liên kết với phong_hoc)
chuyen_nganh

Validation: chuyen_nganh tối đa 100 ký tự.
Dữ liệu mẫu: Phòng C301 (Công nghệ thông tin) và C302 (Kế toán - Tài chính).

Chức năng:Hiện tại, thông tin từ PHONG_TB và PHONG_DT chưa được hiển thị trên giao diện, nhưng có thể mở rộng để hiển thị trong phong_thietbi.html.

6. Các tính năng khácLogging:Sử dụng SLF4J để ghi log các hành động (thêm, sửa, xóa, tìm kiếm, truy vấn thiết bị).
Log thông báo số lượng thiết bị trả về và các lỗi validation.

Cơ sở dữ liệu:Sử dụng MySQL với các bảng:LOAI_PHONG
phong_hoc
PHONG_TB
PHONG_DT
thiet_bi

Ràng buộc:UNIQUE trên ma_thiet_bi, ma_phong, ten_loai.
FOREIGN KEY với ON DELETE CASCADE để đảm bảo tính toàn vẹn dữ liệu.

Dữ liệu mẫu đã được chèn để kiểm tra.

Giao diện:Sử dụng Thymeleaf với các template: index.html, form.html, phong_thietbi.html, layout.html.
Bootstrap CSS để định dạng giao diện.
JavaScript để xử lý validation và logic ẩn/hiện trường ngayLapDat.

