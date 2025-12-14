# OpenLibrary
OpenLibrary is a application for libraries administrators, integrating distributed systems.

## API Endpoints
#### Manage book records
- `-X GET /api/books`: Retrieve a list of all books
- `-X POST /api/books`: Add a new book
- `-X GET /api/books/{id}`: Retrieve details of a specific book
- `-X PUT /api/books/{id}`: Update details of a specific book
- `-X DELETE /api/books/{id}`: Delete a specific book

#### Manage book rentals
- `-X GET /api/rentings`: Retrieve a list of all rentings
- `-X POST /api/rentings`: Rent a book (add new Renting record. Set the book as not available)
- `-X GET /api/rentings/{id}`: Retrieve details of a specific renting
- `-X PUT /api/rentings/{id}`: Update details of a specific renting
- `-X DELETE /api/rentings/{id}`: Return a rented book (delete Renting record. Set the book as available)

#### Authentication with Keycloak

### Curl commands to test the API
- Get all books:
  ```bash
  curl -X GET http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```
- Get all rentings:
  ```bash
  curl -X GET http://localhost:8080/api/rentings \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```
- Add a new book:
  ```bash
  curl -X POST http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN' \
  -d '{"title":"Book Title","author":"Author Name","isbn":"1234567890"}'
  ```
- Rent a book:
  ```bash
  curl -X POST http://localhost:8080/api/rentings \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN' \
  -d '{"bookId":1,"renterFirstName":"Vahan", "renterLastName":"Ducher", "renterEmail":"vahan.ducher@random.com"}'
  ```
- Return a book:
  ```bash
  curl -X DELETE http://localhost:8080/api/rentings/{id} \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```

Replace `YOUR_API_TOKEN` with a valid token obtained from Keycloak.


