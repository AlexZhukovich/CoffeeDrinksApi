<?php
    require_once('../connector/DatabaseConnector.php');
    require_once('../config/ServerConfig.php');
    require_once('../model/Response.php');
    require_once('../model/Coffee.php');

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

    // begin auth script
    if (!isset($_SERVER['HTTP_AUTHORIZATION']) || strlen($_SERVER['HTTP_AUTHORIZATION']) < 1) {
        $response = new Response();
        $response->setHttpStatusCode(401);
        $response->setSuccess(false);
        (!isset($_SERVER['HTTP_AUTHORIZATION']) ? $response->addMessage("Access token is missing from the header") : false);
        (strlen($_SERVER['HTTP_AUTHORIZATION']) < 1 ? $response->addMessage("Access token cannot be blank") : false);
        $response->send();
        exit;
    }

    $accesstoken = $_SERVER['HTTP_AUTHORIZATION'];


    try {
        $query = $db->prepare('SELECT userid, accesstokenexpiry, useractive, loginattempts FROM sessions, users WHERE sessions.userid = users.id AND accesstoken = :accesstoken');
        $query->bindParam(':accesstoken', $accesstoken, PDO::PARAM_STR);
        $query->execute();

        $rowCount = $query->rowCount();
        if ($rowCount === 0) {
            $response = new Response();
            $response->setHttpStatusCode(401);
            $response->setSuccess(false);
            $response->addMessage("Invalid access token");
            $response->send();
            exit;
        }

        $row = $query->fetch(PDO::FETCH_ASSOC);
        $returned_userid = $row['userid'];
        $returned_accesstokenexpiry = $row['accesstokenexpiry'];
        $returned_useractive = $row['useractive'];
        $returned_loginattempts = $row['loginattempts'];

        if ($returned_useractive !== 'Y') {
            $response = new Response();
            $response->setHttpStatusCode(401);
            $response->setSuccess(false);
            $response->addMessage("User account not active");
            $response->send();
            exit;
        }

        if ($returned_loginattempts >= 3) {
            $response = new Response();
            $response->setHttpStatusCode(401);
            $response->setSuccess(false);
            $response->addMessage("User account is currently locked out");
            $response->send();
            exit;
        }

        if (strtotime($returned_accesstokenexpiry) < time()) {
            $response = new Response();
            $response->setHttpStatusCode(401);
            $response->setSuccess(false);
            $response->addMessage("Access token expired");
            $response->send();
            exit;
        }
    } catch (PDOException $ex) {
        $response = new Response();
        $response->setHttpStatusCode(500);
        $response->setSuccess(false);
        $response->addMessage("There was an issue authenticating - please try again");
        $response->send();
        exit;
    }
    // end auth script


    if (array_key_exists("coffeeid", $_GET)) {
        $coffeeid = $_GET['coffeeid'];

        if ($coffeeid == '' || !is_numeric($coffeeid)) {
            $response = new Response();
            $response->setHttpStatusCode(400);
            $response->setSuccess(false);
            $response->addMessage("Coffee ID cannot be blank or must be numeric");
            $response->send();
            exit;
        }

        if ($_SERVER['REQUEST_METHOD'] === 'GET') {
            try {
                $query = $db->prepare('SELECT c.id, c.title, c.photourl, c.description, c.ingredients, IFNULL(f.favourite, "N") AS favourite 
                FROM coffees c 
                LEFT JOIN favourite_coffee f ON c.id = f.coffeeid AND f.userid = :userId 
                WHERE c.id = :coffeeid');
                $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                $query->bindParam(':userId', $returned_userid, PDO::PARAM_INT);
                $query->execute();

                $rowCount = $query->rowCount();
                if ($rowCount === 0) {
                    $response = new Response();
                    $response->setHttpStatusCode(404);
                    $response->setSuccess(false);
                    $response->addMessage("Coffee not found");
                    $response->send();
                    exit;
                }

                while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
                    $coffee = new Coffee($row['id'], $row['title'], $row['photourl'], $row['description'], $row['ingredients'], $row['favourite']);
                    $coffeeArray[] = $coffee->getCoffeeArray();
                }

                $returnData = array();
                $returnData['rows_returned'] = $rowCount;
                $returnData['coffee-drinks'] = $coffeeArray;

                $response = new Response();
                $response->setHttpStatusCode(200);
                $response->setSuccess(true);
                $response->toCache(true);
                $response->setData($returnData);
                $response->send();
                exit;
            } catch (CoffeeException $ex) {
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage($ex->getMessage());
                $response->send();
                exit;
            } catch (PDOException $ex) {
                error_log("Database query error: ".$ex, 0);
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage("Failed to get Coffee");
                $response->send();
                exit;
            }
        } elseif ($_SERVER['REQUEST_METHOD'] === 'PATCH') {
            try {
                if ($_SERVER['CONTENT_TYPE'] !== 'application/json') {
                    $response = new Response();
                    $response->setHttpStatusCode(400);
                    $response->setSuccess(false);
                    $response->addMessage("Content type header not set to JSON");
                    $response->send(); 
                    exit;
                }

                $rawPatchData = file_get_contents('php://input');
                if (!$jsonData = json_decode($rawPatchData)) {
                    $response = new Response();
                    $response->setHttpStatusCode(400);
                    $response->setSuccess(false);
                    $response->addMessage("Request body is not valid JSON");
                    $response->send(); 
                    exit;
                }

                $isFavourite_updated = false;

                if (isset($jsonData->favourite)) {
                    $isFavourite_updated = true;
                }

                if ($isFavourite_updated === false) {
                    $response = new Response();
                    $response->setHttpStatusCode(400);
                    $response->setSuccess(false);
                    $response->addMessage("No coffee favourite field provided");
                    $response->send(); 
                    exit;
                }

                $query = $db->prepare('SELECT c.id, c.title, c.photourl, c.description, c.ingredients, IFNULL(f.favourite, "N") AS favourite 
                                       FROM coffees c 
                                       LEFT JOIN favourite_coffee f ON c.id = f.coffeeid AND f.userid = :userId 
                                       WHERE c.id = :coffeeid');
                $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                $query->bindParam(':userId', $returned_userid, PDO::PARAM_INT);
                $query->execute();


                $rowCount = $query->rowCount();
                if ($rowCount === 0) {
                    $response = new Response();
                    $response->setHttpStatusCode(404);
                    $response->setSuccess(false);
                    $response->addMessage("No coffee found to update");
                    $response->send(); 
                    exit;
                }

                while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
                    $coffee = new Coffee(
                        $row['id'], 
                        $row['title'], 
                        $row['photourl'], 
                        $row['description'], 
                        $row['ingredients'], 
                        $row['favourite']);
                }

                $coffee->setFavourite($jsonData->favourite);

                $query = $db->prepare('SELECT id, userid, coffeeid, favourite FROM favourite_coffee WHERE coffeeid = :coffeeid AND userid = :userid');
                $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                $query->execute();

                if ($coffee->isFavourite() === 'Y') {
                    $rowCount = $query->rowCount();
                    $favourite = $coffee->isFavourite();
                    
                    if ($rowCount === 0) { // insert 
                        $query = $db->prepare('INSERT INTO favourite_coffee (userid, coffeeid, favourite) VALUES(:userid, :coffeeid, :favourite)');
                        $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                        $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                        $query->bindParam(':favourite', $favourite, PDO::PARAM_STR);
                        $query->execute(); 
                    } else { // update
                        $query = $db->prepare('UPDATE favourite_coffee SET favourite = :favourite WHERE coffeeid = :coffeeid AND userid = :userid');
                        $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                        $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                        $query->bindParam(':favourite', $favourite, PDO::PARAM_STR);
                        $query->execute();
                    }   
                } else {
                    $query = $db->prepare('DELETE FROM favourite_coffee WHERE coffeeid = :coffeeid AND userid = :userid');
                    $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                    $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                    $query->execute();
                }

                $query = $db->prepare('SELECT c.id, c.title, c.photourl, c.description, c.ingredients, IFNULL(f.favourite, "N") AS favourite 
                                       FROM coffees c 
                                       LEFT JOIN favourite_coffee f ON c.id = f.coffeeid AND f.userid = :userId 
                                       WHERE c.id = :coffeeid');
                $query->bindParam(':coffeeid', $coffeeid, PDO::PARAM_INT);
                $query->bindParam(':userId', $returned_userid, PDO::PARAM_INT);
                $query->execute();

                $rowCount = $query->rowCount();
                if ($rowCount === 0) {
                    $response = new Response();
                    $response->setHttpStatusCode(404);
                    $response->setSuccess(false);
                    $response->addMessage("No coffee found");
                    $response->send();
                    exit;
                }

                $coffeeArray = array();
                while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
                    $coffee = new Coffee($row['id'], $row['title'], $row['photourl'], $row['description'], $row['ingredients'], $row['favourite']);
                    $coffeeArray[] = $coffee->getCoffeeArray();
                }


                $returnData = array();
                $returnData['rows_returned'] = $rowCount;
                $returnData['coffee-drinks'] = $coffeeArray;

                $response = new Response();
                $response->setHttpStatusCode(200);
                $response->setSuccess(true);
                $response->addMessage("Coffee favourite status updated");
                $response->setData($returnData);
                $response->send();
                exit;
            } catch (CoffeeException $ex) {
                $response = new Response();
                $response->setHttpStatusCode(400);
                $response->setSuccess(false);
                $response->addMessage($ex->getMessage());
                $response->send(); 
                exit;
            } catch (PDOException $ex) {
                error_log("Database query error: ".$ex, 0);
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage("Failed to updated favourite for coffee");
                $response->send(); 
                exit;
            }
        } else {
            $response = new Response();
            $response->setHttpStatusCode(405);
            $response->setSuccess(false);
            $response->addMessage("Request method not allows");
            $response->send();
            exit;
        }
    /* /page */    
    } elseif (array_key_exists("page", $_GET)) {
        if ($_SERVER['REQUEST_METHOD'] === 'GET') {
            $page = $_GET['page'];
            if ($page == '' || !is_numeric($page)) {
                $response = new Response();
                $response->setHttpStatusCode(400);
                $response->setSuccess(false);
                $response->addMessage("Page number cannot be blank and must be numeric");
                $response->send();
                exit;
            }
    
            $limitPerPage = ServerConfig::ITEMS_ON_PAGE;

            try {
                $query = $db->prepare('SELECT count(id) as totalNoOfCoffeeDrinks FROM coffees');
                $query->execute();

                $row = $query->fetch(PDO::FETCH_ASSOC);
                $coffeeDrinksCount = intval($row['totalNoOfCoffeeDrinks']);
                $numOfPages = ceil($coffeeDrinksCount / $limitPerPage);
                if ($numOfPages == 0) {
                    $numOfPages = 1;
                }
                if ($page > $numOfPages || $page == 0) {
                    $response = new Response();
                    $response->setHttpStatusCode(404);
                    $response->setSuccess(false);
                    $response->addMessage("Page not found");
                    $response->send(); 
                    exit;
                }
    
                $offset = ($page == 1 ? 0 : ($limitPerPage * ($page - 1)));
                $query = $db->prepare('SELECT c.id, c.title, c.photourl, c.description, c.ingredients, IFNULL(f.favourite, "N") AS favourite 
                FROM coffees c 
                LEFT JOIN favourite_coffee f ON c.id = f.coffeeid AND f.userid = :userid
                LIMIT :pglimit OFFSET :offset');

                $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                $query->bindParam(':pglimit', $limitPerPage, PDO::PARAM_INT);
                $query->bindParam(':offset', $offset, PDO::PARAM_INT);
                $query->execute();
                
                $rowCount = $query->rowCount();
                $coffeeArray = array();
                while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
                    $coffee = new Coffee($row['id'], $row['title'], $row['photourl'], $row['description'], $row['ingredients'], $row['favourite']);
                    $coffeeArray[] = $coffee->getCoffeeArray();
                }

                $returnData = array();
                $returnData['row_returned'] = $rowCount;
                $returnData['total_rows'] = $coffeeDrinksCount;
                $returnData['total_pages'] = $numOfPages;
                $returnData['has_next_page'] = $page < $numOfPages ? true : false;
                $returnData['has_previous_page'] = $page > 1 ? true : false;
                $returnData['coffee-drinks'] = $coffeeArray;
                $response = new Response();
                $response->setHttpStatusCode(200);
                $response->setSuccess(true);
                $response->toCache(true);
                $response->setData($returnData);
                $response->send();
                exit;
            } catch (CoffeeException $ex) {
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage($ex->getMessage());
                $response->send(); 
                exit;
            } catch (PDOException $ex) {
                error_log("Database query error: ".$ex, 0);
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage("Failed to get coffee drinks");
                $response->send(); 
                exit;
            }
        } else {
            $response = new Response();
            $response->setHttpStatusCode(405);
            $response->setSuccess(false);
            $response->addMessage("Request method not allows");
            $response->send();
            exit;
        }
    /* /coffee drinks */    
    } elseif (empty($_GET)) {
        if ($_SERVER['REQUEST_METHOD'] === 'GET') {
            try {
                $query = $db->prepare('SELECT c.id, c.title, c.photourl, c.description, c.ingredients, IFNULL(f.favourite, "N") AS favourite 
                                       FROM coffees c 
                                       LEFT JOIN favourite_coffee f ON c.id = f.coffeeid AND f.userid = :userid');
                $query->bindParam(':userid', $returned_userid, PDO::PARAM_INT);
                $query->execute();

                $rowCount = $query->rowCount();
                $coffeeArray = array();
                while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
                    $coffee = new Coffee($row['id'], $row['title'], $row['photourl'], $row['description'], $row['ingredients'], $row['favourite']);
                    $coffeeArray[] = $coffee->getCoffeeArray();
                }

                $returnData = array();
                $returnData['row_returned'] = $rowCount;
                $returnData['coffee-drinks'] = $coffeeArray;

                $response = new Response();
                $response->setHttpStatusCode(200);
                $response->setSuccess(true);
                $response->toCache(true);
                $response->setData($returnData);
                $response->send();
                exit;
            } catch (CoffeeException $ex) {
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage($ex->getMessage());
                $response->send(); 
                exit;
            } catch (PDOException $ex) {
                error_log("Database query error: ".$ex, 0);
                $response = new Response();
                $response->setHttpStatusCode(500);
                $response->setSuccess(false);
                $response->addMessage("Failed to get coffee drinks");
                $response->send(); 
                exit;
            }
        } else {
            $response = new Response();
            $response->setHttpStatusCode(405);
            $response->setSuccess(false);
            $response->addMessage("Request method not allows");
            $response->send();
            exit;
        }
    } else {
        $response = new Response();
        $response->setHttpStatusCode(404);
        $response->setSuccess(false);
        $response->addMessage("Endpoint not found");
        $response->send();
        exit;
    }
?>