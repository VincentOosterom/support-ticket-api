# Support Ticket Systeem API

Een production-ready REST API voor het beheren van support tickets met op rollen
gebaseerde toegangscontrole, commentaarthreads en uitgebreide audit logging.
Gebouwd met Spring Boot 3, PostgreSQL en JPA.

Ik heb een flink gevulde data.sql bestaat aangemaakt. De database wordt dus al gevuld bij het opstarten :).

## Inhoudsopgave

1. Inleiding
2. Kernfunctionaliteit
2. Projectstructuur
3. Benodigde technieken
4. Project lokaal opzetten
5. Configuratie
6. Applicatie starten
7. Tests uitvoeren
8. Standaard gebruikers en autorisatie
9. API Endpoints

---

## Inleiding

De Support Ticket Systeem API stelt gebruikers in staat om support tickets aan te
maken en te beheren. Medewerkers (agents) kunnen tickets oppakken en de status
bijwerken. Beheerders hebben volledige toegang tot het systeem.

### Kernfunctionaliteit

- Support tickets aanmaken, lezen en updaten
- Statusbeheer: OPEN naar IN_PROGRESS naar RESOLVED naar CLOSED
- Prioriteitsniveaus: LOW, MEDIUM, HIGH, CRITICAL
- Commentaarthreads met gebruikerstracking
- Ondersteuning voor bestandsbijlagen
- Volledige audittrail via TicketHistory
- Op rollen gebaseerde toegangscontrole (RBAC) via Keycloak

---

## Projectstructuur

src/
└── main/
└── java/
└── nl/ticketsystem/
├── config/ # Configuratieklassen (bijv. Keycloak/Security)
├── controller/ # REST endpoints
├── dto/ # Request- en response-objecten
├── exception/ # Foutafhandeling en custom exceptions
├── helpers/ # Hulpklassen en utilityfuncties
├── mapper/ # Conversie tussen Entity en DTO
├── model/ # JPA Entities en Enums
├── repository/ # Database-toegang via JPA
├── service/ # Bedrijfslogica
└── SupportTicketApiApplication.java # Startpunt van de applicatie
└── resources/
├── application.properties # Configuratiebestand
└── data.sql

## Benodigde technieken

| Technologie | Versie  | Doel                         |
|-------------|---------|------------------------------|
| Java        | 21      | Programmeertaal              |
| Maven       | 3.x     | Build tool                   |
| PostgreSQL  | 15+     | Database                     |
| Keycloak    | 22+     | Authenticatie en autorisatie |
| Postman     | Nieuwst | API testen                   |

### Frameworks (automatisch via Maven)

| Framework         | Doel                     |
|-------------------|--------------------------|
| Spring Boot 4.0.3 | Applicatieframework      |
| Spring Data JPA   | Databaseabstractie (ORM) |
| Hibernate         | Databasemapper           |
| Spring Validation | Inputvalidatie           |
| JUnit 5           | Testen                   |
| Mockito           | Mocking in unit tests    |

---

## Project lokaal opzetten

### Stap 1 — Repository klonen

git clone https://github.com/VincentOosterom/support-ticket-api
cd support-ticket-systeem

### Stap 2 — PostgreSQL database aanmaken

CREATE DATABASE ticketsysteem;
CREATE USER ticketuser WITH PASSWORD 'jouwwachtwoord';
GRANT ALL PRIVILEGES ON DATABASE ticketsysteem TO ticketuser;

### Stap 3 — Keycloak opzetten

1. Download Keycloak via https://www.keycloak.org/downloads
2. Start Keycloak: ./kc.sh start-dev --http-port=9090 (macbook)
3. Ga naar http://localhost:9090 en log in
4. Maak een Realm aan: support-ticket-api
5. Maak een Client aan: support-ticket-api
6. Maak Client Roles aan: ADMIN, AGENT, CUSTOMER
7. Maak testgebruikers aan (zie Standaard gebruikers)

### Stap 4 — Project bouwen

mvn clean install

---

## Configuratie

Maak een .env bestand aan in de root van het project:

DB_URL=jdbc:postgresql://localhost:5432/jouw-database
DB_USERNAME=jouw-username
DB_PASSWORD=jouw-wachtwoord
CLIENT_SECRET=jouw-keycloak-secret
CLIENT_ID=support-ticket-api

Let op: voeg application.properties toe aan .gitignore zodat wachtwoorden
niet in je repository terechtkomen.

---

## Applicatie starten

mvn spring-boot:run

De API is bereikbaar op: http://localhost:8080

---

## Tests uitvoeren

| Testsoort       | Doel                         | Tool    |
|-----------------|------------------------------|---------|
| Unit tests      | Testen van de servicelaag    | JUnit 5 |
| Integratietests | Testen van de controllerlaag | JUnit 5 |

Alle tests: mvn test
Specifieke test: mvn test -Dtest=TicketServiceTest

---

## Standaard gebruikers en autorisatie

| Gebruikersnaam | Wachtwoord | Rol      |
|----------------|------------|----------|
| testadmin      | ****       | ADMIN    |
| agent          | ****       | AGENT    |
| customer       | ****       | CUSTOMER |

Stel zelf wachtwoorden in via Keycloak admin panel

### CUSTOMER

- Eigen tickets aanmaken en bekijken
- Commentaar plaatsen op eigen tickets

### AGENT

- Alle tickets bekijken en bijwerken
- Status van tickets wijzigen
- Commentaar plaatsen op alle tickets

### ADMIN

- Volledige toegang tot alle endpoints
- Gebruikers en tickets beheren

## API Endpoints

### Users

| Methode | Endpoint    | Rol      | Omschrijving                         |
|---------|-------------|----------|--------------------------------------|
| POST    | /users/sync | Ingelogd | Synchroniseer gebruiker met Keycloak |
| GET     | /users/me   | Ingelogd | Haal eigen gebruikersgegevens op     |
| GET     | /users/**   | ADMIN    | Haal gebruikersgegevens op           |
| DELETE  | /users/**   | ADMIN    | Verwijder een gebruiker              |

### Tickets

| Methode | Endpoint             | Rol          | Omschrijving                       |
|---------|----------------------|--------------|------------------------------------|
| POST    | /tickets             | CUSTOMER     | Maak een nieuw ticket aan          |
| GET     | /tickets             | Ingelogd     | Haal alle toegankelijke tickets op |
| GET     | /tickets/stats       | ADMIN        | Haal statistieken op               |
| GET     | /tickets/priority/** | AGENT, ADMIN | Haal tickets op per prioriteit     |
| GET     | /tickets/users/**    | AGENT, ADMIN | Haal tickets op per gebruiker      |
| GET     | /tickets/**          | AGENT, ADMIN | Haal een specifiek ticket op       |
| PUT     | /tickets/**          | AGENT, ADMIN | Werk een ticket bij                |
| DELETE  | /tickets/**          | ADMIN        | Verwijder een ticket               |

### Comments

| Methode | Endpoint               | Rol      | Omschrijving                     |
|---------|------------------------|----------|----------------------------------|
| POST    | /tickets/*/comments    | Ingelogd | Plaats een comment op een ticket |
| DELETE  | /tickets/*/comments/** | ADMIN    | Verwijder een comment            |

### Attachments

| Methode | Endpoint        | Rol             | Omschrijving          |
|---------|-----------------|-----------------|-----------------------|
| POST    | /attachments/** | CUSTOMER, AGENT | Upload een bijlage    |
| GET     | /attachments/** | Ingelogd        | Haal een bijlage op   |
| DELETE  | /attachments/** | ADMIN           | Verwijder een bijlage |

### Ticket Assignments

| Methode | Endpoint               | Rol          | Omschrijving                      |
|---------|------------------------|--------------|-----------------------------------|
| POST    | /ticket-assignments/** | ADMIN, AGENT | Wijs een ticket toe aan een agent |
| GET     | /ticket-assignments/** | ADMIN, AGENT | Haal toewijzingen op              |
| PUT     | /ticket-assignments/** | ADMIN        | Werk een toewijzing bij           |
| DELETE  | /ticket-assignments/** | ADMIN        | Verwijder een toewijzing          |

### Ticket History

| Methode | Endpoint           | Rol      | Omschrijving                           |
|---------|--------------------|----------|----------------------------------------|
| GET     | /ticket-history/** | Ingelogd | Haal de geschiedenis van een ticket op |