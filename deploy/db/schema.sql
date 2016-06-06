--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: currency; Type: TABLE; Schema: public; Owner: currencyconverter
--

CREATE TABLE currency (
    id bigint NOT NULL,
    version bigint NOT NULL,
    code character varying(3) NOT NULL,
    name character varying(64) NOT NULL
);


ALTER TABLE currency OWNER TO currencyconverter;

--
-- Name: currency_id_seq; Type: SEQUENCE; Schema: public; Owner: currencyconverter
--

CREATE SEQUENCE currency_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE currency_id_seq OWNER TO currencyconverter;

--
-- Name: currency_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: currencyconverter
--

ALTER SEQUENCE currency_id_seq OWNED BY currency.id;


--
-- Name: rate; Type: TABLE; Schema: public; Owner: currencyconverter
--

CREATE TABLE rate (
    id bigint NOT NULL,
    version bigint NOT NULL,
    lastupdatets timestamp without time zone NOT NULL,
    ratevalue double precision NOT NULL,
    basecurrency_id bigint NOT NULL,
    ratedcurrency_id bigint NOT NULL,
    CONSTRAINT rate_ratevalue_check CHECK ((ratevalue >= (0)::double precision))
);


ALTER TABLE rate OWNER TO currencyconverter;

--
-- Name: rate_id_seq; Type: SEQUENCE; Schema: public; Owner: currencyconverter
--

CREATE SEQUENCE rate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rate_id_seq OWNER TO currencyconverter;

--
-- Name: rate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: currencyconverter
--

ALTER SEQUENCE rate_id_seq OWNED BY rate.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY currency ALTER COLUMN id SET DEFAULT nextval('currency_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY rate ALTER COLUMN id SET DEFAULT nextval('rate_id_seq'::regclass);


--
-- Name: currency_id_seq; Type: SEQUENCE SET; Schema: public; Owner: currencyconverter
--

SELECT pg_catalog.setval('currency_id_seq', 1, true);

--
-- Name: rate_id_seq; Type: SEQUENCE SET; Schema: public; Owner: currencyconverter
--

SELECT pg_catalog.setval('rate_id_seq', 1, true);


--
-- Name: currency__code_uidx; Type: CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY currency
    ADD CONSTRAINT currency__code_uidx UNIQUE (code);


--
-- Name: currency_pkey; Type: CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY currency
    ADD CONSTRAINT currency_pkey PRIMARY KEY (id);


--
-- Name: rate__base_currency__rated_currency_uidx; Type: CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY rate
    ADD CONSTRAINT rate__base_currency__rated_currency_uidx UNIQUE (basecurrency_id, ratedcurrency_id);


--
-- Name: rate_pkey; Type: CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY rate
    ADD CONSTRAINT rate_pkey PRIMARY KEY (id);


--
-- Name: currency__name_idx; Type: INDEX; Schema: public; Owner: currencyconverter
--

CREATE INDEX currency__name_idx ON currency USING btree (name);


--
-- Name: fkgft33oojw8vq9nfikafqwv8i9; Type: FK CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY rate
    ADD CONSTRAINT fkgft33oojw8vq9nfikafqwv8i9 FOREIGN KEY (ratedcurrency_id) REFERENCES currency(id);


--
-- Name: fkjbccixcn2g3x02ixhbr1lfecb; Type: FK CONSTRAINT; Schema: public; Owner: currencyconverter
--

ALTER TABLE ONLY rate
    ADD CONSTRAINT fkjbccixcn2g3x02ixhbr1lfecb FOREIGN KEY (basecurrency_id) REFERENCES currency(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: miguelgl
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM miguelgl;
GRANT ALL ON SCHEMA public TO miguelgl;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

