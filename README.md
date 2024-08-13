# Real-Time Chat Application with Kotlin and Pusher

This project is a real-time chat application built using Kotlin for the backend and React for the frontend. It leverages Pusher for real-time communication and uses private channels for secure, user-to-user messaging.

## Features

- Real-time messaging using Pusher's private channels
- User authentication and authorization
- One-on-one chat functionality
- User presence detection (online/offline status)
- Spring Boot backend with Kotlin
- React frontend with TypeScript

## Prerequisites

Before you begin, ensure you have the following installed:
- JDK 11 or later
- Node.js 14 or later
- npm 6 or later
- Pusher account

## Setup

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/meenachinmay/kotlin-pusher.git
   ```

2. Navigate to the directory:
   ```bash
   cd kotlin-pusher
   ```

3. Create an `application.yml` file in `src/main/resources/` and add your Pusher credentials:
   ```yaml
   pusher:
     app-id: your_app_id
     key: your_app_key
     secret: your_app_secret
     cluster: your_app_cluster
   ```

4. Build and run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```

The backend server should now be running on `http://localhost:8080`.

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```

2. Install the dependencies:
   ```bash
   npm install
   ```

3. Create a `.env` file in the root of the frontend directory and add your Pusher key:
   ```
   REACT_APP_PUSHER_KEY=your_pusher_key
   REACT_APP_PUSHER_CLUSTER=your_pusher_cluster
   ```

4. Start the React development server:
   ```bash
   npm start
   ```

The frontend application should now be running on `http://localhost:3000`.

## Usage

1. Open your browser and go to `http://localhost:3000`.
2. Enter a username to join the chat.
3. Select a user from the list to start a private chat.
4. Type your message and press send to communicate in real-time.

## Project Structure

```
/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/yourcompany/chat/
│   │   │   │       ├── controller/
│   │   │   │       ├── service/
│   │   │   │       ├── model/
│   │   │   │       └── ChatApplication.kt
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/
│   └── build.gradle.kts
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── services/
│   │   ├── App.tsx
│   │   └── index.tsx
│   ├── package.json
│   └── tsconfig.json
└── README.md
```

## Key Components

### Backend

- `ChatController`: Handles HTTP requests for user management and message sending.
- `PusherController`: Manages Pusher authentication for private channels.
- `ChatService`: Contains the business logic for chat operations.
- `User` and `Message`: Data models for users and messages.

### Frontend

- `Chat`: Main component handling the chat interface and Pusher integration.
- `UserList`: Component for displaying online users.
- `MessageList`: Component for displaying chat messages.
- `MessageInput`: Component for sending new messages.

## Security Considerations

- This application uses Pusher's private channels to ensure that only authenticated users can subscribe to channels.
- The backend implements authentication for Pusher channels, preventing unauthorized access.
- Sensitive information like Pusher credentials are stored in configuration files that are not committed to the repository.

## Deployment

For deploying this application:

1. Set up a production-ready database (e.g., PostgreSQL).
2. Configure environment variables for both backend and frontend.
3. Build the frontend for production: `npm run build`.
4. Package the Spring Boot application: `./gradlew build`.
5. Deploy both the backend JAR and the frontend build to your hosting service.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
