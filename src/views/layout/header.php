<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><?= htmlspecialchars($page_title ?? 'Quản lý Thiết bị') ?></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <link href="/css/styles.css" rel="stylesheet">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.php?action=list">QL Thiết Bị</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <span class="navbar-text text-light me-3">
                        Chào, <?= htmlspecialchars($_SESSION['user_ho_ten'] ?? 'Guest') ?>
                        (<?= htmlspecialchars($_SESSION['user_vai_tro'] ?? 'Guest') ?>)
                    </span>
                </li>
                <li class="nav-item">
                    <a class="btn btn-outline-light" href="index.php?action=logout">Đăng xuất</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="container my-5">