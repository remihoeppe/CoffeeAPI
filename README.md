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

## Package

```
./gradlew distZip
```
