# Coffee API

Our goal is to create an API that sends data about coffee roasters back to clients.

## Stories

### 1. Getting a list of all roasters

As an API client, I should be able to get a list of all the roasters in this API DB.
This list should include roasters names, websites (... and more later).

-> When client sends a GET request to `/roasters` endpoint, they should be send a JSON array containing all the roasters
in the database with the appropriate information.

### 2. Getting more information about a roaster

As an API client, I should be able to query for more information about a specific roaster.

-> when a client sends a GET request to `/roaster/{id}` or `/roasters/{name}`, it should be send a JSON object
containing more information about the specified roaster.

### 3. Adding a new roaster

As a client of the API, I should be able to add a new roaster to the API DB. To create a new roaster I will need to
supply, at the very least, a valid roaster name and website URL.

-> When a client sends a POST request to `/roasters`, accompanied with valid data, it should create a new roaster

### 4. Deleting a roaster

As a client of the API, I should be able to delete a roaster from the API DB.

-> When a client sends a DELETE request to `/roasters/{name}`, the corresponding roaster should be deleted and a 204
should be returned.
If no roaster can be found a 404 should be returned.

### 4. All CRUD should happen on a local DB

As a client of the API, I want the data I read from and write to, to be stored in a persistent way. For the time being a
local Postgres DB will do.

-> A local DB should be set up and a connection to it created within the application.
All CRUD method should be refactored to be working with that local DB.

## File Structure

    src
    └── main
        └── kotlin
            └── com
                └── coffee
                    └── api
                        ├── roaster
                        │   ├── RoasterTable.kt // Defines RoasterTable, RoasterCoffeeTable
                        │   ├── RoasterDAO.kt // RoasterDAO class with relationships
                        │   ├── RoasterService.kt // Business logic for roasters
                        │   ├── RoasterController.kt // API endpoints for roasters
                        │   ├── Roaster.kt // Roaster data model
                        │   └── RoasterMapper.kt // Mapping functions (like daoToModel)
                        ├── coffee
                        │   ├── CoffeeTable.kt // Defines CoffeeTable
                        │   ├── CoffeeDAO.kt // CoffeeDAO class
                        │   ├── CoffeeService.kt // Business logic for coffees
                        │   ├── CoffeeController.kt // API endpoints for coffees
                        │   └── Coffee.kt // Coffee data model
                        └── utils
                        ├── DbTransaction.kt // Database transaction helper
                        └── Mappings.kt // Any shared mapping logic

## Qs:

- Am I using lenses properly?
- Is the file structure adequate?
- What is the best way to handle missing params as such? Or should they be considered as invalid params? 404 or 400?
- Should there be one setup file with DB manips ran beforeAll tests?

## Package

```
./gradlew distZip
```
