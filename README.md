for admin user name and password

username : yagnik
password : yagnik1505


for creating user

POST http://localhost:8080/users/add

{
"username" : "Dev",
"password" : "dev123"
}


Post http://localhost:8080/categories/

{
"name" : "Sleeping"

}

for admin

see all the categories

Get http://localhost:8080/categories/all


category by id

Get http://localhost:8080/categories/8



update by id

Put http://localhost:8080/categories/4

{
"name" : "Sleeping"

}


for user


all the tasks of user

Get http://localhost:8080/tasks

for

Post http://localhost:8080/tasks/4


{
"title": "Finish Java Project Report",
"description": "Prepare and finalize the Spring Boot project report.",
"status": "NOT_COMPLETED",
"dueDate": "2026-04-06"
}

get all the tasks

Get http://localhost:8080/tasks



get tasks by id

Get http://localhost:8080/tasks/4



delete by id

Delete http://localhost:8080/tasks/4


get tasks by status

Get http://localhost:8080/tasks/status/NOT_COMPLETED


get all tasks of category by

Get http://localhost:8080/tasks/category/4



for Complete the task

PUT http://localhost:8080/tasks/5/complete




Get http://localhost:8080/tasks/category/4