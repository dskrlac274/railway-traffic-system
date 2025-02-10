# 🚉 Railway Traffic System

## 📌 Overview
The **Railway Traffic System** is a command-line application designed for managing railway networks, schedules, and ticketing operations. It provides functionality for **train scheduling, dynamic route adjustments, ticket purchasing, and railway status management**. 

Built with **scalability and efficiency in mind**, the system incorporates multiple **design patterns** to ensure a clean, maintainable, and flexible architecture.

---

## 🔥 Key Features
- **📌 Dynamic Railway Infrastructure Management**  
  - Track and update railway networks, stations, and routes.
  - Modify track conditions (operational, under maintenance, closed).
  - Multi-route travel planning with automatic adjustments.

- **🎟️ Smart Ticketing System**  
  - Purchase tickets dynamically based on train type and travel conditions.
  - Apply discounts for **weekend travel** and **online purchases**.
  - Undo/rollback ticket purchases using **Memento & Command patterns**.

- **🚂 Train Schedule Management**  
  - Display train schedules, stops, and estimated travel times.
  - Filter schedules by **time, day, and train type**.

- **🛠️ Infrastructure Simulation**  
  - Simulate real-time train movements.
  - Notify users when trains arrive at selected stations (**Observer Pattern**).

---

## 🏗️ Design Patterns Used

| 🏷️ Pattern | 🎯 Purpose |
|------------|-----------|
| **Strategy** | Implements dynamic pricing strategies for ticket purchases, allowing flexibility based on the method of purchase (online, at station, in train) and travel conditions. |
| **State** | Manages the state transitions of railway segments, ensuring smooth transitions between operational, maintenance, closed, and testing states while preventing invalid transitions. |
| **Command** | Encapsulates ticket purchase operations, allowing users to undo or rollback their ticket purchases efficiently. |
| **Memento** | Saves and restores previous ticket purchase states, enabling users to undo actions when necessary. |
| **Chain of Responsibility** | Ensures structured and efficient processing of railway commands by delegating responsibilities across multiple handlers. |
| **Flyweight** | Optimizes memory usage by sharing common attributes among train objects with similar properties. |
| **Composite** | Structures train schedules and stops hierarchically, facilitating efficient traversal and modifications. |
| **Visitor** | Separates analytical operations on train schedules, ensuring that new analysis functionalities can be added without modifying the core structure. |
| **Observer** | Enables real-time user notifications for train arrivals, enhancing the system’s responsiveness. |
| **Mediator** | Centralizes and manages notifications related to train arrivals, preventing direct dependencies between components. |
| **Factory Method** | Ensures the flexible creation of train instances, improving object instantiation consistency. |
| **Bridge** | Decouples the logic for calculating optimal train routes from train details, enhancing maintainability. |
| **Singleton** | Guarantees that key components exist as a single instance, centralizing system control. |
| **Prototype** | Allows efficient cloning of train configurations, reducing the need for repetitive instantiation. |

---

## 📂 Setup & Execution
### 1️⃣ **Installation**
Make sure you have **Java & Maven** installed. Then, build ans run the project:

```bash
mvn clean install
```

### 2️⃣ **Execution**
After building the project, run the application using the following command:

```bash
java -jar ./target/dskrlac20_zadaca_3.jar \
--zs DZ_3_stanice.csv --zps DZ_3_vozila.csv --zk DZ_3_kompozicije.csv \
--zvr DZ_3_vozni_red.csv --zod DZ_3_oznake_dana.csv
