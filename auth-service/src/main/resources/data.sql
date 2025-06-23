-- Password Comprador: password
-- Password Vendedor: password

INSERT INTO USERS (username, email, password, role, type)
    VALUES ('comprador', 'comprador@email.com', '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy', 'USER', 1),
           ('vendedor', 'vendedor@email.com', '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy', 'USER', 2);