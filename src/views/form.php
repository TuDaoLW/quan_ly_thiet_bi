<?php
// Determine if we are editing or adding
$is_edit = isset($thietbi) && !empty($thietbi['id_thiet_bi']);
$page_title = $is_edit ? "Sửa Thiết Bị" : "Thêm Thiết Bị Mới";

// Set default values for add form
if (!$is_edit) {
    $thietbi = [
        'id_thiet_bi' => null,
        'ten_thiet_bi' => '',
        'ma_thiet_bi' => '',
        'id_phong' => null,
        'so_luong' => 1,
        'tinh_trang' => 'Tốt',
        'ngay_lap_dat' => date('Y-m-d'), // Default to today
        'mo_ta' => ''
    ];
}
?>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><?= $page_title ?></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="/css/styles.css" rel="stylesheet">
</head>
<body>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h2 class="mb-4"><?= $page_title ?></h2>

            <?php if (!empty($errors)): ?>
                <div class="alert alert-danger" role="alert">
                    <h4 class="alert-heading">Lỗi!</h4>
                    <p>Vui lòng sửa các lỗi sau:</p>
                    <hr>
                    <ul class="mb-0">
                        <?php foreach ($errors as $error): ?>
                            <li><?= htmlspecialchars($error) ?></li>
                        <?php endforeach; ?>
                    </ul>
                </div>
            <?php endif; ?>

            <form action="index.php?action=save" method="post" class="needs-validation" novalidate>
                <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
                <input type="hidden" name="id_thiet_bi" value="<?= $thietbi['id_thiet_bi'] ?>">

                <div class="mb-3">
                    <label for="tenThietBi" class="form-label">Tên thiết bị <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="tenThietBi" name="ten_thiet_bi"
                           value="<?= htmlspecialchars($thietbi['ten_thiet_bi']) ?>" required minlength="3" maxlength="100">
                    <div class="invalid-feedback">Tên thiết bị phải từ 3 đến 100 ký tự.</div>
                </div>

                <div class="mb-3">
                    <label for="maThietBi" class="form-label">Mã thiết bị <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="maThietBi" name="ma_thiet_bi"
                           value="<?= htmlspecialchars($thietbi['ma_thiet_bi']) ?>" required pattern="^[A-Za-z0-9-]+$">
                    <div class="invalid-feedback">Mã thiết bị chỉ được chứa chữ cái, số, và dấu gạch ngang.</div>
                </div>

                <div class="mb-3">
                    <label for="phongHoc" class="form-label">Phòng học <span class="text-danger">*</span></label>
                    <select class="form-select" id="phongHoc" name="id_phong" required>
                        <option value="">-- Chọn phòng --</option>
                        <?php foreach ($phongs as $phong): ?>
                            <option value="<?= $phong['id_phong'] ?>"
                                    <?= ($phong['id_phong'] == $thietbi['id_phong']) ? 'selected' : '' ?>>
                                <?= htmlspecialchars($phong['ten_phong']) ?> (<?= htmlspecialchars($phong['ma_phong']) ?>)
                            </option>
                        <?php endforeach; ?>
                    </select>
                    <div class="invalid-feedback">Vui lòng chọn một phòng.</div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="soLuong" class="form-label">Số lượng <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" id="soLuong" name="so_luong"
                               value="<?= (int)$thietbi['so_luong'] ?>" required min="1">
                        <div class="invalid-feedback">Số lượng phải lớn hơn 0.</div>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="tinhTrang" class="form-label">Tình trạng <span class="text-danger">*</span></label>
                        <select class="form-select" id="tinhTrang" name="tinh_trang" required>
                            <option value="Tốt" <?= $thietbi['tinh_trang'] == 'Tốt' ? 'selected' : '' ?>>Tốt</option>
                            <option value="Cần bảo trì" <?= $thietbi['tinh_trang'] == 'Cần bảo trì' ? 'selected' : '' ?>>Cần bảo trì</option>
                            <option value="Hỏng" <?= $thietbi['tinh_trang'] == 'Hỏng' ? 'selected' : '' ?>>Hỏng</option>
                        </select>
                        <div class="invalid-feedback">Vui lòng chọn tình trạng.</div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="ngayLapDat" class="form-label">Ngày lắp đặt</label>
                    <input type="date" class="form-control" id="ngayLapDat" name="ngay_lap_dat"
                           value="<?= htmlspecialchars($thietbi['ngay_lap_dat']) ?>">
                    <div class="form-text">Để trống nếu thiết bị ở trong kho (K00).</div>
                </div>

                <div class="mb-3">
                    <label for="moTa" class="form-label">Mô tả</label>
                    <textarea class="form-control" id="moTa" name="mo_ta" rows="3" maxlength="500"><?= htmlspecialchars($thietbi['mo_ta']) ?></textarea>
                    <div class="invalid-feedback">Mô tả không được vượt quá 500 ký tự.</div>
                </div>

                <div class="d-flex justify-content-end">
                    <a href="index.php?action=list" class="btn btn-secondary me-2">Hủy</a>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>

</body>
</html>