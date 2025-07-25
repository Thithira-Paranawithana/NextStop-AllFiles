PGDMP  8            	        }         	   paymentdb    17.4    17.4     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16714 	   paymentdb    DATABASE     �   CREATE DATABASE paymentdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE paymentdb;
                     postgres    false            �            1259    16716    payments    TABLE     �  CREATE TABLE public.payments (
    id bigint NOT NULL,
    user_id integer NOT NULL,
    schedule_id integer NOT NULL,
    travel_date date NOT NULL,
    amount numeric(38,2) NOT NULL,
    payment_method character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    stripe_payment_intent_id character varying(255)
);
    DROP TABLE public.payments;
       public         heap r       postgres    false            �            1259    16715    payments_id_seq    SEQUENCE     �   CREATE SEQUENCE public.payments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.payments_id_seq;
       public               postgres    false    218            �           0    0    payments_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.payments_id_seq OWNED BY public.payments.id;
          public               postgres    false    217            W           2604    16723    payments id    DEFAULT     j   ALTER TABLE ONLY public.payments ALTER COLUMN id SET DEFAULT nextval('public.payments_id_seq'::regclass);
 :   ALTER TABLE public.payments ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217    218            �          0    16716    payments 
   TABLE DATA           �   COPY public.payments (id, user_id, schedule_id, travel_date, amount, payment_method, status, created_at, stripe_payment_intent_id) FROM stdin;
    public               postgres    false    218   -       �           0    0    payments_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.payments_id_seq', 9, true);
          public               postgres    false    217            Z           2606    16725    payments payments_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.payments DROP CONSTRAINT payments_pkey;
       public                 postgres    false    218            �   L  x����N�@��ݧ����y�N��@$ ��h9TD�Қ���Cq7�'���3��!�j�npK  ٬���5i6!YǳY2K���O�{e��̡� �&��$�zg�wW�|�u1�ϣ�Q�~4#<�9�@󊉖���4�4]�8"e]Fz�$RQ1��⇁�mw�6\Q��xt^i&�Ei��i�ݻшT��i ��3B{���U�S�6�������.c=(�%C+]&naPFK~�^�����Y/����W��l�\&�z�6��_���HDk�i��j:��F�	u�vf>�|�r-������yl�����3J�;R��     