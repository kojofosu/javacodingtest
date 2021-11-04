# javacodingtest

![Heroku](https://heroku-badge.herokuapp.com/?app=javacodingtest&root=/author&style=flat)

Java coding test

This is a Java Spring boot API for creating and managing posts.

## Built With

* [Spring](https://spring.io/) - The REST web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit](https://junit.org/junit5/) - The testing framework

## Getting Started

## PRODUCTION
The API is hosted on [heroku](https://www.heroku.com/). To access the API, visit [here](https://javacodingtest.herokuapp.com)

## LOCAL
### Prerequisites

* Maven
* Java (Java 11)

### Steps
To run the project locally, follow the steps below;
1. Clone a copy of the project from git
2. cd in to the project's root directory. e.g. `cd javacodingtest`
3. Build the project using maven, e.g. `mvn clean install`
4. Run the resulting jar, e.g. `java -jar target/javacodingtest-0.0.1-SNAPSHOT.jar --spring.config.location=src/main/resources/application.properties`

## Endpoints
### AUTHOR
`/author`
- `POST '/'`
  - Add a new author with the email being the ID
  - Returns status `201 CREATED` when successful.
  - Returns status `400 BAD_REQUEST` when email is invalid
  - Returns status `400 BAD_REQUEST` when phone number is greater or less than 10 digits
  - Returns status `409 CONFLICT` when author with email already exists.
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught
  - sample of the request body
```json
    {
      "firstname": "kojo",
      "lastname": "fosu",
      "email": "kojofosube@gmail.com",
      "phone": "0209152068"
    } 
   ```

- `GET '/'`
  - Fetch lists of authors
  - Returns status `200 OK` when successful
  - Returns an empty list with status `200 OK` when empty.
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `GET '/{email}'`
  - Fetch author with specified email
  - Returns status `200 OK` when successful
  - Returns `400 BAD_REQUEST` when email is invalid
  - Returns `404 NOT_FOUND` when no author exists with that email
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `PUT '/'`
  - Update author
  - Returns status `200 OK` when successful
  - Returns status `400 BAD_REQUEST` when email is invalid
  - Returns status `400 BAD_REQUEST` when phone number is greater or less than 10 digits
  - Returns status `404 NOT_FOUND` when no author exists with that email
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught
  

- `DELETE '/{email}'`
  - Delete an author
  - Returns `200 OK` when successful
  - Returns `400 BAD_REQUEST` when email is invalid
  - Returns `404 NOT_FOUND` when no author exists with that email
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught

  
### CATEGORY
`/category`
- `POST '/'`
  - Add new category
  - Return status `200 CREATED` when successful
  - Returns status `400 BAD_REQUEST` when category name is null or empty
  - Returns status `409 CONFLICT` when category already exists with specified name
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught
  - sample request body
```json
    {
      "name": "LIVE",
      "description": "it is LIVE"
    }
```


- `GET '/'`
  - Fetch list of categories
  - Returns status `200 OK` when successful
  - Returns an empty list with status `200 OK` when empty.
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `GET '/{name}'`
  - Fetch category by its name
  - Returns status `200 OK` when successful
  - Returns status `400 BAD_REQUEST` when is null or empty
  - Return status `404 NOT_FOUND` when no category with that name exists
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `PUT '/'`
  - Update category
  - Returns status `200 OK` when successful
  - Returns status `400 BAD_REQUEST` when category name is empty or null
  - Returns status `404 NOT_FOUND` when no category with specified name exists
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `DELETE '/{name}`
  - Delete a category
  - Returns status `200 OK` when successful
  - Returns `400 BAD_REQUEST` when category name is null or empty
  - Returns `404 NOT_FOUND` when no category exists with specified name
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught



### POST
`/post`
- `POST '/'`
  - Add new post
  - Returns status `201 CREATED` when successful
  - Returns status `400 BAD_REQUEST` when title is null or empty
  - Returns status `409 CONFLICT` when post with that title already exists
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught
  - sample request body
```json
{
  "title": "Accra",
  "text": "accra is dangerous",
  "categories": [
    {
      "name": "LIVE",
      "posts": [
        {
        "title": "Accra"
        }
      ]
    }
  ],
  "author": {
    "email": "edue@gmail.com"
  }
}
```


- `GET '/`
  - Get list of posts
  - Returns status `200 OK` when successful
  - Returns an empty list with status `200 OK` when empty
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `GET '/{title}'`
  - Fetch post by its title
  - Returns status `200 OK` when successful
  - Returns status `400 BAD_REQUEST` when title is empty or null
  - Returns status `404 NOT_FOUND` when post with specified title does not exist
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `PUT '/'`
  - Update a post
  - Returns `400 BAD_REQUEST` when title is null or empty
  - Returns `404 NOT_FOUND` when no post exists with specified title
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught


- `DELETE '/{tile]`
  - Delete a post
  - Returns status `200 OK` when successful
  - Returns status `400 BAD_REQUEST` when title is empty or null
  - Returns status `404 NOT_FOUND` when no post with specified title exists
  - Returns status `500 INTERNAL_SERVER_ERROR` when an exception is caught
