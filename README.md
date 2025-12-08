# OpenLibrary
OpenLibrary is a application for libraries administrators, integrating distributed systems.

## API Endpoints
- `/api/books`: Manage book records
- `-X GET /api/books`: Retrieve a list of all books
- `-X POST /api/books`: Add a new book
- `-X GET /api/books/{id}`: Retrieve details of a specific book
- `-X DELETE /api/books/{id}`: Delete a specific book

- `/api/rentings`: Manage book rentals

### Curl commands to test the API
- Get all books:
  ```bash
  curl -X GET http://localhost:8080/api/books \
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
