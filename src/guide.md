# TODO Application API Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Security Architecture](#security-architecture)
3. [Authentication](#authentication)
4. [User Roles](#user-roles)
5. [Step-by-Step Usage Guide](#step-by-step-usage-guide)
6. [API Endpoints](#api-endpoints)
   - [Authentication Endpoints](#authentication-endpoints)
   - [User Endpoints](#user-endpoints)
   - [Category Endpoints](#category-endpoints)
   - [Task Endpoints](#task-endpoints)
7. [Testing with Postman](#testing-with-postman)
8. [Security Best Practices](#security-best-practices)
9. [Troubleshooting](#troubleshooting)

## Introduction

This TODO application is built with Spring Boot and uses Spring Security to provide robust authentication and authorization features. The application allows for managing tasks, users, and categories with a role-based access control system that differentiates between regular users and administrators.

## Security Architecture

The security architecture is based on the following components:

- **Spring Security:** Provides core security infrastructure
- **BCrypt Password Encoder:** Secures passwords with strong one-way encryption
- **Role-Based Access Control:** Uses Spring's `@PreAuthorize` annotations for fine-grained authorization
- **Stateless REST API:** Uses HTTP Basic Authentication with stateless sessions
- **CORS Configuration:** Configured for cross-origin requests

## Authentication

The application uses session-based authentication. When accessing protected endpoints, you must include proper authentication credentials. There are two ways to authenticate:

1. **Login Endpoint:** Use the `/auth/login` endpoint to authenticate and receive a session ID
2. **HTTP Basic Auth:** Include the `Authorization` header with Base64-encoded credentials

Authentication flow:
1. Register a user through `/auth/register`, `/auth/admin/register`, or `/users/register`
2. Login using username and password
3. Include the session ID cookie (JSESSIONID) in subsequent requests

## User Roles

The application supports two roles:

1. **USER:** Regular users who can:
   - Manage their own tasks
   - View and use categories
   - Update their own profile

2. **ADMIN:** Administrators who can:
   - Perform all USER operations
   - Manage categories (create, update, delete)
   - View all users
   - Delete users

## Step-by-Step Usage Guide

Follow these steps to properly use the TODO application:

### 1. Create an Admin User

First, create an administrator who can manage categories:
```
POST /auth/admin/register
```
Request body:
```json
{
  "username": "admin123",
  "password": "adminpassword123"
}
```

### 2. Create Categories

Admins can create categories that users can assign tasks to:
```
POST /categories
```
Request body:
```json
{
  "name": "Work"
}
```

### 3. Register Regular Users

Register regular users who can create and manage their own tasks:
```
POST /auth/register
```
Request body:
```json
{
  "username": "user123",
  "password": "password123"
}
```

### 4. Create Tasks

Users can create tasks and assign them to categories:
```
POST /tasks/{userId}/{categoryId}
```
Request body:
```json
{
  "title": "Complete project",
  "description": "Finish the Spring Security implementation",
  "status": "NOT_COMPLETED",
  "dueDate": "2023-08-15"
}
```

### 5. Manage Tasks

Users can update, delete, or mark tasks as completed using the respective endpoints.

## API Endpoints

### Authentication Endpoints

#### Register a New User
```
POST /auth/register
```
Request body:
```json
{
  "username": "user123",
  "password": "password123"
}
```
Notes:
- Creates a new user with the USER role

#### Register a New Admin
```
POST /auth/admin/register
```
Request body:
```json
{
  "username": "admin123",
  "password": "adminpassword123"
}
```
Notes:
- Creates a new user with the ADMIN role

#### Login
```
POST /auth/login
```
Request body:
```json
{
  "username": "user123",
  "password": "password123"
}
```
Response:
- Returns a session ID that can be used in subsequent requests

#### Logout
```
POST /auth/logout
```
Notes:
- Invalidates the current session

#### Get Current User Info
```
GET /auth/me
```
Response:
- Returns information about the currently authenticated user

### User Endpoints

#### Register a New User (Alternative)
```
POST /users/register
```
Request body:
```json
{
  "username": "user123",
  "password": "password123",
  "role": "USER"
}
```

#### Update User Profile
```
PUT /users/{id}
```
Security:
- Users can only update their own profile
- Admins can update any user

#### Get User by ID
```
GET /users/{id}
```
Security:
- Users can only access their own profile
- Admins can access any profile

#### Get All Users
```
GET /users/all
```
Security:
- Only accessible to ADMIN users

#### Delete User
```
DELETE /users/{id}
```
Security:
- Only accessible to ADMIN users
- Cannot delete a user with incomplete tasks

### Category Endpoints

#### Create a Category
```
POST /categories
```
Request body:
```json
{
  "name": "Work"
}
```
Security:
- Only accessible to ADMIN users

#### Update Category
```
PUT /categories/{id}
```
Security:
- Only accessible to ADMIN users

#### Get Category by ID
```
GET /categories/{id}
```
Security:
- Accessible to USER and ADMIN roles

#### Get All Categories
```
GET /categories/all
```
Security:
- Accessible to USER and ADMIN roles

#### Delete Category
```
DELETE /categories/{id}
```
Security:
- Only accessible to ADMIN users
- Cannot delete a category with incomplete tasks

#### Get Tasks by Category
```
GET /categories/{categoryId}/tasks
```
Security:
- Accessible to USER and ADMIN roles

### Task Endpoints

#### Create a Task
```
POST /tasks/{userId}/{categoryId}
```
Request body:
```json
{
  "title": "Complete project",
  "description": "Finish the Spring Security implementation",
  "status": "NOT_COMPLETED",
  "dueDate": "2023-08-15"
}
```
Security:
- Accessible to USER and ADMIN roles
- Users can only create tasks for themselves

#### Get All Tasks for a User
```
GET /tasks/user/{userId}
```
Security:
- Users can only view their own tasks
- Admins can view any user's tasks

#### Get Task by ID
```
GET /tasks/{id}/user/{userId}
```
Security:
- Users can only view their own tasks
- Admins can view any task

#### Update Task
```
PUT /tasks/{id}/user/{userId}
```
Security:
- Users can only update their own tasks
- Admins can update any task

#### Delete Task
```
DELETE /tasks/{id}/user/{userId}
```
Security:
- Users can only delete their own tasks
- Admins can delete any task

#### Get Tasks by Status
```
GET /tasks/user/{userId}/status/{status}
```
Values for status:
- NOT_COMPLETED
- COMPLETED

#### Get Tasks by Category
```
GET /tasks/category/{categoryId}
```

#### Mark Task as Completed
```
PUT /tasks/{id}/user/{userId}/complete
```

## Testing with Postman

To test the API, use Postman or a similar tool. Follow these steps:

1. Set up authentication by logging in and storing the session ID cookie.
2. Test endpoints in the order specified in the Step-by-Step Usage Guide.
3. Verify responses and ensure proper role-based access control.

## Security Best Practices

1. **Password Management:**
   - Passwords are stored using BCrypt with strength factor 12
   - Never store or transmit passwords in plain text
   - Use strong passwords with mixed case, numbers, and symbols

2. **Authentication:**
   - Token sessions expire automatically
   - Logout when done to invalidate the session
   - Don't share tokens between applications

3. **Authorization:**
   - The application enforces strict role-based access control
   - Users can only access their own resources
   - Admin privileges should be granted sparingly

4. **API Security:**
   - Use HTTPS in production
   - Set specific CORS origins in production
   - Validate all input data

## Troubleshooting

### Common Error Responses

- **401 Unauthorized:** Authentication credentials are missing or invalid
- **403 Forbidden:** User does not have permission to access the resource
- **404 Not Found:** The requested resource does not exist

### Authentication Issues

If you're having trouble authenticating:
1. Verify your username and password
2. Ensure you're including the proper Authorization header
3. Check that your session hasn't expired

### Authorization Issues

If you're getting 403 Forbidden errors:
1. Verify that your user has the correct role for the operation
2. If attempting to access another user's resources, you need ADMIN privileges
3. Check that you're accessing your own resources if you're a regular user
