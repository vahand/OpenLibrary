
**Environment**: Local Docker Compose (Windows)
---

## Step 1: List All Existing Books
**Command**:
```powershell
# 1. Get Access Token
$token = (docker run --rm --network openlibrary-1_default curlimages/curl -s -X POST http://library-keycloak:8080/realms/library/protocol/openid-connect/token -d "grant_type=client_credentials&client_id=test-client&client_secret=secret123" | ConvertFrom-Json).access_token

# 2. Get Books
docker run --rm --network openlibrary-1_default curlimages/curl -s -H "Authorization: Bearer $token" http://library-backend:8080/api/books
```

**Output**:
```json
[
  {
    "id": 1,
    "title": "Test Book",
    "author": "Test Author",
    "isbn": "123456",
    "available": false
  },
  {
    "id": 2,
    "title": "Test Book",
    "author": "Me",
    "isbn": "12345",
    "available": false
  },
  {
    "id": 3,
    "title": "Leben in Deutschland",
    "author": "MKV",
    "isbn": "989798",
    "available": true
  },
  {
    "id": 4,
    "title": "The Pragmatic Programmer",
    "author": "Andy Hunt",
    "isbn": "978-0201616224",
    "available": true
  }
]
```

---

## Step 2: Add New Books
### Book 1: B1.1

**Command**:
```powershell
# Add Book
docker run --rm --network openlibrary-1_default curlimages/curl -s -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{"title":"B1.1","author":"MKV","isbn":"8899-9989"}' http://library-backend:8080/api/books
```

**Output**:
```json
{
  "id": 5,
  "title": "B1.1",
  "author": "MKV",
  "isbn": "8899-9989",
  "available": true
}
```

### Book 2: C1.1
**Command**:
```powershell
# Add Book
docker run --rm --network openlibrary-1_default curlimages/curl -s -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{"title":"C1.1","author":"VDD","isbn":"7688-6654"}' http://library-backend:8080/api/books
```

**Output**:
```json
{
  "id": 6,
  "title": "C1.1",
  "author": "VDD",
  "isbn": "7688-6654",
  "available": true
}
```

**NATS Note**: 
Checked `library-notification` logs. The service is configured to listen only to `book.rented` and `book.returned`.

---

## Step 3: Rent a Book - B1.1 (ID 5)
- **Renter**: (vahan.ducher@random.com)

**Command**:
```powershell
docker run --rm --network openlibrary-1_default curlimages/curl -s -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{"bookId":5,"renterFirstName":"Vahan","renterLastName":"Ducher","renterEmail":"vahan.ducher@random.com"}' http://library-backend:8080/api/rentings
```

**Output**:
```json
{
  "id": 4,
  "bookId": 5,
  "renterFirstName": "Vahan",
  "renterLastName": "Ducher",
  "renterEmail": "vahan.ducher@random.com",
  "rentDate": "2025-12-14T19:04:05.616332095",
  "returnDate": "2025-12-28T19:04:05.619367335"
}
```

**NATS-Verification**:
*BackendLog (`library-backend`)*:
```
Published event to book.rented: {"to":"vahan.ducher@random.com","body":"You have successfully rented the book 'B1.1'. Return by: 2025-12-28T19:04:05.619367335","subject":"Book Rented: B1.1","eventType":"BOOK_RENTED"}
```

*NotificationLog (`library-notification`)*:
```
--------------------------------------------------
RECEIVED EVENT FROM NATS
Topic: book.rented
Event Type: BOOK_RENTED
To: vahan.ducher@random.com
Subject: Book Rented: B1.1
Body: You have successfully rented the book 'B1.1'. Return by: 2025-12-28T19:04:05.619367335
--------------------------------------------------
```

---

## Step 3.1: Rent Additional Book (Leben in Deutschland(ID 3))
- **Renter**: Vahan Ducher

**Command**:
```powershell
docker run --rm --network openlibrary-1_default curlimages/curl -s -X POST -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{"bookId":3,"renterFirstName":"Vahan","renterLastName":"Ducher","renterEmail":"vahan.ducher@random.com"}' http://library-backend:8080/api/rentings
```

**Output**:
```json
{
  "id": 5,
  "bookId": 3,
  "renterFirstName": "Vahan",
  "renterLastName": "Ducher",
  "renterEmail": "vahan.ducher@random.com",
  "rentDate": "2025-12-14T19:10:42.777934221",
  "returnDate": "2025-12-28T19:10:42.781038941"
}
```

**NATS-Verification**:
*BackendLog*:
```
Published event to book.rented: {"to":"vahan.ducher@random.com","body":"You have successfully rented the book 'Leben in Deutschland'. Return by: 2025-12-28T19:10:42.781038941","subject":"Book Rented: Leben in Deutschland","eventType":"BOOK_RENTED"}
```

*NotificationLog*:
```
--------------------------------------------------
RECEIVED EVENT FROM NATS
Topic: book.rented
Event Type: BOOK_RENTED
Subject: Book Rented: Leben in Deutschland
--------------------------------------------------
```

---

## Step 4: Get All Rentings
**Command**:
```powershell
docker run --rm --network openlibrary-1_default curlimages/curl -s -H "Authorization: Bearer $token" http://library-backend:8080/api/rentings
```

**Output**:
```json
[
  {
    "id": 1,
    "bookId": 1,
    "renterFirstName": "MK",
    "renterLastName": "Vishwasrao",
    "rentDate": "2025-12-11T16:56:51.528794968"
  },
  {
    "id": 2,
    "bookId": 2,
    "renterFirstName": "Test",
    "renterLastName": "User",
    "rentDate": "2025-12-14T15:42:10.378500469"
  },
  {
    "id": 4,
    "bookId": 5,
    "renterFirstName": "Vahan",
    "renterLastName": "Ducher",
    "rentDate": "2025-12-14T19:04:05.616332095"
  },
  {
    "id": 5,
    "bookId": 3,
    "renterFirstName": "Vahan",
    "renterLastName": "Ducher",
    "rentDate": "2025-12-14T19:10:42.777934221"
  }
]
```

---

## Step 5: Return a Book
**Command**:
```powershell
docker run --rm --network openlibrary-1_default curlimages/curl -s -X DELETE -H "Authorization: Bearer $token" http://library-backend:8080/api/rentings/5
```

**Output**:
Returns 200/204 OK

**NATS-Verification**:

*BackendLog*:
```
Published event to book.returned: {"to":"vahan.ducher@random.com","body":"You have successfully returned the book. Thank you!","subject":"Book Returned: Leben in Deutschland","eventType":"BOOK_RETURNED"}
```

*NotificationLog*:
```
--------------------------------------------------
RECEIVED EVENT FROM NATS
Topic: book.returned
Event Type: BOOK_RETURNED
To: vahan.ducher@random.com
Subject: Book Returned: Leben in Deutschland
Body: You have successfully returned the book. Thank you!
--------------------------------------------------
```
---

## Postgres0updates
**Command**:
```powershell
docker exec -i library-postgres psql -U admin -d librarydb -c "SELECT * FROM book;" -c "SELECT * FROM renting;"
```

**Output Analysis**:
1. **Books Table** (6 rows total):
   - Book ID 5 (B1.1): `available = f` (currently rented)
   - Book ID 6 (C1.1): `available = t` (never rented)
   - Book ID 3 (Leben in Deutschland): `available = t` (returned in Step5)

2. **Rentings Table** (3 rows total):
   - Rental ID 4: Active rental for Book ID5 by Vahan Ducher.
   - Rental ID 5: **Absent** (deleted in Step5).
