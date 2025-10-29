<?php
/**
 * Creates and returns a PDO database connection.
 * @return PDO
 */
function get_pdo_connection() {
    // Get database credentials from environment variables
    $host = getenv('DB_HOST');
    $port = getenv('DB_PORT');
    $db   = getenv('DB_NAME');
    $user = getenv('DB_USER');
    $pass = getenv('DB_PASSWORD');
    $charset = 'utf8mb4';

    // Create the "DSN" (Data Source Name) string
    $dsn = "mysql:host=$host;port=$port;dbname=$db;charset=$charset";

    $options = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    try {
         return new PDO($dsn, $user, $pass, $options);
    } catch (\PDOException $e) {
         // On a real site, you'd log this error and show a user-friendly page
         throw new \PDOException($e->getMessage(), (int)$e->getCode());
    }
}
?>