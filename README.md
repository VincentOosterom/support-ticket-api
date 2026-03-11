# Support Ticket Systeem API

Een production-ready REST API voor het beheren van support tickets met op rollen gebaseerde toegangscontrole, commentaarthreads en uitgebreide audit logging. Gebouwd met Spring Boot 3, MySQL en JPA.



### Kernfunctionaliteit
- Support tickets aanmaken, lezen en updaten
- Real-time statusbeheer (OPEN → IN_PROGRESS → RESOLVED → CLOSED)
- Prioriteitsniveaus (LOW, MEDIUM, HIGH, CRITICAL)
- Commentaarthreads met gebruikerstracking
- Ondersteuning voor bestandsbijlagen
- Volledige audittrail (TicketHistory)

### Beveiliging & Toegangscontrole
- Op rollen gebaseerde toegangscontrole (RBAC)
    - **CUSTOMER**: Kan tickets aanmaken, commentaar plaatsen op eigen tickets
    - **AGENT**: Kan toegewezen tickets beheren, status bijwerken
    - **ADMIN**: Volledige systeemtoegang
- Autorisatiecontroles op alle endpoints
- Input validatie (Spring Validation)
- Foutafhandeling met betekenisvolle berichten

### Architectuur
-  Gelaagde architectuur (Controller → DTO/Mapper → Service → Repository → Entity)
- Scheiding van verantwoordelijkheden
- Type-veilige Enums voor status en prioriteit
- DTOs voor schone API responses/request
- Mappers voor Entity ↔ DTO conversie
- JPA/Hibernate voor databaseabstractie

### Testen
- Unit tests voor Services
- Integratietests voor Controllers
- Repository tests met H2 in-memory database
- Mockito voor afhankelijkheid mocking

## 🛠️ Tech Stack

| Technologie         | Versie | Doel |
|---------------------|--------|------|
| **Java**            | 21     | Programmeertaal |
| **Spring Boot**     | 4.0.3| Framework |
| **Spring Data JPA** | -      | ORM |
| **POSTgreSQL**      | 8.0+   | Database |
| **Hibernate**       | -      | Databasemapper |
| **JUnit 5**         | -      | Testen |
| **Mockito**         | -      | Mocking |
| **Maven**           | -      | Build tool |