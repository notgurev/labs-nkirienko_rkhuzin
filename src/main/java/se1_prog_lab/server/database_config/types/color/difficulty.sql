-- Type: difficulty

-- DROP TYPE public.difficulty;

CREATE TYPE public.difficulty AS ENUM
    ('EASY', 'HARD', 'IMPOSSIBLE');

ALTER TYPE public.difficulty
    OWNER TO postgres;
