CREATE TABLE IF NOT EXISTS course (
    id bigint NOT NULL,
    platform character varying(60),
    author character varying(60),
    description character varying,
    visible boolean NOT NULL DEFAULT FALSE,
    subject_of_study character varying(120),
    link character varying,
    finish_date date,
    status smallint
);


CREATE TABLE IF NOT EXISTS course_profile (
    course_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    amount numeric DEFAULT 1 NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS course_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS feedback (
    id bigint NOT NULL,
    course_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    description text,
    rating integer,
    start_date timestamp without time zone,
    last_update timestamp without time zone
);


CREATE SEQUENCE IF NOT EXISTS feedback_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);

CREATE TABLE IF NOT EXISTS profile (
    id bigint NOT NULL,
    uid character varying(100) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    birth_day date,
    phone character varying(20),
    email character varying(100),
    country character varying(60),
    city character varying(100),
    objective text,
    summary text,
    info text,
    password character varying(255) NOT NULL,
    completed boolean NOT NULL,
    created timestamp(0) without time zone DEFAULT now() NOT NULL,
    skype character varying(80),
    vkontakte character varying(255),
    facebook character varying(255),
    linkedin character varying(255),
    github character varying(255),
    stackoverflow character varying(255)
);

CREATE TABLE IF NOT EXISTS profile_restore (
    id bigint NOT NULL,
    token character varying(255) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;