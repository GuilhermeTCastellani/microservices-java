CREATE TABLE tb_address (
    id             SERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    label          VARCHAR(60),
    recipient_name VARCHAR(120),
    phone          VARCHAR(20),
    zip_code       VARCHAR(9),
    street         VARCHAR(150),
    number         VARCHAR(20),
    district       VARCHAR(80),
    city           VARCHAR(80),
    state          VARCHAR(2),
    complement     VARCHAR(120)
);
