# REBOUND Backend

REBOUND is the Spring Boot backend for a healthcare service app that supports VIP concierge nursing care, at-home patient support, care packages, medical staff assignment, ratings, and a rule-based "AI Nurse 24/7" chatbot for logged-in patients.

## Tech Stack
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL
- H2 for tests

## What the backend does
- Stores user, patient, medical staff, package, subscription, rating, chat session, and chat message data.
- Exposes REST APIs for CRUD operations and chat workflows.
- Routes chat messages through a safe rule-based assistant.
- Escalates risky, urgent, or clinical content to human follow-up.

## Environment Variables
Set these before running locally:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `SPRING_JPA_SHOW_SQL`
- `APP_CORS_ALLOWED_ORIGINS`
- `AI_PROVIDER`
- `OPENAI_API_KEY`
- `OPENAI_MODEL`

Use [src/main/resources/application-example.properties](src/main/resources/application-example.properties) as the reference template.

## MySQL Setup
Create a database named `rebound_db` or point `SPRING_DATASOURCE_URL` to your own database.

Example:
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/rebound_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
```

## How to Run Locally
1. Install Java 21 and Maven.
2. Start MySQL.
3. Set the environment variables above.
4. Run the app:
```bash
mvn spring-boot:run
```

## How to Test Locally
```bash
mvn test
```

## API Summary
### Users
- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Patients
- `GET /api/patients`
- `GET /api/patients/{id}`
- `POST /api/patients/user/{userId}`
- `PUT /api/patients/{id}`
- `DELETE /api/patients/{id}`

### Medical Staff
- `GET /api/medical-staff`
- `GET /api/medical-staff/{id}`
- `POST /api/medical-staff/user/{userId}`
- `PUT /api/medical-staff/{id}`
- `DELETE /api/medical-staff/{id}`
- `GET /api/medical-staff/{staffId}/patients`

### Packages
- `GET /api/packages`
- `GET /api/packages/{id}`
- `POST /api/packages`
- `PUT /api/packages/{id}`
- `DELETE /api/packages/{id}`

### Subscriptions
- `GET /api/subscriptions`
- `GET /api/subscriptions/{id}`
- `GET /api/subscriptions/patient/{patientId}`
- `POST /api/subscriptions/patient/{patientId}/package/{packageId}`
- `PUT /api/subscriptions/{subscriptionId}/medical-staff/{staffId}`
- `PUT /api/subscriptions/{subscriptionId}`
- `PUT /api/subscriptions/{subscriptionId}/cancel`
- `DELETE /api/subscriptions/{subscriptionId}`

### Ratings
- `GET /api/ratings`
- `GET /api/ratings/{id}`
- `GET /api/ratings/patient/{patientId}`
- `GET /api/ratings/medical-staff/{staffId}`
- `POST /api/ratings`
- `DELETE /api/ratings/{id}`

### Chat
- `POST /api/chat/sessions/patient/{patientId}`
- `GET /api/chat/sessions/patient/{patientId}`
- `GET /api/chat/sessions/{sessionId}`
- `GET /api/chat/sessions/{sessionId}/messages`
- `POST /api/chat/sessions/{sessionId}/messages`
- `PUT /api/chat/sessions/{sessionId}/close`
- `PUT /api/chat/sessions/{sessionId}/escalate`

## Domain Model Summary
- `User`: login identity, role, contact info, timestamps.
- `Patient`: patient profile linked to a user.
- `MedicalStaff`: nurse or medical staff profile linked to a user.
- `Package`: care package/service plan.
- `Subscription`: links a patient to a package and optionally a medical staff member.
- `ChatSession`: one chatbot/live-support conversation.
- `ChatMessage`: individual messages inside a chat session.
- `Rating`: patient feedback, sentiment, and ratings.

## Chatbot Safety
The chatbot is rule-based for now. It does not diagnose, prescribe, recommend medication dosage changes, replace a nurse or doctor, or give emergency medical advice.

It can:
- Explain REBOUND services and packages.
- Help users navigate the app.
- Summarize assigned package or medical staff details already stored in the app.
- Escalate risky or medical messages to human support.

Risky topics such as chest pain, difficulty breathing, overdose, suicidal thoughts, seizure, unconsciousness, severe bleeding, and similar urgent symptoms are marked for escalation.

## Future OpenAI Integration
The code is structured so a future `OpenAIChatService` can implement `AiChatService` without changing the controller layer. The current configuration already includes placeholder environment variables for provider selection and OpenAI credentials.
