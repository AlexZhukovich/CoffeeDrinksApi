# Coffee List RESTful API

A RESTful API with token-based authentication for Coffee List application.

## Technologies and tools

* PHP
* MySQL

## Set Up 

The `.htaccess` file contains rooting and basic configuration for Apache server.

## Use cases

Basic use case of using API for **creating an account**, **log in** and **log out**.

1. [Create a new account](#create_user_account)
2. [Log In](#create_user_session)
3. [Log Out](#delete_existing_session)


## Routes

* **/v1/users/**
  * **<code>POST</code>** Create a new user account ([see requirements](#create_user_account)).
* **/v1/sessions/** 
  * **<code>POST</code>** Log in (Create a new user session) ([see requirements](#create_user_session)).
* **/v1/sessions/SESSION_ID**
  * **<code>PATCH</code>** Refresh access and refresh tokens, based on `SESSION_ID`. ([see requirements](#refresh-tokens)).
  * **<code>DELETE</code>** Log out (Delete existing session) ([see requirements](#delete_existing_session)).

## Request requirements

Description for basic requirements for each request.

### <a name="create_user_account"></a>Create a new user account 

**Request:**

| Label  | Value                           |
| ------ |-------------------------------- |
| Method | POST                            |
| URL    | /coffees-rest-api/v1/users        |
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
| Method | POST                            |
| URL    | /coffees-rest-api/v1/sessions     |
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

| Label  | Value                                      |
| ------ |------------------------------------------- |
| Method | PATCH                                      |
| URL    | /coffees-rest-api/v1/sessions/`SESSION_ID`   |
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

| Label  | Value                                      |
| ------ |------------------------------------------- |
| Method | DELETE                                     |
| URL    | /coffees-rest-api/v1/sessions/`SESSION_ID`   |
| Header | Authorization : `ACCESS_TOKEN`             |

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