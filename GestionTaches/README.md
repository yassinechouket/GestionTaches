# GestionTaches API Documentation

## Authentication

### Register
- **POST** `/api/v1/auth/register`
- Request: `{ "username": "string", "email": "string", "password": "string" }`
- Response: `{ "userId": number, "token": "string" }`

### Authenticate
- **POST** `/api/v1/auth/authenticate`
- Request: `{ "email": "string", "password": "string" }`
- Response: `{ "userId": number, "token": "string" }`

## Status

### Status Check
- **GET** `/api/status`
- Response: `{ "status": "ok", "timestamp": number }`

## Users

### Get All Users
- **GET** `/user` (Requires JWT token)
- Response: `[ { "id": number, "username": "string", "email": "string" }, ... ]`

## Tasks

### Create Task
- **POST** `/api/tasks` (Requires JWT token)
- Request: `{ "title": "string", "description": "string", "isCompleted": boolean, "priority": "LOW|MEDIUM|HIGH", "dueDate": "ISO8601", "assignedId": number|null }`
- Response: `{ "taskId": number }`

### Update Task
- **PUT** `/api/tasks/{id}` (Requires JWT token)
- Request: `{ "description": "string", "isCompleted": boolean, "priority": "LOW|MEDIUM|HIGH", "assignedId": number|null, "dueDate": "ISO8601" }`
- Response: `{ "success": true, "updatedAt": "ISO8601" }`

### Delete Task
- **DELETE** `/api/tasks/{id}` (Requires JWT token)
- Response: `{ "success": true }`

### Get All Tasks
- **GET** `/api/tasks` (Requires JWT token)
- Supports filtering:
  - Unassigned: `assignedId == null`
  - Assigned to current user: `assignedId == currentUserId`
  - Created by current user: `ownerId == currentUserId`
- Response: `[ { "taskId": number, "title": "string", "description": "string", "isCompleted": boolean, "priority": "LOW|MEDIUM|HIGH", "dueDate": "ISO8601", "ownerId": number, "assignedId": number|null }, ... ]`

### Get Task By ID
- **GET** `/api/tasks/{id}` (Requires JWT token)
- Response: `{ "taskId": number, "title": "string", "description": "string", "isCompleted": boolean, "priority": "LOW|MEDIUM|HIGH", "dueDate": "ISO8601", "ownerId": number, "assignedId": number|null }`

## Notes
- All endpoints except Register, Authenticate, and Status require JWT token in the `Authorization` header: `Bearer <token>`.
- Proper validation and error handling are implemented.
