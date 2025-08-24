# Missing Features and Planned API Extensions

## Missing Features from Current API

1. **Task Ownership**
   - Owner ID is not returned or set for tasks.
   - Cannot filter tasks by ownerId (created by current user).
2. **Task Assignment**
   - No support for setting or updating assignedId for tasks.
   - Cannot filter tasks by assignedId (assigned to current user or unassigned).
3. **Task Priority**
   - No support for setting or updating task priority.
4. **User Listing**
   - Get Users endpoint does not require token (should be secured).
5. **Task Filtering**
   - All Tasks endpoint does not support filtering by user context (ownerId, assignedId, unassigned).
6. **Task Deletion**
   - No endpoint for deleting tasks.
7. **Status Endpoint**
   - Status API is present but must be explicitly included in the spec.
8. **Validation & Error Handling**
   - Missing proper validation and error handling for bad input, unauthorized access, and not found resources.
9. **Username has to be Unique?**
   - There is no validation at signing up but when retrieving the listing of User it throws an error (breaking 7.) 

## Endpoints to Add or Modify

### POST
- **/api/v1/auth/register** (no token required)
  - Request: `{ username, email, password }`
  - Response: `{ userId, token }`
- **/api/v1/auth/authenticate** (no token required)
  - Request: `{ email, password }`
  - Response: `{ userId, token }`
- **/api/tasks** (requires token)
  - Request: `{ title, description, isCompleted, priority, dueDate, assignedId (nullable) }`
  - Response: `{ taskId }`

### PUT
- **/api/tasks/{id}** (requires token)
  - Request: `{ description, isCompleted, priority, assignedId, dueDate }`
  - Response: `{ success: true, updatedAt: timestamp }`

### DELETE
- **/api/tasks/{id}** (requires token)
  - Request: `{ taskId }`
  - Response: `{ success: true }`

### GET
- **/api/status** (no token)
  - Response: `{ status: "ok", timestamp }`
- **/user** (requires token)
  - Response: `[ { id, username, email }, ... ]`
- **/api/tasks** (requires token)
  - Supports filtering by:
    - Unassigned tasks → `assignedId == null`
    - Tasks assigned to current user → `assignedId == currentUserId`
    - Tasks created by current user → `ownerId == currentUserId`
  - Response: `[ { taskId, title, description, isCompleted, priority, dueDate, ownerId, assignedId }, ... ]`
- **/api/tasks/{id}** (requires token)
  - Response: `{ taskId, title, description, isCompleted, priority, dueDate, ownerId, assignedId }`

## Notes
- Maintain existing URL structure (e.g., `/api/v1/auth/...`, `/api/tasks/...`).
- Secure endpoints with JWT token auth (except Register, Authenticate, Status).
- Ensure `ownerId` is automatically set on task creation (from the authenticated user).
- Ensure proper validation and error handling (bad input, unauthorized, not found).
