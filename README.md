# SafeCircle Backend
## Overview
SafeCircle is a personal safety app designed for youth and young adults, offering emergency alerts to your safety circles or everyone in cases of absolute emergency. It features customizable alerts, location sharing, and Event Circles for staying connected during events, all for free. SafeCircle addresses the need for accessible safety solutions, making it unique and user-friendly.
## Functions

### Alerts
- **Create an alert:** A user can create a task.
- **View alerts on the map:** A user can get an overview of all their tasks.
- **Track users** A user can be tracked when they send an alert (only if the alert is still active).

### Circles
- **Create a circle:** A user can create a circle.
- **Manage circle members:** A user can be added to the circle through invitation only.
- **Delete a circle:** A circle could be deleted a member.
- **View circles:** A user can get an overview of all their circles.

### Events
- **View events:** A user gets an overview of the events that start 2 days in advanced.
- **Join event circles:** A user can join an event circle to stay updated during the event.

### Users
- **Login:** A user can log in.
- **Register:** A user can register a new account.
- **Update:** A user can update their information.

## Installation
### Requirments
- **JDK 21:** Ensure you have JDK 21 installed to run the project.
- **MySQL:** MySQL is required for database management. Ensure it is installed and properly configured. Alternatively, you can opt for other database systems, such as Oracle DB, if preferred.

### Steps
**clone the project from github**

### Backend
1. Navigate to the `/backend` directory.
2. Load Maven.
3. Open the project structure and select the correct SDK (JDK version 21).
4. Go to `backend/src/main/resources` and create an `application.properties` file. Add your database connection details and JWT secret in the file.
5. Once Maven is loaded and the `application.properties` file is configured, you can start the `BackendServer`.

### Frontend
- **Mobile**: Follow the steps in "**https://github.com/ebenhaj2005/safecircle_mobile/**".
- **Web**(Admin): Follow the steps in "**https://github.com/Youmni/safecircle_web/**".
  
## Technologies
1. **Spring Boot**: Used for building the backend of the application.
2. **MySQL**: Used for managing the database.

### Sources
1. [@Relations: Many to Many](https://www.baeldung.com/jpa-many-to-many)
2. [@Relations: One to Many](https://www.baeldung.com/hibernate-one-to-many)
3. [@Validation](https://www.baeldung.com/spring-boot-bean-validation)
4. [@JWT-token](https://connect2id.com/products/nimbus-jose-jwt)
5. [@Expo tokens](https://chatgpt.com/share/677d19ab-dc00-8002-a0f6-945cd41e8214)
6. [@Dependency injection](https://medium.com/@reetesh043/spring-boot-dependency-injection-137f85f84590)

### Authors
- [@Youmni Malha](https://github.com/Youmni)
- [@Mathias Mertens](https://github.com/mathias782)
- [@Thomas van der Borght](https://github.com/ThomasVanderBorght)
