# Coffee Drinks RESTful API

A RESTful API which provides information about coffee drinks. Users can store information about favorite coffee drinks. The current implementation uses a runtime database.

## Technologies and tools

- Kotlin
- Ktor
- H2 database
- JWT

## Set Up 

The following environment variables are mandatory:
-`SECRET_KEY`: used for generating the JWT token

To run the environment variables, you can use the application configuration in Intellij IDEA:
- Select "Edit Configurations..." from the "Edit" menu
- Add Environment Variables 
- Save changes

**Example of environment variables:**
```
SECRET_KEY=super-important-key
```

## Use cases

- [Create a new account](#create-a-new-account)
- [Log In](#log-in)
- [Get all coffee drinks](#get-all-coffee-drinks)
  - [Get all coffee drinks without favorite state](#get-all-coffee-drinks-without-favorite-state)
  - [Get all coffee drinks with favorite state (TOKEN is needed)](#get-all-coffee-drinks-with-favorite-state-token-is-needed)
- [Mark coffee as favorite](#mark-coffee-drink-as-favorite)
- [Get the coffee drink by ID](#get-the-coffee-drink-by-id)
  - [Get the coffee drink by ID without favorite state](#get-the-coffee-drink-by-id-without-favorite-state)
  - [Get the coffee drink by ID with favorite state (TOKEN is needed)](#get-the-coffee-drink-by-id-with-favorite-state-token-is-needed)
- [Get user information](#get-user-information)

## v1 API

### Create a new account

**Request**:

| Label  | Value                                                                                                                                                                           |
|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Method | **<code>POST</code>**                                                                                                                                                           |
| URL    | /api/v1/users/create                                                                                                                                                            |
| Header | Content-Type : application/json                                                                                                                                                 |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"username":`"example_user"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"email":`"user@example.com"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"password":`"password123"`<br>} |

**Response**: `JWT TOKEN`

### Log In

| Label  | Value                                                                                                                  |
|--------|------------------------------------------------------------------------------------------------------------------------|
| Method | **<code>POST</code>**                                                                                                  |
| URL    | /api/v1/users/login                                                                                                    |
| Header | Content-Type : application/json                                                                                        |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"email":`"user@example.com"`, <br>&nbsp;&nbsp;&nbsp;&nbsp;"password":`"password123"`<br>} |

**Response**: `JWT TOKEN`

### Get all coffee drinks

> It's possible to get a list of coffee drinks without creating user.

#### Get all coffee drinks without favorite state

| Label  | Value                 |
|--------|-----------------------|
| Method | **<code>GET</code>**  |
| URL    | /api/v1/coffee-drinks |

**Response:**
```
[
  {
        "id": 1,
        "name": "Americano",
        "imageUrl": "americano.png",
        "description": "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee.",
        "ingredients": "Espresso, Water"
    },
    {
        "id": 2,
        "name": "Cappuccino",
        "imageUrl": "cappuccino.png",
        "description": "A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with steamed milk foam.",
        "ingredients": "Espresso, Steamed milk foam"
    },
    ...
]
```

#### Get all coffee drinks with favorite state (TOKEN is needed)

| Label  | Value                          |
|--------|--------------------------------|
| Method | **<code>GET</code>**           |
| URL    | /api/v1/coffee-drinks          |
| Header | Authorization : `Bearer TOKEN` |

You can get token from response after [creating account](#create-a-new-account) or [login](#log-in) to the API.

**Response:**
```
[
    {
        "id": 1,
        "name": "Americano",
        "imageUrl": "americano.png",
        "description": "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee.",
        "ingredients": "Espresso, Water",
        "isFavorite": true
    },
    {
        "id": 2,
        "name": "Cappuccino",
        "imageUrl": "cappuccino.png",
        "description": "A cappuccino is an espresso-based coffee drink that originated in Italy, and is traditionally prepared with steamed milk foam.",
        "ingredients": "Espresso, Steamed milk foam",
        "isFavorite": false
    },
    ...
]
```

### Mark coffee drink as favorite

| Label  | Value                                                                                                   |
|--------|---------------------------------------------------------------------------------------------------------|
| Method | **<code>PATCH</code>**                                                                                  |
| URL    | /api/v1/coffee-drinks/`COFFEE_DRINK_ID`                                                                 |
| Header | Content-Type : application/json<br>Authorization : `Bearer TOKEN`                                       |
| Body   | {<br>&nbsp;&nbsp;&nbsp;&nbsp;"isFavorite": true &nbsp;// *true* - favorite, *false* - not favorite<br>} |

You can get token from response after [creating account](#create-a-new-account) or [login](#log-in) to the API.

**Response:**
```
{
    "isFavorite": true
}
```

### Get the coffee drink by ID

> It's possible to get the coffee drink information without creating a user.

#### Get the coffee drink by ID without favorite state

| Label  | Value                                   |
|--------|-----------------------------------------|
| Method | **<code>GET</code>**                    |
| URL    | /api/v1/coffee-drinks/`COFFEE_DRINK_ID` |

**Response:**
```
{
    "id": 1,
    "name": "Americano",
    "imageUrl": "americano.png",
    "description": "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee.",
    "ingredients": "Espresso, Water"
}
```

#### Get the coffee drink by ID with favorite state (TOKEN is needed)

| Label  | Value                                   |
|--------|-----------------------------------------|
| Method | **<code>GET</code>**                    |
| URL    | /api/v1/coffee-drinks/`COFFEE_DRINK_ID` |
| Header | Authorization : `Bearer TOKEN`          |

You can get token from response after [creating account](#create-a-new-account) or [login](#log-in) to the API.

**Response:**
```
{
    "id": 1,
    "name": "Americano",
    "imageUrl": "americano.png",
    "description": "Americano is a type of coffee drink prepared by diluting an espresso with hot water, giving it a similar strength to, but different flavour from, traditionally brewed coffee.",
    "ingredients": "Espresso, Water",
    "isFavorite": true
}
```

### Get user information

| Label  | Value                          |
|--------|--------------------------------|
| Method | **<code>GET</code>**           |
| URL    | /coffee-drinks-api/v1/users    |
| Header | Authorization : `Bearer TOKEN` |

You can get token from response after [creating account](#create-a-new-account) or [login](#log-in) to the API.

**Response**:
```
{
    "userId": 1,
    "name": "example_user",
    "email": "user@example.com"
}
```
