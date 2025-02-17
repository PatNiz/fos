BEGIN;

CREATE TABLE IF NOT EXISTS public.administrator
(
    id serial NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT administrator_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.dish
(
    id bigserial NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    price double precision NOT NULL,
    cuisine character varying(255) COLLATE pg_catalog."default" NOT NULL,
    recipe_creator_id bigint NOT NULL,
    CONSTRAINT dish_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.drink
(
    id bigserial NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    price double precision NOT NULL,
    CONSTRAINT drink_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.fosuser
(
    id bigserial NOT NULL,
    username character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT fosuser_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.order_dishes
(
    id bigserial NOT NULL,
    order_id bigint NOT NULL,
    dish_id integer NOT NULL,
    CONSTRAINT order_dishes_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.order_drinks
(
    id bigserial NOT NULL,
    order_id bigint NOT NULL,
    drink_id integer NOT NULL,
    CONSTRAINT order_drinks_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.restaurant_order
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    order_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.user_roles
(
    id serial NOT NULL,
    user_id bigint NOT NULL,
    role character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.administrator
    ADD CONSTRAINT fk_user_administrator FOREIGN KEY (user_id)
    REFERENCES public.fosuser (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS public.dish
    ADD CONSTRAINT fk_creator_dish FOREIGN KEY (recipe_creator_id)
    REFERENCES public.fosuser (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

ALTER TABLE IF EXISTS public.order_dishes
    ADD CONSTRAINT order_dishes_dish_id_fkey FOREIGN KEY (dish_id)
    REFERENCES public.dish (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.order_dishes
    ADD CONSTRAINT order_dishes_order_id_fkey FOREIGN KEY (order_id)
    REFERENCES public.restaurant_order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.order_drinks
    ADD CONSTRAINT order_drinks_drink_id_fkey FOREIGN KEY (drink_id)
    REFERENCES public.drink (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.order_drinks
    ADD CONSTRAINT order_drinks_order_id_fkey FOREIGN KEY (order_id)
    REFERENCES public.restaurant_order (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.restaurant_order
    ADD CONSTRAINT fk_restaurant_order_user FOREIGN KEY (user_id)
    REFERENCES public.fosuser (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;

ALTER TABLE IF EXISTS public.user_roles
    ADD CONSTRAINT fk_user_user_roles FOREIGN KEY (user_id)
    REFERENCES public.fosuser (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

INSERT INTO public.fosuser (id, username) VALUES
    (1, 'Client'),
    (2, 'Administrator');

INSERT INTO public.user_roles (user_id, role) VALUES
    (1, 'CLIENT'),
    (2, 'ADMINISTRATOR');

INSERT INTO public.administrator (user_id) VALUES
    (2);

INSERT INTO public.dish (name, price, cuisine, recipe_creator_id) VALUES
    ( 'Spaghetti Bolognese', 12.99, 'Italian', 2),
    ( 'Tacos', 10.99, 'Mexican', 2),
    ( 'Pierogi', 9.99, 'Polish', 2),
    ( 'Lasagna', 14.99, 'Italian', 2),
    ( 'Burrito', 11.99, 'Mexican', 2),
    ( 'Bigos', 8.99, 'Polish', 2);

INSERT INTO public.drink (name, price) VALUES
    ( 'Coca Cola', 2.50),
    ( 'Orange Juice', 3.00),
    ( 'Espresso', 2.00),
    ( 'Mojito', 7.50),
    ( 'Wyborowa', 5.50),
    ( 'Żubrówka', 5.50);

END;
