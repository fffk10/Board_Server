INSERT INTO users(name, email, password, role)
SELECT * FROM (
    SELECT
    'admin',
    'test@example.com',
    '$2a$10$2ztOZPIWw1XVPpMnniPCienSggo9rgKTvLVdaRK4.uDEcjE05CRZ.',
    0
) AS tbl
WHERE NOT EXISTS (SELECT 1 FROM users);