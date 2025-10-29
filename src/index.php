<?php
// Start a session to handle CSRF tokens and flash messages
session_start();

// 1. Include our database connection function
require_once 'db.php';

// 2. Get the requested "action" from the URL
// We use 'list' as the default action if none is specified
$action = $_GET['action'] ?? 'list';

// 3. Get a database connection
try {
    $pdo = get_pdo_connection();
} catch (\PDOException $e) {
    echo "<h1>Database Connection Failed</h1>";
    echo "<p>Error: " . $e->getMessage() . "</p>";
    exit;
}

// 4. The main router logic
switch ($action) {
    case 'list':
        // --- This is the logic from your ThietBiController.list() method ---
        
        // Get filter parameters
        $keyword = $_GET['keyword'] ?? '';
        $idPhong = (int)($_GET['id_phong'] ?? 0);

        // --- Logic from getAllPhongs() ---
        $phongs_stmt = $pdo->query("SELECT id_phong, ten_phong FROM phong_hoc ORDER BY ten_phong");
        $phongs = $phongs_stmt->fetchAll();

        // --- Logic from getAll() and search() ---
        // Base query joins the tables we need for the view
        $sql = "SELECT tb.*, ph.ten_phong, lp.ten_loai
                FROM thiet_bi tb
                LEFT JOIN phong_hoc ph ON tb.id_phong = ph.id_phong
                LEFT JOIN loai_phong lp ON ph.id_loai_phong = lp.id_loai_phong";
        
        $conditions = [];
        $params = [];

        if (!empty($keyword)) {
            $conditions[] = "tb.ten_thiet_bi LIKE ?";
            $params[] = '%' . $keyword . '%';
        }
        
        if (!empty($idPhong)) {
            $conditions[] = "tb.id_phong = ?";
            $params[] = $idPhong;
        }

        if (count($conditions) > 0) {
            $sql .= " WHERE " . implode(" AND ", $conditions);
        }
        
        $sql .= " ORDER BY tb.id_thiet_bi DESC";

        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);
        $thietbis = $stmt->fetchAll();

        // --- Generate a CSRF token for delete forms ---
        // This replaces <input type="hidden" th:name="${_csrf.parameterName}" ...>
        if (empty($_SESSION['csrf_token'])) {
            $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
        }
        $csrf_token = $_SESSION['csrf_token'];

        // 5. Load the "view" file (our PHP template)
        require_once 'views/list.php';
        break;

    case 'add':
        // --- Logic from getAllPhongs() ---
        $phongs_stmt = $pdo->query("SELECT id_phong, ten_phong, ma_phong FROM phong_hoc ORDER BY ten_phong");
        $phongs = $phongs_stmt->fetchAll();

        // --- Generate a CSRF token for the form ---
        if (empty($_SESSION['csrf_token'])) {
            $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
        }
        $csrf_token = $_SESSION['csrf_token'];

        // Load the form view. 
        // $thietbi is not set, so the form will be empty
        require_once 'views/form.php';
        break;
        
    case 'save':
        // 1. We only accept POST requests for saving
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            header('Location: index.php?action=list&error=Invalid request method');
            exit;
        }

        // 2. CSRF Token Validation
        if (!isset($_POST['csrf_token']) || $_POST['csrf_token'] !== $_SESSION['csrf_token']) {
            header('Location: index.php?action=list&error=Invalid CSRF token');
            exit;
        }

        // 3. Get and Sanitize Data
        // This replaces Jsoup.clean()
        $id = (int)($_POST['id_thiet_bi'] ?? 0);
        $ten_thiet_bi = strip_tags($_POST['ten_thiet_bi'] ?? '');
        $ma_thiet_bi = strip_tags($_POST['ma_thiet_bi'] ?? '');
        $id_phong = (int)($_POST['id_phong'] ?? 0);
        $so_luong = (int)($_POST['so_luong'] ?? 0);
        $tinh_trang = strip_tags($_POST['tinh_trang'] ?? '');
        $ngay_lap_dat = $_POST['ngay_lap_dat'] ?? null;
        $mo_ta = strip_tags($_POST['mo_ta'] ?? '');

        // 4. Server-Side Validation (like @Valid)
        $errors = [];
        if (strlen($ten_thiet_bi) < 3 || strlen($ten_thiet_bi) > 100) {
            $errors[] = "Tên thiết bị phải từ 3 đến 100 ký tự";
        }
        if (empty($ma_thiet_bi) || !preg_match('/^[A-Za-z0-9-]+$/', $ma_thiet_bi)) {
            $errors[] = "Mã thiết bị không được để trống và chỉ chứa chữ cái, số, dấu gạch ngang";
        }
        if ($id_phong <= 0) {
            $errors[] = "Vui lòng chọn một phòng học";
        }
        if ($so_luong <= 0) {
            $errors[] = "Số lượng phải lớn hơn 0";
        }
        if (!in_array($tinh_trang, ['Tốt', 'Cần bảo trì', 'Hỏng'])) {
            $errors[] = "Tình trạng không hợp lệ";
        }
        
        // 5. Business Logic (from ThietBiService)
        
        // 5a. Check for unique ma_thiet_bi
        $stmt = $pdo->prepare("SELECT id_thiet_bi FROM thiet_bi WHERE ma_thiet_bi = ? AND id_thiet_bi != ?");
        $stmt->execute([$ma_thiet_bi, $id]);
        if ($stmt->fetch()) {
            $errors[] = "Mã thiết bị đã tồn tại!";
        }

        // 5b. Handle "Kho" (K00) logic
        $stmt = $pdo->prepare("SELECT ma_phong FROM phong_hoc WHERE id_phong = ?");
        $stmt->execute([$id_phong]);
        $phong = $stmt->fetch();
        
        if ($phong && $phong['ma_phong'] === 'K00') {
            $ngay_lap_dat = null; // Set ngay_lap_dat to NULL if it's in Kho
        } elseif (empty($ngay_lap_dat)) {
             $errors[] = "Ngày lắp đặt là bắt buộc (trừ khi thiết bị ở trong Kho)";
        } elseif (new DateTime($ngay_lap_dat) > new DateTime()) {
            $errors[] = "Ngày lắp đặt không được là một ngày trong tương lai";
        }
        
        // 6. If there are errors, re-render the form
        if (!empty($errors)) {
            // We need to pass all the data back to the form
            
            // Get all rooms for the dropdown
            $phongs_stmt = $pdo->query("SELECT id_phong, ten_phong, ma_phong FROM phong_hoc ORDER BY ten_phong");
            $phongs = $phongs_stmt->fetchAll();
            
            // Pass the user's submitted data back
            $thietbi = $_POST;
            
            // Get the CSRF token
            $csrf_token = $_SESSION['csrf_token'];
            
            // Load the form view again, which will display the $errors
            require_once 'views/form.php';
            break; // Stop execution
        }

        // 7. If validation passes, save to database
        try {
            if ($id > 0) {
                // UPDATE (for Edit)
                $sql = "UPDATE thiet_bi SET ten_thiet_bi = ?, ma_thiet_bi = ?, id_phong = ?, so_luong = ?, tinh_trang = ?, ngay_lap_dat = ?, mo_ta = ?
                        WHERE id_thiet_bi = ?";
                $params = [$ten_thiet_bi, $ma_thiet_bi, $id_phong, $so_luong, $tinh_trang, $ngay_lap_dat, $mo_ta, $id];
            } else {
                // INSERT (for Add)
                $sql = "INSERT INTO thiet_bi (ten_thiet_bi, ma_thiet_bi, id_phong, so_luong, tinh_trang, ngay_lap_dat, mo_ta)
                        VALUES (?, ?, ?, ?, ?, ?, ?)";
                $params = [$ten_thiet_bi, $ma_thiet_bi, $id_phong, $so_luong, $tinh_trang, $ngay_lap_dat, $mo_ta];
            }
            
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            // 8. Redirect to list with success message
            // This is the "redirect:/thietbi?success=..."
            unset($_SESSION['csrf_token']); // Regenerate token on next page load
            header('Location: index.php?action=list&success=Da luu thiet bi thanh cong');
            exit;

        } catch (\PDOException $e) {
            // Handle unexpected errors
            header('Location: index.php?action=list&error=Loi khi luu vao database: ' . $e->getMessage());
            exit;
        }
        break;

    case 'edit':
        // 1. Get the ID from the URL
        $id = (int)($_GET['id'] ?? 0);
        if ($id <= 0) {
            header('Location: index.php?action=list&error=ID thiet bi khong hop le');
            exit;
        }

        // 2. Fetch the specific device from the database
        $stmt = $pdo->prepare("SELECT * FROM thiet_bi WHERE id_thiet_bi = ?");
        $stmt->execute([$id]);
        $thietbi = $stmt->fetch();

        // 3. If not found, redirect back to the list
        if (!$thietbi) {
            header('Location: index.php?action=list&error=Thiet bi khong ton tai');
            exit;
        }

        // 4. Get all rooms for the dropdown
        $phongs_stmt = $pdo->query("SELECT id_phong, ten_phong, ma_phong FROM phong_hoc ORDER BY ten_phong");
        $phongs = $phongs_stmt->fetchAll();

        // 5. Generate a CSRF token for the form
        if (empty($_SESSION['csrf_token'])) {
            $_SESSION['csrf_token'] = bin2hex(random_bytes(32));
        }
        $csrf_token = $_SESSION['csrf_token'];

        // 6. Load the form view.
        // The form will be pre-filled because we are passing $thietbi
        require_once 'views/form.php';
        break;
    
    case 'delete':
        // 1. We only accept POST requests for deleting
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            header('Location: index.php?action=list&error=Invalid request method');
            exit;
        }

        // 2. CSRF Token Validation
        if (!isset($_POST['csrf_token']) || $_POST['csrf_token'] !== $_SESSION['csrf_token']) {
            header('Location: index.php?action=list&error=Invalid CSRF token');
            exit;
        }

        // 3. Get the ID from the URL
        $id = (int)($_GET['id'] ?? 0);
        if ($id <= 0) {
            header('Location: index.php?action=list&error=ID thiet bi khong hop le');
            exit;
        }

        // 4. Execute the delete query
        try {
            $stmt = $pdo->prepare("DELETE FROM thiet_bi WHERE id_thiet_bi = ?");
            $stmt->execute([$id]);

            // 5. Check if any row was actually deleted
            if ($stmt->rowCount() > 0) {
                $message = "Da xoa thiet bi thanh cong";
            } else {
                $message = "Thiet bi khong ton tai hoac da duoc xoa";
            }
            
            unset($_SESSION['csrf_token']); // Regenerate token
            header('Location: index.php?action=list&success=' . urlencode($message));
            exit;

        } catch (\PDOException $e) {
            // Handle database errors (e.g., foreign key constraints)
            header('Location: index.php?action=list&error=Loi khi xoa thiet bi: ' . $e->getMessage());
            exit;
        }
        break;

    default:
        // Handle unknown actions
        echo "<h1>404 - Page Not Found</h1>";
        echo "<p>Unknown action: " . htmlspecialchars($action) . "</p>";
        echo '<a href="index.php">Back to list</a>';
        break;
}
?>