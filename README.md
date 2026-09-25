# Personal Expense Tracker

A REST API for tracking personal expenses and incomes, built with Spring Boot.

## Features

- **Accounts:** register, log in, update your profile, change your password, delete your account
- **JWT authentication:** short-lived access tokens (5 min) with refresh tokens (7 days, single use)
- **Logout:** from this device, or from all devices after a password change or account deletion
- **Login rate limiting:** 5 failed attempts per email lock that email out for 15 minutes (HTTP 429)
- **Expenses:** create, read, update, delete; each has a category
  (`Groceries`, `Leisure`, `Electronics`, `Utilities`, `Clothing`, `Health`, `Others`)
- **Incomes:** create, read, update, delete; each has a free-text source (e.g. `Salary`)
- **Listing:** pagination, sorting, date range filter, category/source filter
- **Totals:** total spent or earned, optionally per category/source (cached in Redis)
- **Privacy:** users can only see and change their own data
- **Validation and clear errors:** 400 / 401 / 403 / 404 / 409 / 429 with a readable message
- **API docs:** Swagger UI, plus a ready-made Postman collection

**Tech:** Java 26, Spring Boot 4.1, Spring Security, Spring Data JPA + SQLite, Redis, Docker.

## Install and run

### Option 1: Docker (recommended)

You only need [Docker](https://www.docker.com/).

```bash
docker compose up -d --build
```

This starts the app and Redis. The first build takes a few minutes. When it's done, the API is
at `http://localhost:8080`. Data is kept in a Docker volume between restarts.

To stop it:

```bash
docker compose down
```

### Option 2: Run locally

You need **JDK 26** and a running **Redis** on `localhost:6379`. The easiest way to get Redis is
Docker:

```bash
docker run -d -p 6379:6379 --name redis redis
```

Then start the app:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`. The database is created automatically as
`expense_tracker.db` in the project folder.

### Check that it's running

Open `http://localhost:8080/actuator/health`. It should show `{"status":"UP"}`.

## Test the API

Every endpoint except register, login and refresh needs a token. Register and log in first,
then send the token as `Authorization: Bearer <token>`.

### Swagger UI

Open **http://localhost:8080/swagger-ui/index.html**.

1. Call `POST /auth/register`, then `POST /auth/login`.
2. Copy the `token` from the login response.
3. Click **Authorize** at the top, paste the token, and confirm.
4. Try any other endpoint. The token is kept while you use the page.

### Postman

Import `PersonalExpenseTracker.postman_collection.json` from this repo.

1. Run **Auth → Register**, then **Auth → Login**. The token is saved automatically and used
   by every other request.
2. Run **Add Expense** / **Add Income**. The created ids are saved too, so the get, update and
   delete requests work without copying anything.
3. When the access token expires (after 5 minutes), run **Refresh Token**.
4. Run the requests in **Session** last: they log you out or delete the account.

### curl

```bash
curl -X POST localhost:8080/auth/register -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'

curl -X POST localhost:8080/auth/login -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

curl -X POST localhost:8080/expense -H "Authorization: Bearer <token>" -H "Content-Type: application/json" \
  -d '{"name":"Lunch","amount":12.50,"description":"Pizza","category":"Groceries","date":"2026-09-20"}'

curl "localhost:8080/user/expenses?category=Groceries&sort=amount,desc" -H "Authorization: Bearer <token>"
```

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/auth/register` | Create an account |
| POST | `/auth/login` | Log in, returns `token` and `refreshToken` |
| POST | `/auth/refresh` | Trade a refresh token for a new token pair |
| POST | `/auth/logout` | Log out this device (body: `refreshToken`) |
| GET | `/user/name` | Your name |
| PUT | `/user/update` | Update name and email |
| PUT | `/user/password` | Change password (logs out all devices) |
| DELETE | `/user/delete` | Delete your account and all its data |
| GET | `/user/expenses` | List expenses (`startDate`, `endDate`, `category`, `page`, `size`, `sort`) |
| GET | `/user/incomes` | List incomes (`startDate`, `endDate`, `source`, `page`, `size`, `sort`) |
| GET | `/user/expenses/total-amount` | Total expenses (optional `category`) |
| GET | `/user/incomes/total-amount` | Total incomes (optional `source`) |
| POST | `/expense` | Add an expense |
| GET | `/expense/{id}/name` · `/description` · `/amount` | Read one field of an expense |
| PUT | `/expense/{id}/update` | Update an expense |
| DELETE | `/expense/{id}` | Delete an expense |
| POST | `/income` | Add an income |
| GET | `/income/{id}/name` · `/description` · `/amount` | Read one field of an income |
| PUT | `/income/{id}/update` | Update an income |
| DELETE | `/income/{id}` | Delete an income |

Dates use the format `YYYY-MM-DD`.
