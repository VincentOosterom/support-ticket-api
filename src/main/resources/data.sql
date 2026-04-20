-- Users
INSERT INTO users (name, email, keycloak_id)
VALUES ('Test Admin', 'admin@test.nl', '07341f79-00d2-48e1-a340-f5952df9eec3')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, keycloak_id)
VALUES ('Test Agent', 'agent@test.nl', 'aa2cff82-fdc3-4d7e-8623-197692c1fc6b')
    ON CONFLICT (email) DO NOTHING;

INSERT INTO users (name, email, keycloak_id)
VALUES ('Test Customer', 'customer@test.nl', 'fddc0ba4-d1dd-4696-8bf9-bcf34ce2f491')
    ON CONFLICT (email) DO NOTHING;

-- Tickets (user_id = 3 = customer, want nu begint het bij 1)
INSERT INTO tickets (subject, description, status, priority, created_at, user_id)
VALUES ('Internet werkt niet', 'Al 2 dagen geen internet', 'IN_PROGRESS', 'HIGH', NOW(), 3);

INSERT INTO tickets (subject, description, status, priority, created_at, user_id)
VALUES ('Laptop scherm flikkert', 'Scherm flikkert elke 5 minuten', 'OPEN', 'MEDIUM', NOW(), 3);

INSERT INTO tickets (subject, description, status, priority, created_at, closed_at, user_id)
VALUES ('Printer doet het niet', 'Printer reageert niet', 'CLOSED', 'LOW', NOW(), NOW(), 3);

-- Comments (user_id = 2 = agent)
INSERT INTO comments (message, created_at, ticket_id, user_id)
VALUES ('We kijken ernaar!', NOW(), 1, 2);

-- Ticket assignments (agent_id = 2)
INSERT INTO ticket_assignments (ticket_id, agent_id)
VALUES (1, 2);