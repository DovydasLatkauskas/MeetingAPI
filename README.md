# Meeting Management Application

This is a web application built with Java Spring Boot to manage internal meetings.

### API
The application provides REST API endpoints to create, delete, add attendees to, remove attendees from, and list meetings.
### Data Storage
All meeting data is stored in a JSON file, however, the application can be easily extended to use a database instead through its repository interfaces.
### Security and authentication
There is user registration (with password hashing and JSON Web Tokens) and authentication functionality, which allows for checking if a user is allowed to delete a meeting.
### Testing
The application also includes unit tests to ensure the functionality of its components.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)

## Features

- Create a new meeting with details such as name, responsible person, description, category, type, start date, and end date.
- Delete a meeting (only the person responsible can delete the meeting).
- Add a person to a meeting, specifying the person and the time of addition.
- Display a warning if a person being added to a meeting is already part of another meeting that intersects with the current one.
- Prevent adding the same person twice to a meeting.
- Remove a person from a meeting (except for the person responsible for the meeting).
- List all meetings with filtering options:
    - Filter by description (partial match)
    - Filter by responsible person
    - Filter by category
    - Filter by type
    - Filter by dates (single date or date range)
    - Filter by the number of attendees (minimum number)

## Requirements

To run this application, make sure you have the following installed:

- Java Development Kit (JDK) 20 or later
- Apache Maven

## Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   ```

2. Navigate to the project directory:

   ```bash
   cd visma-meeting-management
   ```

3. Build the application using Maven:

   ```bash
   mvn clean package
   ```

## Usage

1. Run the application using Maven:

   ```bash
   mvn spring-boot:run
   ```

2. The application will start and be accessible at `http://localhost:8080`.

3. Use an API client (e.g., Postman, cURL) to interact with the application's REST API endpoints.

## API Endpoints

The following API endpoints are available:

- `POST /meetings`: Create a new meeting.
- `DELETE /meetings/{meetingId}`: Delete a meeting.
- `POST /meetings/{meetingId}/attendees`: Add a person to a meeting.
- `DELETE /meetings/{meetingId}/attendees/{attendeeId}`: Remove a person from a meeting.
- `GET /meetings`: List all meetings with optional filtering parameters.

## Testing

The application includes unit tests to ensure the functionality of its components. To run the tests, use the following command:

```bash
mvn test
```