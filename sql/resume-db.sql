--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.16
-- Dumped by pg_dump version 9.6.16

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

--COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: certificate; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.certificate (
    id bigint NOT NULL,
    id_profile bigint NOT NULL,
    name character varying(50) NOT NULL,
    large_url character varying(255) NOT NULL,
    small_url character varying(255) NOT NULL
);


ALTER TABLE public.certificate OWNER TO resume;

--
-- Name: certificate_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.certificate_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.certificate_seq OWNER TO resume;

--
-- Name: course; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.course (
    id bigint NOT NULL,
    name character varying(60) NOT NULL,
    school character varying(60) NOT NULL,
    finish_date date
);


ALTER TABLE public.course OWNER TO resume;

--
-- Name: course_profile; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.course_profile (
    course_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    amount numeric DEFAULT 1 NOT NULL
);


ALTER TABLE public.course_profile OWNER TO resume;

--
-- Name: course_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.course_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.course_seq OWNER TO resume;

--
-- Name: education; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.education (
    id bigint NOT NULL,
    id_profile bigint NOT NULL,
    summary character varying(100) NOT NULL,
    begin_year integer NOT NULL,
    finish_year integer,
    university text NOT NULL,
    faculty character varying(255) NOT NULL
);


ALTER TABLE public.education OWNER TO resume;

--
-- Name: education_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.education_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.education_seq OWNER TO resume;

--
-- Name: feedback; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.feedback (
    id bigint NOT NULL,
    course_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    description text,
    rating integer,
    start_date timestamp without time zone,
    last_update timestamp without time zone
);


ALTER TABLE public.feedback OWNER TO resume;

--
-- Name: feedback_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.feedback_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.feedback_seq OWNER TO resume;

--
-- Name: hobby; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.hobby (
    id bigint NOT NULL,
    id_profile bigint NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE public.hobby OWNER TO resume;

--
-- Name: hobby_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.hobby_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.hobby_seq OWNER TO resume;

--
-- Name: language_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.language_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.language_seq OWNER TO resume;

--
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);


ALTER TABLE public.persistent_logins OWNER TO resume;

--
-- Name: practic_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.practic_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.practic_seq OWNER TO resume;

--
-- Name: profile; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.profile (
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
    large_photo character varying(255),
    small_photo character varying(255),
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


ALTER TABLE public.profile OWNER TO resume;

--
-- Name: profile_restore; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.profile_restore (
    id bigint NOT NULL,
    token character varying(255) NOT NULL
);


ALTER TABLE public.profile_restore OWNER TO resume;

--
-- Name: profile_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.profile_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.profile_seq OWNER TO resume;

--
-- Name: skill; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.skill (
    id bigint NOT NULL,
    id_profile bigint NOT NULL,
    category character varying(50) NOT NULL,
    value text NOT NULL
);


ALTER TABLE public.skill OWNER TO resume;

--
-- Name: skill_category; Type: TABLE; Schema: public; Owner: resume
--

CREATE TABLE public.skill_category (
    id bigint NOT NULL,
    category character varying(50) NOT NULL
);


ALTER TABLE public.skill_category OWNER TO resume;

--
-- Name: skill_seq; Type: SEQUENCE; Schema: public; Owner: resume
--

CREATE SEQUENCE public.skill_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE public.skill_seq OWNER TO resume;

--
-- Data for Name: certificate; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.certificate (id, id_profile, name, large_url, small_url) FROM stdin;
\.


--
-- Name: certificate_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.certificate_seq', 1, false);


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.course (id, name, school, finish_date) FROM stdin;
\.


--
-- Data for Name: course_profile; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.course_profile (course_id, profile_id, amount) FROM stdin;
\.


--
-- Name: course_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.course_seq', 1, false);


--
-- Data for Name: education; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.education (id, id_profile, summary, begin_year, finish_year, university, faculty) FROM stdin;
\.


--
-- Name: education_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.education_seq', 1, false);


--
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.feedback (id, course_id, profile_id, description, rating, start_date, last_update) FROM stdin;
\.


--
-- Name: feedback_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.feedback_seq', 1, false);


--
-- Data for Name: hobby; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.hobby (id, id_profile, name) FROM stdin;
\.


--
-- Name: hobby_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.hobby_seq', 1, false);


--
-- Name: language_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.language_seq', 1, false);


--
-- Data for Name: persistent_logins; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.persistent_logins (username, series, token, last_used) FROM stdin;
\.


--
-- Name: practic_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.practic_seq', 1, false);


--
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.profile (id, uid, first_name, last_name, birth_day, phone, email, country, city, objective, summary, large_photo, small_photo, info, password, completed, created, skype, vkontakte, facebook, linkedin, github, stackoverflow) FROM stdin;
\.


--
-- Data for Name: profile_restore; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.profile_restore (id, token) FROM stdin;
\.


--
-- Name: profile_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.profile_seq', 1, false);


--
-- Data for Name: skill; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.skill (id, id_profile, category, value) FROM stdin;
\.


--
-- Data for Name: skill_category; Type: TABLE DATA; Schema: public; Owner: resume
--

COPY public.skill_category (id, category) FROM stdin;
\.


--
-- Name: skill_seq; Type: SEQUENCE SET; Schema: public; Owner: resume
--

SELECT pg_catalog.setval('public.skill_seq', 1, false);


--
-- PostgreSQL database dump complete
--

