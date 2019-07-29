<?php
    require_once('../config/DatabaseConfig.php');

    class DatabaseConnector {
        private static $databaseConnection;

        public static function connectDatabase() {
            if (self::$databaseConnection === null) {
                self::$databaseConnection = new PDO(
                    'mysql:host='.DatabaseConfig::DB_HOST.';dbname='.DatabaseConfig::DB_NAME.';'.DatabaseConfig::DB_CHARSET, 
                    DatabaseConfig::DB_USERNAME,
                    DatabaseConfig::DB_PASSWORD
                );
                self::$databaseConnection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                self::$databaseConnection->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
            }
            return self::$databaseConnection;
        }
    }
?>