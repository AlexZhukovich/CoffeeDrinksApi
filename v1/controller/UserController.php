<?php
    require_once('../connector/DatabaseConnector.php');
    require_once('../model/Response.php');

    try {
        $db = DatabaseConnector::connectDatabase();
    } catch (PDOException $ex) {
        error_log("Connection error: ".$ex, 0);
        $response = new Response();
        $response->setHttpStatusCode(500);
        $response->setSuccess(false);
        $response->addMessage("Database connection error");
        $response->send();
        exit;
    }

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        $response = new Response();
        $response->setHttpStatusCode(405);
        $response->setSuccess(false);
        $response->addMessage("Request method not allowed");
        $response->send();
        exit;
    }

    if ($_SERVER['CONTENT_TYPE'] !== 'application/json') {
        $response = new Response();
        $response->setHttpStatusCode(400);
        $response->setSuccess(false);
        $response->addMessage("Content type header not set to JSON");
        $response->send();
        exit;
    }

    $rawPostData = file_get_contents('php://input');
    if (!$jsonData = json_decode($rawPostData)) {
        $response = new Response();
        $response->setHttpStatusCode(400);
        $response->setSuccess(false);
        $response->addMessage("Request body is not valid JSON");
        $response->send();
        exit;
    }
    
    if (!isset($jsonData->fullname) || !isset($jsonData->email) || !isset($jsonData->password)) {
        $response = new Response();
        $response->setHttpStatusCode(400);
        $response->setSuccess(false);
        (!isset($jsonData->fullname) ? $response->addMessage("Full name not supplied") : false);
        (!isset($jsonData->email) ? $response->addMessage("Email not supplied") : false);
        (!isset($jsonData->password) ? $response->addMessage("Password name not supplied") : false);
        $response->send();
        exit;
    }

    if (strlen($jsonData->fullname) < 1 || strlen($jsonData->fullname) > 255 || strlen($jsonData->email) < 1 || strlen($jsonData->email) > 255 || strlen($jsonData->password) < 1 || strlen($jsonData->password) > 255) {
        $response = new Response();
        $response->setHttpStatusCode(400);
        $response->setSuccess(false);
        (strlen($jsonData->fullname) < 1 ? $response->addMessage("Full name cannot be blank") : false);
        (strlen($jsonData->fullname) > 255 ? $response->addMessage("Full name cannot be greater than 255 characters") : false);
        (strlen($jsonData->email) < 1 ? $response->addMessage("Email cannot be blank") : false);
        (strlen($jsonData->email) > 255 ? $response->addMessage("Email cannot be greater than 255 characters") : false);
        (strlen($jsonData->password) < 1 ? $response->addMessage("Password cannot be blank") : false);
        (strlen($jsonData->password) > 255 ? $response->addMessage("Password cannot be greater than 255 characters") : false);
        $response->send();
        exit;
    }

    if (!filter_var($jsonData->email, FILTER_VALIDATE_EMAIL)) {
        $response = new Response();
        $response->setHttpStatusCode(400);
        $response->setSuccess(false);
        $response->addMessage("Invalid email address");
        $response->send();
        exit;
    }

    // Remove whitespace from full name and user name
    $fullname = trim($jsonData->fullname);
    $email = trim($jsonData->email);
    $password = $jsonData->password;

    try {
        
        // check if email already exist
        $query = $db->prepare('SELECT id 
                               FROM '.DatabaseConfig::USERS_TABLE.' 
                               WHERE email = :email');
        $query->bindParam(':email', $email, PDO::PARAM_STR);
        $query->execute();

        $rowCount = $query->rowCount();
        if ($rowCount !== 0) {
            $response = new Response();
            $response->setHttpStatusCode(409);
            $response->setSuccess(false);
            $response->addMessage("Email already exist");
            $response->send();
            exit;
        }

        // create a new user account
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $query = $db->prepare('INSERT INTO '.DatabaseConfig::USERS_TABLE.' (fullname, email, password) 
                               VALUES (:fullname, :email, :password)');
        $query->bindParam(':fullname', $fullname, PDO::PARAM_STR);
        $query->bindParam(':email', $email, PDO::PARAM_STR);
        $query->bindParam(':password', $hashedPassword, PDO::PARAM_STR);
        $query->execute();

        $rowCount = $query->rowCount();
        if ($rowCount === 0) {
            $response = new Response();
            $response->setHttpStatusCode(500);
            $response->setSuccess(false);
            $response->addMessage("There was an issue creating a user account");
            $response->send();
            exit;
        }

        $lastUserId = $db->lastInsertId();
        $returnData = array();
        $returnData['user_id'] = $lastUserId;
        $returnData['fullname'] = $fullname;
        $returnData['email'] = $email;

        $response = new Response();
        $response->setHttpStatusCode(201);
        $response->setSuccess(true);
        $response->addMessage("User created");
        $response->setData($returnData);
        $response->send();
        exit;
    } catch (PDOException $ex) {
        error_log("Database query error: ".$ex, 0);
        $response = new Response();
        $response->setHttpStatusCode(500);
        $response->setSuccess(false);
        $response->addMessage("There was an issue creating a user account - please try again");
        $response->send();
        exit;
    }
?>