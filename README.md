# ExpenseManagement

![](https://img.shields.io/github/workflow/status/Runtime-Terror-G3/expense-management-back-end/Build)

## Backend JavaDocs
[https://runtime-terror-g3.github.io/expense-management-back-end/](https://runtime-terror-g3.github.io/expense-management-back-end/)

## Endpoints

The base path for all API requests is `/api/expense-management`, the
following path for each endpoint should be appended to this.

All request bodies are in the json format, only the field names and
their type will be mentioned for brevity. Also, the header
`Content-Type: application/json` is only required for some of such
requests, but there should be no harm in using it on each of them.

For requests that require the `Authorization` header, the format is
`Authorization: Bearer <token>`, where `<token>` should be replaced
with the JWT obtained from the `/sign-in` endpoint.

Expense categories are: `Food`, `Housekeeping`, `Education`, `Health`,
`SelfCare`, `Entertainment`, `Clothing`, `Other`.

Vendors are: `Altex`, `Cel`, `Other`.

A python script with functions and example calls for all endpoints
is at `testing_utils/rest_testing.py`.

### `/create-account`

Creates an account.

* Method: `POST`

* Body fields
	* `email`: string
	* `password`: string (hex string of a sha256 hash)
	* `firstName`: string
	* `lastName`: string
	* `dateOfBirth`: string (yyyy-mm-dd)

### `/sign-in`

Obtains a session token to be used on endpoints that require
authorization.

* Method: `POST`

* Body fields
	* `email`: string
	* `password`: string (hex string of a sha256 hash)

* Success response: a json string with the field `token` containing
  a session JWT
  	* JWT fields
		* `id`: int
		* `first_name`: string
		* `last_name`: string
		* `exp`: int
		* `iat`: int
	* example: `{"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsYXN0X25hbWUiOiJmaXJzdCIsImlkIjo2LCJleHAiOjE2MzkzNDA0MjU3ODksImZpcnN0X25hbWUiOiJmaXJzdCIsImlhdCI6MTYzOTMxMTYyNTc4OX0.LKpzx_0AE2gHpwqiZcjJeWnOMwenhmhfCUv0v0gvqBE"}`

### `/add-expense`

Adds an expense.

* Method: `POST`

* Required headers:
	* `Content-Type: application/json`
	* `Authorization: Bearer <token>`

* Body fields
	* `amount`: float
	* `category`: string (one of the expense categories)
	* `date`: string (yyyy-mm-ddThh:mm:ssZ)

### `/delete-expense/<expense_id>`

Deletes an expense.

* Method: `DELETE`

* Path variables
	* `expense_id`: int

* Required headers:
	* `Authorization: Bearer <token>`

### `/get-expenses?category=<...>&startDate=<...>&endDate=<...>`

Retrieves user expenses filtered by category and date interval.

* Method: `GET`

* Query parameters
	* `category`: string (one of the expense categories or `All`)
	* `startDate`: string (UNIX timestamp)
	* `endDate`: string (UNIX timestamp)

* Required headers:
	* `Authorization: Bearer <token>`

* Success response: json list of expense objects
	* expense object format
		* `id`: int
		* `amount`: float
		* `category`: string (one of the expense categories)
		* `date`: string (yyyy-mm-ddThh:mm:ssZ)
	* example:
	  `[{"id":11,"amount":123.0,"category":"Food","date":"1970-01-02T00:00:00","user":null}]`

### `/update-expense/<expense_id>`

Updates an expense.

* Method: `POST`

* Path variables
	* `expense_id`: int

* Required headers:
	* `Content-Type: application/json`
	* `Authorization: Bearer <token>`

* Body fields
	* `amount`: float
	* `category`: string (one of the expense categories)
	* `date`: string (yyyy-mm-ddThh:mm:ssZ)

### `/total-expenses-in-time?granularity=<...>&category=<...>&startDate=<...>&endDate=<...>`

Retrieves user expense sums with given granularity, filtered by
category and date interval.

* Method: `GET`

* Query parameters
	* `granularity`: string (one of `day`, `month`, `year`)
	* `category`: string (one of the expense categories or `All`)
	* `startDate`: string (yyyy-mm-dd)
	* `endDate`: string (yyyy-mm-dd)

* Required headers:
	* `Authorization: Bearer <token>`

* Success response: json list of sum objects
	* sum object format
		* `amount`: float
		* `dateTime`: string (yyyy-mm-dd)
	* example:
	  `[{"amount":123.0,"dateTime":"1970-01-02"},{"amount":1.1,"dateTime":"1970-01-01"}]`

### `/category-total?start=<...>&end=<...>`

Retrieves user expense sums for each category that has expenses.

* Method: `GET`

* Query parameters
	* `start`: string (yyyy-mm-ddThh:mm:ssZ)
	* `end`: string (yyyy-mm-ddThh:mm:ssZ)

* Required headers:
	* `Authorization: Bearer <token>`

* Success response: json object with a field for each category that
  has expenses, the value of the field being a float with the sum
  for that category
	* example: `{"Housekeeping":435.0,"Food":124.1}`

### `/add-monthly-budget`

Adds a monthly budget.

* Method: `POST`

* Required headers:
	* `Content-Type: application/json`
	* `Authorization: Bearer <token>`

* Body fields
	* `income`: float
	* `date`: string (yyyy-mm-ddThh:mm:ssZ)

### `/delete-monthly-budget/<budget_id>`

Deletes a monthly budget.

* Method: `DELETE`

* Path variables
	* `budget_id`: int

* Required headers:
	* `Authorization: Bearer <token>`

### `/get-monthly-budgets?startDate=<...>&endDate=<...>`

Retrieves user monthly budgets filter by date interval.

* Method: `GET`

* Query parameters
	* `startDate`: string (yyyy-mm-dd)
	* `endDate`: string (yyyy-mm-dd)

* Required headers:
	* `Authorization: Bearer <token>`

* Success response: json list of monthly budget objects
	* monthly budget object format
		* `id`: int
		* `income`: float
		* `date`: string (yyyy-mm-dd hh:mm:ss)
	* example:
	  `[{"id":2,"income":4321.0,"date":"1970-02-01 06:00:00"}]`

### `/update-monthly-budget/<budget_id>`

Updates a monthly budget.

* Method: `POST`

* Path variables
	* `budget_id`: int

* Required headers:
	* `Content-Type: application/json`
	* `Authorization: Bearer <token>`

* Body fields
	* `income`: float
	* `date`: string (yyyy-mm-ddThh:mm:ssZ)

### `/add-wishlistItem`

Adds a whishlist item.

* Method: `POST`

* Required headers:
	* `Content-Type: application/json`
	* `Authorization: Bearer <token>`

* Body fields
	* `title`: string
	* `price`: float
	* `link`: string
	* `image`: string
	* `vendor`: string (one of the vendors)

### `/get-wishlist-items`

Retrieves user wishlist items.

* Method: `GET`

* Required headers:
	* `Authorization: Bearer <token>`

* Success response: json list of wishlist item objects
	* wishlist item object format
		* `id`: int
		* `title`: string
		* `price`: float
		* `link`: string
		* `image`: string
		* `vendor`: string (one of the vendors)
	* example:
	  `[{"id":1,"title":"item","price":123.0,"link":"https://store.asd/item","image":"https://store.asd/item-image","vendor":"Altex"}]`
