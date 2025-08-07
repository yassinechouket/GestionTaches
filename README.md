# Task Management API

This API allows you to manage your personal tasks and to-do items. Think of it like a digital to-do list where you can create, update, complete, and organize your tasks.

The API is available at `http://localhost:8080`

## How It Works

This is a **personal task manager**. Each user has their own private task list - you can only see and manage your own tasks, not other people's tasks. You need to be logged in (authenticated) to use most features.

## What You Need to Know

- **Authentication Required**: You need a valid token (like a digital key) to access your tasks
- **JSON Format**: All data is sent and received in JSON format
- **Your Tasks Only**: You can only see/modify tasks that belong to you
- **Automatic Features**: Some tasks get archived automatically when completed and overdue

---

## API Endpoints

### Check if API is Running
```
GET /status
```
**Purpose**: See if the API server is working  
**Authentication**: Not required  
**Returns**: Status information

---

### Get All Your Tasks
```
GET /api/tasks
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Get a complete list of all your tasks  
**Authentication**: Required  
**Returns**: Array of all your tasks

**Example Response:**
```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, bread, eggs",
    "dueDate": "2025-08-08T17:00:00",
    "completed": false,
    "archived": false,
    "createdAt": "2025-08-05T12:00:00",
    "updatedAt": "2025-08-05T12:00:00"
  }
]
```

---

### Get One Specific Task
```
GET /api/tasks/1
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Get details about one specific task (replace "1" with task ID)  
**Authentication**: Required  
**Returns**: Single task object

---

### Create a New Task
```
POST /api/tasks
Authorization: Bearer <YOUR_TOKEN>
Content-Type: application/json

{
  "title": "Finish homework",
  "description": "Math and English assignments",
  "dueDate": "2025-08-09T18:00:00",
  "completed": false,
  "archived": false
}
```
**Purpose**: Create a brand new task  
**Authentication**: Required

**Request Fields:**
- `title` (Required): What's the task called?
- `description` (Optional): More details about the task
- `dueDate` (Optional): When should it be done? Format: YYYY-MM-DDTHH:MM:SS
- `completed` (Optional): Is it done? true or false (defaults to false)
- `archived` (Optional): Is it archived? true or false (defaults to false)

**Returns**: The newly created task with its assigned ID

---

### Update an Existing Task
```
PUT /api/tasks/1
Authorization: Bearer <YOUR_TOKEN>
Content-Type: application/json

{
  "title": "Updated task name",
  "description": "Updated details",
  "dueDate": "2025-08-10T12:00:00",
  "completed": true
}
```
**Purpose**: Change the details of an existing task  
**Authentication**: Required  
**Note**: You cannot update archived tasks

**Smart Feature**: If you mark a task as completed and it's already past its due date, it will automatically be archived!

---

### Delete a Task Forever
```
DELETE /api/tasks/1
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Permanently remove a task (cannot be undone!)  
**Authentication**: Required  
**Returns**: Nothing (just confirms deletion)

---

### Mark Task as Done
```
PUT /api/tasks/1/complete
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Mark a task as completed ✓  
**Authentication**: Required

---

### Mark Task as Not Done
```
PUT /api/tasks/1/uncomplete
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Mark a task as not completed (undo completion)  
**Authentication**: Required

---

### Archive a Task
```
PUT /api/tasks/1/archive
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Move task to archive (hide it from main list without deleting)  
**Authentication**: Required  
**Think of it as**: Moving old tasks to storage

---

### Bring Task Back from Archive
```
PUT /api/tasks/1/unarchive
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Move task back to main list from archive  
**Authentication**: Required

---

### Change When Task is Due
```
PUT /api/tasks/1/duedate?date=2025-08-15T14:30:00
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Change only the due date of a task  
**Authentication**: Required  
**Note**: Date must be in format YYYY-MM-DDTHH:MM:SS

---

### Auto-Clean Old Completed Tasks
```
POST /api/tasks/auto-archive
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Automatically archive all tasks that are:
- ✅ Completed
- 📅 Past their due date
- 📂 Not already archived

**Think of it as**: Spring cleaning for your task list!

---

### Get Today's Tasks
```
GET /api/tasks/today
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Get only tasks that are due today  
**Authentication**: Required  
**Perfect for**: "What do I need to do today?"

---

### Get Urgent Tasks (Due Soon)
```
GET /api/tasks/due-soon
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: Get tasks that are:
- ❌ Not completed yet
- 📂 Not archived
- ⏰ Due within next 24 hours

**Perfect for**: "What's urgent that I need to do?"

---

### Get All Completed Tasks
```
GET /api/tasks/completed
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: See all tasks you've finished  
**Authentication**: Required  
**Perfect for**: Seeing your accomplishments!

---

### Get All Archived Tasks
```
GET /api/tasks/archived
Authorization: Bearer <YOUR_TOKEN>
```
**Purpose**: See all tasks you've moved to archive  
**Authentication**: Required  
**Perfect for**: Finding old tasks you stored away

---

## Authentication (How to Login)

**You Need**: A valid JWT token (like a temporary password)

**How to Use**: Add this header to every request (except /status):
```
Authorization: Bearer <YOUR_TOKEN>
```

**Example**: If your token is "abc123", use:
```
Authorization: Bearer abc123
```

---

## Task Object - What Each Field Means

Every task has these properties:

| Field | What It Means | Example |
|-------|---------------|---------|
| `id` | Unique task number (automatically assigned) | 1, 2, 3... |
| `title` | Name of the task | "Buy groceries" |
| `description` | More details about the task | "Milk, bread, eggs" |
| `dueDate` | When it should be done | "2025-08-08T17:00:00" |
| `completed` | Is it finished? | true or false |
| `archived` | Is it stored away? | true or false |
| `createdAt` | When was it created? | "2025-08-05T12:00:00" |
| `updatedAt` | When was it last changed? | "2025-08-07T10:30:00" |

---

## Date Format

All dates use this format: **YYYY-MM-DDTHH:MM:SS**

**Examples:**
- August 8, 2025 at 5:00 PM = `2025-08-08T17:00:00`
- December 25, 2025 at 9:30 AM = `2025-12-25T09:30:00`

---

## Error Messages

**401 Unauthorized**: Your token is invalid or missing  
**404 Not Found**: Task doesn't exist or doesn't belong to you  
**403 Forbidden**: You can't do that (like updating an archived task)

---

## Complete Example - Creating and Managing a Task

### 1. Create a new task:
```
POST /api/tasks
Authorization: Bearer your_token_here
Content-Type: application/json

{
  "title": "Study for exam",
  "description": "Math final exam preparation",
  "dueDate": "2025-08-15T09:00:00",
  "completed": false
}
```

### 2. Mark it as complete:
```
PUT /api/tasks/1/complete
Authorization: Bearer your_token_here
```

### 3. Archive it:
```
PUT /api/tasks/1/archive
Authorization: Bearer your_token_here
```

That's it! Your task is now completed and stored away in the archive.
