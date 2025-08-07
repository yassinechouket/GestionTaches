# Task Management API

This API allows you to manage your personal tasks and to-do items.

The API is available at `http://localhost:8080`

## Endpoints

### Status
GET `/status`

Returns the status of the API.

### List of tasks
GET `/api/tasks`

Returns a list of all tasks for the authenticated user. Requires authentication.

### Get a single task
GET `/api/tasks/:taskId`

Retrieve detailed information about a specific task. Requires authentication.

### Create a new task
POST `/api/tasks`

Allows you to create a new task. Requires authentication.

The request body needs to be in JSON format and include the following properties:
- `title` - String - Required
- `description` - String - Optional
- `dueDate` - String (ISO 8601 format) - Optional
- `completed` - Boolean - Optional (defaults to false)
- `archived` - Boolean - Optional (defaults to false)

Example
```
POST /api/tasks
Authorization: Bearer <YOUR TOKEN>
{
  "title": "Finish Spring Boot project",
  "description": "API should be done by Friday",
  "dueDate": "2025-08-09T18:00:00",
  "completed": false,
  "archived": false
}
```

The response body will contain the created task with its assigned ID.

### Update a task
PUT `/api/tasks/:taskId`

Update an existing task. Requires authentication.

The request body needs to be in JSON format and allows you to update the following properties:
- `title` - String
- `description` - String
- `dueDate` - String (ISO 8601 format)
- `completed` - Boolean

Note: Archived tasks cannot be updated.

Example
```
PUT /api/tasks/1
Authorization: Bearer <YOUR TOKEN>
{
  "title": "Updated Title",
  "description": "Updated description",
  "dueDate": "2025-08-10T12:00:00",
  "completed": true
}
```

### Delete a task
DELETE `/api/tasks/:taskId`

Delete an existing task permanently. Requires authentication.

The request body needs to be empty.

Example
```
DELETE /api/tasks/1
Authorization: Bearer <YOUR TOKEN>
```

### Mark task as completed
PUT `/api/tasks/:taskId/complete`

Marks a task as completed. Requires authentication.

### Mark task as not completed
PUT `/api/tasks/:taskId/uncomplete`

Marks a task as not completed. Requires authentication.

### Archive a task
PUT `/api/tasks/:taskId/archive`

Archives a task. Requires authentication.

### Unarchive a task
PUT `/api/tasks/:taskId/unarchive`

Unarchives a task. Requires authentication.

### Change due date
PUT `/api/tasks/:taskId/duedate`

Changes the due date of a task. Requires authentication.

Query parameters:
- `date` - String (ISO 8601 format) - Required

Example
```
PUT /api/tasks/1/duedate?date=2025-08-10T17:00:00
Authorization: Bearer <YOUR TOKEN>
```

### Auto-archive overdue completed tasks
POST `/api/tasks/auto-archive`

Automatically archives tasks that are completed, have a due date in the past, and are not already archived. Requires authentication.

### Get tasks due today
GET `/api/tasks/today`

Returns tasks due today (from 00:00 to 23:59). Requires authentication.

### Get tasks due soon
GET `/api/tasks/due-soon`

Returns tasks that are not completed, not archived, and due in the next 24 hours. Requires authentication.

### Get completed tasks
GET `/api/tasks/completed`

Returns all tasks that are marked as completed. Requires authentication.

### Get archived tasks
GET `/api/tasks/archived`

Returns all archived tasks. Requires authentication.

## API Authentication

All endpoints (except status) require JWT authentication.

**Header format:**
```
Authorization: Bearer <YOUR TOKEN>
```

You need to be authenticated to access or modify any task. Each task is linked to the logged-in user.

## Task Object

A task contains the following fields:

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Unique identifier of the task |
| `title` | String | Title of the task |
| `description` | String | Details about the task |
| `dueDate` | String | When the task is due (ISO 8601 format) |
| `completed` | Boolean | If the task is completed |
| `archived` | Boolean | If the task is archived |
| `createdAt` | String | When the task was created (ISO 8601 format) |
| `updatedAt` | String | When the task was last updated (ISO 8601 format) |

Example task object:
```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs",
  "dueDate": "2025-08-08T17:00:00",
  "completed": false,
  "archived": false,
  "createdAt": "2025-08-05T12:00:00",
  "updatedAt": "2025-08-05T12:00:00"
}
```
