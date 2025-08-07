# 📝 Task Management API - Full Documentation

Base URL:


http://localhost:8080/api/tasks

## 🔐 Authentication

All endpoints require JWT authentication.

Header format:

Authorization: Bearer <YOUR_TOKEN>

You need to be authenticated to access or modify any task. Each task is linked to the logged-in user.

## 📋 Endpoints

### 1. Get All Tasks
  
 GET /api/tasks
   
Returns all tasks of the authenticated user.

Response:

[
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
]


### 2. Get Task by ID

GET /api/tasks/{id}

Returns a specific task if it belongs to the user.

Response:

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
