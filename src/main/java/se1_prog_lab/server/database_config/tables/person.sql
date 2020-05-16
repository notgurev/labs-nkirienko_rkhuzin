-- Table: public.person

-- DROP TABLE public.person;

CREATE TABLE public.person
(
    person_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    person_name character varying(1000) COLLATE pg_catalog."default" NOT NULL,
    height real NOT NULL,
    passportid text COLLATE pg_catalog."default" NOT NULL,
    hair_color color,
    locationx integer NOT NULL,
    locationy real NOT NULL,
    locationz integer NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (person_id),
    CONSTRAINT person_person_name_check CHECK (person_name::text > ''::text),
    CONSTRAINT person_height_check CHECK (height > 0::double precision),
    CONSTRAINT person_passportid_check CHECK (char_length(passportid) > 8)
)

TABLESPACE pg_default;

ALTER TABLE public.person
    OWNER to postgres;