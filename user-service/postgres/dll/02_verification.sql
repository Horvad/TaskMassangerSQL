CREATE TABLE IF NOT EXISTS user_service.verification
(
    mail text NOT NULL,
    code text NOT NULL,
    PRIMARY KEY (mail)
);
