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

| 🏷️ Pattern | 🛠️ Implementation | 🎯 Purpose |
|------------|------------------|-----------|
| **Strategy** | `ICijenaStrategy`, `WebMobilnaCijenaStrategy`, `BlagajnaCijenaStrategy`, `KupovinaUVlakuCijenaStrategy`, `CjenikContext` | Allows dynamic pricing strategies for ticket purchases based on method (online, at station, in train) and travel conditions. |
| **State** | `StatusPrugeState`, `IspravnaState`, `UKvaruState`, `ZatvorenaState`, `UTestiranjuState`, `SegmentPruge` | Manages dynamic state transitions of railway segments (operational, under maintenance, closed). Prevents invalid transitions. |
| **Command** | `KarteCommand`, `KupovinaKartiCommand`, `KarteReceiver`, `KupovinaKartiInvoker` | Encapsulates ticket purchase operations, enabling **undo functionality** for cancellations. |
| **Memento** | `KupovinaKarteOriginator`, `KupovinaKarteMemento`, `KupovinaKarteCaretaker` | Saves and restores previous ticket purchases, enabling rollback if needed. |
| **Chain of Responsibility** | `KKPV2SUpravitelj`, `UNDOUpravitelj`, `PSP2SUpravitelj`, `IRPSUpravitelj`, `CVPUpravitelj`, `IKKPVUpravitelj`, `UKP2SUpravitelj` | Handles processing of railway commands, ensuring proper delegation of responsibilities. |
| **Flyweight** | Applied for train objects with similar properties | Optimizes memory usage by reusing shared train characteristics. |
| **Composite** | Used for train schedules and stops | Organizes schedules hierarchically, making traversal and updates efficient. |
| **Visitor** | Applied for analyzing train schedules | Decouples operations performed on schedules from their structure. |
| **Observer** | Implemented for train notifications | Notifies users about train arrivals in real time. |
| **Mediator** | Centralized notification management | Manages user notifications for train arrivals efficiently. |
| **Factory Method** | Creates train instances dynamically | Encapsulates object creation logic, ensuring flexibility. |
| **Bridge** | Applied for shortest path calculations | Separates logic for optimal route selection from train details. |
| **Singleton** | Ensures only one instance of key components | Centralizes control over critical components. |
| **Prototype** | Used for object cloning | Enables efficient duplication of train configurations. |

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
