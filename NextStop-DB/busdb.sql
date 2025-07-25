PGDMP  #            	        }            busdb    17.4    17.4 $               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    16485    busdb    DATABASE     �   CREATE DATABASE busdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE busdb;
                     postgres    false            �            1259    16487    bus    TABLE       CREATE TABLE public.bus (
    id integer NOT NULL,
    bus_number character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    total_seats integer NOT NULL,
    operator_name character varying(255) NOT NULL,
    status character varying(255)
);
    DROP TABLE public.bus;
       public         heap r       postgres    false            �            1259    16486 
   bus_id_seq    SEQUENCE     �   CREATE SEQUENCE public.bus_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.bus_id_seq;
       public               postgres    false    218                       0    0 
   bus_id_seq    SEQUENCE OWNED BY     9   ALTER SEQUENCE public.bus_id_seq OWNED BY public.bus.id;
          public               postgres    false    217            �            1259    16505    bus_route_schedule    TABLE       CREATE TABLE public.bus_route_schedule (
    id integer NOT NULL,
    bus_id integer,
    route_id integer,
    departure_time timestamp without time zone NOT NULL,
    arrival_time timestamp without time zone,
    fare double precision NOT NULL,
    status character varying(255)
);
 &   DROP TABLE public.bus_route_schedule;
       public         heap r       postgres    false            �            1259    16504    bus_route_schedule_id_seq    SEQUENCE     �   CREATE SEQUENCE public.bus_route_schedule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.bus_route_schedule_id_seq;
       public               postgres    false    222                       0    0    bus_route_schedule_id_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.bus_route_schedule_id_seq OWNED BY public.bus_route_schedule.id;
          public               postgres    false    221            �            1259    16498    route    TABLE       CREATE TABLE public.route (
    id integer NOT NULL,
    source_city character varying(255) NOT NULL,
    destination_city character varying(255) NOT NULL,
    duration character varying(255),
    distance_km double precision,
    status character varying(255)
);
    DROP TABLE public.route;
       public         heap r       postgres    false            �            1259    16497    route_id_seq    SEQUENCE     �   CREATE SEQUENCE public.route_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.route_id_seq;
       public               postgres    false    220                       0    0    route_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.route_id_seq OWNED BY public.route.id;
          public               postgres    false    219            �            1259    16522    seat    TABLE     �   CREATE TABLE public.seat (
    id integer NOT NULL,
    seat_number character varying(255) NOT NULL,
    bus_id integer NOT NULL
);
    DROP TABLE public.seat;
       public         heap r       postgres    false            �            1259    16521    seat_id_seq    SEQUENCE     �   CREATE SEQUENCE public.seat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.seat_id_seq;
       public               postgres    false    224                       0    0    seat_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.seat_id_seq OWNED BY public.seat.id;
          public               postgres    false    223            f           2604    16490    bus id    DEFAULT     `   ALTER TABLE ONLY public.bus ALTER COLUMN id SET DEFAULT nextval('public.bus_id_seq'::regclass);
 5   ALTER TABLE public.bus ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    217    218    218            h           2604    16508    bus_route_schedule id    DEFAULT     ~   ALTER TABLE ONLY public.bus_route_schedule ALTER COLUMN id SET DEFAULT nextval('public.bus_route_schedule_id_seq'::regclass);
 D   ALTER TABLE public.bus_route_schedule ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    221    222    222            g           2604    16501    route id    DEFAULT     d   ALTER TABLE ONLY public.route ALTER COLUMN id SET DEFAULT nextval('public.route_id_seq'::regclass);
 7   ALTER TABLE public.route ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219    220            i           2604    16525    seat id    DEFAULT     b   ALTER TABLE ONLY public.seat ALTER COLUMN id SET DEFAULT nextval('public.seat_id_seq'::regclass);
 6   ALTER TABLE public.seat ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    224    223    224            	          0    16487    bus 
   TABLE DATA           W   COPY public.bus (id, bus_number, type, total_seats, operator_name, status) FROM stdin;
    public               postgres    false    218   �(                 0    16505    bus_route_schedule 
   TABLE DATA           n   COPY public.bus_route_schedule (id, bus_id, route_id, departure_time, arrival_time, fare, status) FROM stdin;
    public               postgres    false    222   �)                 0    16498    route 
   TABLE DATA           a   COPY public.route (id, source_city, destination_city, duration, distance_km, status) FROM stdin;
    public               postgres    false    220   �*                 0    16522    seat 
   TABLE DATA           7   COPY public.seat (id, seat_number, bus_id) FROM stdin;
    public               postgres    false    224   8+                  0    0 
   bus_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.bus_id_seq', 8, true);
          public               postgres    false    217                       0    0    bus_route_schedule_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.bus_route_schedule_id_seq', 8, true);
          public               postgres    false    221                       0    0    route_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.route_id_seq', 8, true);
          public               postgres    false    219                       0    0    seat_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.seat_id_seq', 26, true);
          public               postgres    false    223            k           2606    16656    bus bus_bus_number_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_bus_number_key UNIQUE (bus_number);
 @   ALTER TABLE ONLY public.bus DROP CONSTRAINT bus_bus_number_key;
       public                 postgres    false    218            m           2606    16494    bus bus_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.bus
    ADD CONSTRAINT bus_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.bus DROP CONSTRAINT bus_pkey;
       public                 postgres    false    218            q           2606    16510 *   bus_route_schedule bus_route_schedule_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.bus_route_schedule
    ADD CONSTRAINT bus_route_schedule_pkey PRIMARY KEY (id);
 T   ALTER TABLE ONLY public.bus_route_schedule DROP CONSTRAINT bus_route_schedule_pkey;
       public                 postgres    false    222            o           2606    16503    route route_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.route
    ADD CONSTRAINT route_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.route DROP CONSTRAINT route_pkey;
       public                 postgres    false    220            s           2606    16528    seat seat_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.seat DROP CONSTRAINT seat_pkey;
       public                 postgres    false    224            t           2606    16511 1   bus_route_schedule bus_route_schedule_bus_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.bus_route_schedule
    ADD CONSTRAINT bus_route_schedule_bus_id_fkey FOREIGN KEY (bus_id) REFERENCES public.bus(id);
 [   ALTER TABLE ONLY public.bus_route_schedule DROP CONSTRAINT bus_route_schedule_bus_id_fkey;
       public               postgres    false    218    222    4717            u           2606    16516 3   bus_route_schedule bus_route_schedule_route_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.bus_route_schedule
    ADD CONSTRAINT bus_route_schedule_route_id_fkey FOREIGN KEY (route_id) REFERENCES public.route(id);
 ]   ALTER TABLE ONLY public.bus_route_schedule DROP CONSTRAINT bus_route_schedule_route_id_fkey;
       public               postgres    false    4719    220    222            v           2606    16529    seat seat_bus_id_fkey    FK CONSTRAINT     q   ALTER TABLE ONLY public.seat
    ADD CONSTRAINT seat_bus_id_fkey FOREIGN KEY (bus_id) REFERENCES public.bus(id);
 ?   ALTER TABLE ONLY public.seat DROP CONSTRAINT seat_bus_id_fkey;
       public               postgres    false    218    4717    224            	   �   x�e�͊�0 ���)�J��c�]5�ղx�$H@ZI���kEeٝ��#PY�O�9e���X��T�]���;t�������\H�{%��BS��\�ѷo�a�����o�v1,efĸ�O���S�W�+�f�M=���fEV�1�]8��S+����G������gt�ѓ�������8S\��+fT��G�k}?L��a�M          �   x�]�;!Dk8 �͟�lE)�I��1��'�<3^y
��b*�7Y6��R���>?OM�O_���\�"���<�Q�E�`�\.v6.��%R��<�%i7�f��_�A�YRn7ɣ��f�����a��Z�}seD�|X�j3� qK�IX�%N����n[E�g4r7���]u         �   x�E�?�0����a$�6t�A��9��m*��@h����"��p���	��#.�{�G�ˁ8༹�eN���#����M5B7�����Sy5T��.���;rT;�ʚU)�r��>���I!陡g����{R2�e�)�����/�H�         t   x����0C�3�ɉ㭃��_���1<`լR�5w�y��u�K��.�IQM�f���%�&��t��ֱ;XhG�,��cg��Ǜ�js�:c��L�%��ۜ��}��+s!w     