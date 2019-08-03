# Coffee Drinks RESTful API

A RESTful API with token-based authentication for Coffee Drinks application.

## Technologies and tools

* PHP
* MySQL

## Set Up 

The `.htaccess` file contains rooting and basic configuration for Apache server.

**Note:** *The `php_flag display_errors` on should be removed from the `.htaccess` file before installing to server.*

## Use cases

Basic use case of using API for **creating an account**, **log in**, **get coffee drinks**, **creating a personalized coffee drink list**, **log out** and much more.

1. [Create a new account](#create_user_account)
2. [Log In](#create_user_session)
3. [Get all coffee drinks](#get_all_coffee_drinks)
4. [Mark coffee as favourite](#mark_coffee_as_favourite)
5. [Log Out](#delete_existing_session)

Additional features:
1. [Refresh access and refresh tokens](#refresh-tokens)
2. [Get coffee drinks by page number](#get_coffee_drinks_by_page_number)
3. [Get coffee drink by ID](#get_coffee_drink_by_id)

## Routes

* **/v1/users**
  * **<code>POST</code>** Create a new user account ([see requirements](#create_user_account)).
* **/v1/sessions** 
  * **<code>POST</code>** Log in (Create a new user session) ([see requirements](#create_user_session)).
* **/v1/sessions/`SESSION_ID`**
  * **<code>PATCH</code>** Refresh access and refresh tokens, based on `SESSION_ID`. ([see requirements](#refresh-tokens)).
  * **<code>DELETE</code>** Log out (Delete existing session) ([see requirements](#delete_existing_session)).
* **v1/coffee-drinks**
  * **<code>GET</code>** Get all coffee drinks ([see requirements](#get_all_coffee_drinks)).
  * **<code>PATCH</code>** Mark coffee drink as favourite ([see requirements](#mark_coffee_as_favourite)).
* **v1/coffee-drinks/`COFFEE_DRINK_ID`**
  * **<code>GET</code>** Get coffee drink by `COFFEE_DRINK_ID` ([see requirements](#get_coffee_drinks_by_page_number)).
* **v1/coffee-drinks/page/`PAGE_NUMBER`**
  * **<code>GET</code>** Get coffee drinks by `PAGE_NUMBER` ([see requirements](#get_coffee_drink_by_id)).


## Request requirements

Description for basic requirements for each request.

### <a name="create_user_account"></a>Create a new user account 

**Request:**

| Label  | Value                           |
| ------ |-------------------------------- |
| Method | **<code>POST</code>**           |
| URL    | /coffee-drinks-api/v1/users     |
| Header | Content-Type : application/json |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"fullname":`"FULL_NAME"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"username":`"USER_NAME"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"password":`"PASSWORD"`<br>}|

The request contains user data: `FULL_NAME`, `USER_NAME` and `PASSWORD`.

**Response**:
```
{
    "statusCode": 201,
    "success": true,
    "messages": [
        "User created"
    ],
    "data": {
        "user_id": "ID",
        "fullname": "FULL_NAME",
        "email": "EMAIL"
    }
}
```

### <a name="create_user_session"></a>Log in (Create a new user session)

| Label  | Value                           |
| ------ |-------------------------------- |
| Method | **<code>POST</code>**           |
| URL    | /coffee-drinks-api/v1/sessions  |
| Header | Content-Type : application/json |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"username":`"USER_NAME"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"password":`"PASSWORD"`<br>}|

The request contains user data: `USER_NAME` and `PASSWORD`.

**Response:**
```
{
    "statusCode": 201,
    "success": true,
    "messages": [],
    "data": {
        "session_id": SESSION_ID,
        "access_token": "ACCESS_TOKEN",
        "access_token_expires_in": ACCESS_TOKEN_EXPIRY,
        "refresh_token": "REFRESH_TOKEN",
        "refresh_token_expires_in": REFRESH_TOKEN_EXPIRY
    }
}
```


### <a name="refresh-tokens"></a>Refresh access and refresh tokens

| Label  | Value                                       |
| ------ |-------------------------------------------- |
| Method | **<code>PATCH</code>**                      |
| URL    | /coffee-drinks-api/v1/sessions/`SESSION_ID` |
| Header | Content-Type : application/json<br>Authorization : `ACCESS_TOKEN`  |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"refresh_token":`"REFRESH_TOKEN"`<br>}|

The request contains user data: `SESSION_ID`, `ACCESS_TOKEN` and `REFRESH_TOKEN`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [
        "Token refreshed"
    ],
    "data": {
        "session_id": SESSION_ID,
        "access_token": "ACCESS_TOKEN",
        "access_token_expires_in": ACCESS_TOKEN_EXPIRY,
        "refresh_token": "REFRESH_TOKEN",
        "refresh_token_expires_in": REFRESH_TOKEN_EXPIRY
    }
}
```

### <a name="delete_existing_session"></a>Log out (Delete existing session)

| Label  | Value                                       |
| ------ |-------------------------------------------- |
| Method | **<code>DELETE</code>**                     |
| URL    | /coffee-drinks-api/v1/sessions/`SESSION_ID` |
| Header | Authorization : `ACCESS_TOKEN`              |

The request contains user data: `SESSION_ID` and `ACCESS_TOKEN`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [
        "Logged out"
    ],
    "data": {
        "session_id": SESSION_ID
    }
}
```

### <a name="get_all_coffee_drinks"></a>Get all coffee drinks

| Label  | Value                               |
| ------ |------------------------------------ |
| Method | **<code>GET</code>**                |
| URL    | /coffee-drinks-api/v1/coffee-drinks |
| Header | Authorization : `ACCESS_TOKEN`      |

The request contains user data: `ACCESS_TOKEN`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [],
    "data": {
        "row_returned": COFFEE_COUNT,
        "coffee-drinks": [
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS" // 'Y' or 'N'
            },
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS" // 'Y' or 'N'
            }
        ]
    }
}
```

### <a name="mark_coffee_as_favourite"></a>Mark coffee drink as favourite

| Label  | Value                               |
| ------ |------------------------------------ |
| Method | **<code>PATCH</code>**              |
| URL    | /coffee-drinks-api/v1/coffee-drinks |
| Header | Content-Type : application/json<br>Authorization : `ACCESS_TOKEN` |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"favourite":`"N | Y"` &nbsp;&nbsp;&nbsp;&nbsp;// `Y` - favourite, `N` - not favourite<br>} |

The request contains user data: `ACCESS_TOKEN` and `FAVOURITE_VALUE (Y|N)`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [
        "Coffee favourite status updated"
    ],
    "data": {
        "rows_returned": 1,
        "coffee-drinks": [
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS"     // 'Y' or 'N'
            }
        ]
    }
}
```


### <a name="get_coffee_drinks_by_page_number"></a>Get coffee drinks by page number

| Label  | Value                                                  |
| ------ |------------------------------------------------------- |
| Method | **<code>GET</code>**                                   |
| URL    | /coffee-drinks-api/v1/coffee-drinks/page/`PAGE_NUMBER` |
| Header | Authorization : `ACCESS_TOKEN`                         |

The request contains user data: `ACCESS_TOKEN` and `PAGE_NUMBER`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [],
    "data": {
        "row_returned": NUMBER_OF_ROWS,
        "total_rows": TOTAL_NUMBER_OF_ROWS,
        "total_pages": NUMBER_OF_TOTAL_PAGES,
        "has_next_page": true|false,
        "has_previous_page": true|false,
        "coffee-drinks": [
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS" // 'Y' or 'N'
            },
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS" // 'Y' or 'N'
            }
        ]
    }
}
```

### <a name="get_coffee_drink_by_id"></a>Get coffee by ID

| Label  | Value                                                 |
| ------ |------------------------------------------------------ |
| Method | **<code>GET</code>**                                  |
| URL    | /coffee-drinks-api/v1/coffee-drinks/`COFFEE_DRINK_ID` |
| Header | Authorization : `ACCESS_TOKEN`                        |

The request contains user data: `ACCESS_TOKEN` and `COFFEE_DRINK_ID`.

**Response:**
```
{
    "statusCode": 200,
    "success": true,
    "messages": [],
    "data": {
        "rows_returned": 1,
        "coffee-drinks": [
            {
                "id": COFFEE_ID,
                "title": "COFFEE_TITLE",
                "photo_url": "COFFEE_PHOTO_URL",
                "description": "COFFEE_DESCRIPTION",
                "ingredients": "COFFEE_INGREDIENTS",
                "favourite": "COFFEE_FAVOURITE_STATUS" // 'Y' or 'N'
            }
        ]
    }
}
```
