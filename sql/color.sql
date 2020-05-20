-- Type: color

-- DROP TYPE public.color;

CREATE TYPE public.color AS ENUM
    ('RED', 'BLACK', 'ORANGE');

ALTER TYPE public.color
    OWNER TO postgres;