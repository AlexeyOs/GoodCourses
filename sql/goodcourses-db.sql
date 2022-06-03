--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1
-- Dumped by pg_dump version 11.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: course; Type: TABLE; Schema: public; Owner: goodcourses
--

CREATE TABLE public.course (
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


ALTER TABLE public.course OWNER TO goodcourses;

--
-- Name: course_profile; Type: TABLE; Schema: public; Owner: goodcourses
--

CREATE TABLE public.course_profile (
    course_id bigint NOT NULL,
    profile_id bigint NOT NULL,
    amount numeric DEFAULT 1 NOT NULL
);


ALTER TABLE public.course_profile OWNER TO goodcourses;

--
-- Name: course_seq; Type: SEQUENCE; Schema: public; Owner: goodcourses
--

CREATE SEQUENCE public.course_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_seq OWNER TO goodcourses;

--
-- Name: feedback; Type: TABLE; Schema: public; Owner: goodcourses
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


ALTER TABLE public.feedback OWNER TO goodcourses;

--
-- Name: feedback_seq; Type: SEQUENCE; Schema: public; Owner: goodcourses
--

CREATE SEQUENCE public.feedback_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.feedback_seq OWNER TO goodcourses;

--
-- Name: persistent_logins; Type: TABLE; Schema: public; Owner: goodcourses
--

CREATE TABLE public.persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);


ALTER TABLE public.persistent_logins OWNER TO goodcourses;

--
-- Name: profile; Type: TABLE; Schema: public; Owner: goodcourses
--

CREATE TABLE public.profile (
    id bigserial NOT NULL,
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


ALTER TABLE public.profile OWNER TO goodcourses;

--
-- Name: profile_restore; Type: TABLE; Schema: public; Owner: goodcourses
--

CREATE TABLE public.profile_restore (
    id bigint NOT NULL,
    token character varying(255) NOT NULL
);


ALTER TABLE public.profile_restore OWNER TO goodcourses;

--
-- Name: profile_seq; Type: SEQUENCE; Schema: public; Owner: goodcourses
--

CREATE SEQUENCE public.profile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.profile_seq OWNER TO goodcourses;


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.course (id, platform, author, description, visible, subject_of_study, link, finish_date, status) FROM stdin;
\.


--
-- Data for Name: course_profile; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.course_profile (course_id, profile_id, amount) FROM stdin;
\.


--
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.feedback (id, course_id, profile_id, description, rating, start_date, last_update) FROM stdin;
\.


--
-- Data for Name: persistent_logins; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.persistent_logins (username, series, token, last_used) FROM stdin;
\.


--
-- Data for Name: profile; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.profile (id, uid, first_name, last_name, birth_day, phone, email, country, city, objective, summary, info, password, completed, created, skype, vkontakte, facebook, linkedin, github, stackoverflow) FROM stdin;
\.


--
-- Data for Name: profile_restore; Type: TABLE DATA; Schema: public; Owner: goodcourses
--

COPY public.profile_restore (id, token) FROM stdin;
\.


--
-- Name: course_seq; Type: SEQUENCE SET; Schema: public; Owner: goodcourses
--

SELECT pg_catalog.setval('public.course_seq', 1, false);


--
-- Name: feedback_seq; Type: SEQUENCE SET; Schema: public; Owner: goodcourses
--

SELECT pg_catalog.setval('public.feedback_seq', 1, false);


--
-- Name: profile_seq; Type: SEQUENCE SET; Schema: public; Owner: goodcourses
--

SELECT pg_catalog.setval('public.profile_seq', 1, false);


--
-- PostgreSQL database dump complete
--

