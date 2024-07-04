# Twitter-like API

This project is a Twitter-like API developed using Groovy and Spring Boot, backed by MongoDB. It allows users to register, log in, post messages, follow other users, and more. The application is containerized using Docker, making it easy to set up and deploy.

## Technology Stack

- **Programming Language:** Groovy
- **Framework:** Spring Boot
- **Database:** MongoDB
- **Build Tool:** Gradle
- **Testing Framework:** Spock
- **Containerization:** Docker

## Getting Started

### Prerequisites

- Docker
- Docker Compose

### Setup and Running

1. **Clone the repository**

    ```bash
    git clone https://github.com/yuriy-tsarenko/twitter-api.git
    ```

2. **Navigate to the project directory**

    ```bash
    cd twitter-api
    ```

3. **Environment Variables**

   Before running the application, you need to set up the necessary environment variables. Create a `.env` file in the project root with the following variables:

    ```plaintext
    MONGO_INITDB_ROOT_USERNAME=<your-mongodb-username>
    MONGO_INITDB_ROOT_PASSWORD=<your-mongodb-password>
    SPRING_DATA_MONGODB_URI=mongodb://<your-mongodb-username>:<your-mongodb-password>@mongo:27017/twitterdb
    ```

   Replace `<your-mongodb-username>` and `<your-mongodb-password>` with your MongoDB credentials.

4. **Build the Project**

   Before starting the application with Docker Compose, you need to build the project. Run the following command:

    ```bash
    ./gradlew build
    ```

5. **Docker Compose**

   The application and its dependencies can be easily started using Docker Compose. The `docker-compose.yml` file is configured to set up the application and MongoDB containers.

   - **To start the application**, run:

       ```bash
       docker-compose -f twitter-local-compose.yml up -d
       ```

   - **To stop the application**, run:

       ```bash
       docker-compose -f twitter-local-compose.yml down
       ```

## Testing

The application uses Spock for testing. To run the tests, execute the following command:

```bash
./gradlew test
