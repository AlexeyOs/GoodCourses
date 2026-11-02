CREATE SEQUENCE profile_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE course_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE feedback_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE skill_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE certificate_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE profile (
    id BIGINT PRIMARY KEY DEFAULT nextval('profile_seq'),
    uid VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    birth_day DATE,
    phone VARCHAR(20),
    email VARCHAR(100),
    country VARCHAR(60),
    city VARCHAR(100),
    objective TEXT,
    summary TEXT,
    large_photo VARCHAR(255),
    small_photo VARCHAR(255),
    info TEXT,
    password VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL,
    created TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    skype VARCHAR(80),
    vkontakte VARCHAR(255),
    facebook VARCHAR(255),
    linkedin VARCHAR(255),
    github VARCHAR(255),
    stackoverflow VARCHAR(255)
);

CREATE TABLE course (
    id BIGINT PRIMARY KEY DEFAULT nextval('course_seq'),
    platform VARCHAR(60),
    author VARCHAR(60),
    description TEXT,
    visible BOOLEAN NOT NULL DEFAULT FALSE,
    subject_of_study VARCHAR(120),
    link TEXT,
    finish_date DATE,
    status SMALLINT
);

CREATE TABLE course_profile (
    course_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    amount NUMERIC NOT NULL DEFAULT 1,
    PRIMARY KEY (course_id, profile_id),
    CONSTRAINT fk_course_profile_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_course_profile_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

CREATE TABLE feedback (
    id BIGINT PRIMARY KEY DEFAULT nextval('feedback_seq'),
    course_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    rating INTEGER,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_update TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT uq_feedback_course_profile UNIQUE (course_id, profile_id),
    CONSTRAINT chk_feedback_rating CHECK (rating IS NULL OR rating BETWEEN 1 AND 5),
    CONSTRAINT fk_feedback_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_feedback_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

CREATE TABLE skill (
    id BIGINT PRIMARY KEY DEFAULT nextval('skill_seq'),
    id_profile BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    value TEXT NOT NULL,
    CONSTRAINT fk_skill_profile FOREIGN KEY (id_profile) REFERENCES profile(id) ON DELETE CASCADE
);

CREATE TABLE skill_category (
    id BIGINT PRIMARY KEY,
    category VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO skill_category (id, category) VALUES
    (1, 'Languages'),
    (2, 'DBMS'),
    (3, 'Web'),
    (4, 'Java'),
    (5, 'IDE'),
    (6, 'CVS'),
    (7, 'Web Servers'),
    (8, 'Build system'),
    (9, 'Cloud');

CREATE TABLE profile_restore (
    id BIGINT PRIMARY KEY,
    token VARCHAR(100) NOT NULL UNIQUE,
    CONSTRAINT fk_profile_restore_profile FOREIGN KEY (id) REFERENCES profile(id) ON DELETE CASCADE
);

CREATE TABLE persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE certificate (
    id BIGINT PRIMARY KEY DEFAULT nextval('certificate_seq'),
    id_profile BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    large_url VARCHAR(255) NOT NULL,
    small_url VARCHAR(255) NOT NULL,
    CONSTRAINT fk_certificate_profile FOREIGN KEY (id_profile) REFERENCES profile(id) ON DELETE CASCADE
);
