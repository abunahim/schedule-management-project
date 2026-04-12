CREATE TABLE IF NOT EXISTS schedules (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    start_time  TIMESTAMP(6) NOT NULL,
    end_time    TIMESTAMP(6) NOT NULL,
    status      VARCHAR(255) NOT NULL CHECK (status IN ('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED')),
    created_at  TIMESTAMP(6) NOT NULL
);