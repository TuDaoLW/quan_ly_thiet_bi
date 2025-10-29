<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách thiết bị</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    
    <link href="/css/styles.css" rel="stylesheet">
</head>
<body>
<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Danh Sách Thiết Bị</h2>
        <a href="index.php?action=add" class="btn btn-primary"><i class="bi bi-plus-circle me-2"></i>Thêm thiết bị</a>
    </div>

    <?php if (!empty($_GET['success'])): ?>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <?= htmlspecialchars($_GET['success']) ?>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <?php endif; ?>
    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <?= htmlspecialchars($_GET['error']) ?>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <?php endif; ?>

    <form action="index.php" method="get" class="mb-4 p-3 border rounded bg-light">
        <input type="hidden" name="action" value="list">
        <div class="row g-3 align-items-end">
            <div class="col-md-6">
                <label for="keyword" class="form-label">Tìm kiếm</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-search"></i></span>
                    <input type="text" name="keyword" id="keyword" class="form-control"
                           value="<?= htmlspecialchars($keyword) ?>" placeholder="Tìm kiếm thiết bị...">
                </div>
            </div>
            
            <div class="col-md-4">
                <label for="phongHoc" class="form-label">Lọc theo phòng</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="bi bi-building"></i></span>
                    <select id="phongHoc" name="id_phong" class="form-select">
                        <option value="">-- Tất cả phòng --</option>
                        
                        <?php foreach ($phongs as $phong): ?>
                            <option value="<?= $phong['id_phong'] ?>" 
                                    <?= ($phong['id_phong'] == $idPhong) ? 'selected' : '' ?>>
                                <?= htmlspecialchars($phong['ten_phong']) ?>
                            </option>
                        <?php endforeach; ?>
                        
                    </select>
                </div>
            </div>

            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Lọc / Tìm</button>
            </div>
        </div>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Tên thiết bị</th>
                    <th>Mã thiết bị</th>
                    <th>Phòng học</th>
                    <th>Loại phòng</th>
                    <th>Số lượng</th>
                    <th>Tình trạng</th>
                    <th>Ngày lắp đặt</th>
                    <th>Mô tả</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <?php if (empty($thietbis)): ?>
                    <tr>
                        <td colspan="10" class="text-center">Không có dữ liệu</td>
                    </tr>
                <?php else: ?>
                    <?php foreach ($thietbis as $tb): ?>
                        <tr>
                            <td><?= $tb['id_thiet_bi'] ?></td>
                            <td><?= htmlspecialchars($tb['ten_thiet_bi']) ?></td>
                            <td><?= htmlspecialchars($tb['ma_thiet_bi']) ?></td>
                            <td><?= htmlspecialchars($tb['ten_phong']) ?></td>
                            <td><?= htmlspecialchars($tb['ten_loai']) ?></td>
                            <td><?= $tb['so_luong'] ?></td>
                            <td>
                                <?php
                                $tinh_trang = htmlspecialchars($tb['tinh_trang']);
                                switch ($tinh_trang) {
                                    case 'Tốt':
                                        echo '<span class="badge bg-success">Tốt</span>';
                                        break;
                                    case 'Cần bảo trì':
                                        echo '<span class="badge bg-warning">Cần bảo trì</span>';
                                        break;
                                    case 'Hỏng':
                                        echo '<span class="badge bg-danger">Hỏng</span>';
                                        break;
                                    default:
                                        echo '<span class="badge bg-secondary">' . $tinh_trang . '</span>';
                                }
                                ?>
                            </td>
                            <td>
                                <?= $tb['ngay_lap_dat'] ? (new DateTime($tb['ngay_lap_dat']))->format('d/m/Y') : '' ?>
                            </td>
                            <td><?= htmlspecialchars($tb['mo_ta']) ?></td>
                            <td>
                                <a href="index.php?action=edit&id=<?= $tb['id_thiet_bi'] ?>"
                                   class="btn btn-sm btn-warning mb-1"><i class="bi bi-pencil me-1"></i>Sửa</a>
                                
                                <form action="index.php?action=delete&id=<?= $tb['id_thiet_bi'] ?>" method="post" style="display:inline;">
                                    <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
                                    
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Bạn có chắc muốn xóa?')"><i class="bi bi-trash me-1"></i>Xóa</button>
                                </form>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                <?php endif; ?>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>