PGDMP  +            	        }         	   bookingdb    17.4    17.4     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    16554 	   bookingdb    DATABASE     �   CREATE DATABASE bookingdb WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE bookingdb;
                     postgres    false            �            1259    16573    booking    TABLE     �  CREATE TABLE public.booking (
    id integer NOT NULL,
    user_id integer,
    schedule_id integer,
    seat_numbers character varying(255),
    number_of_seats integer,
    price_per_seat double precision,
    total_price double precision,
    source character varying(255),
    destination character varying(255),
    booking_date_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    travel_date date,
    departure_time time without time zone
);
    DROP TABLE public.booking;
       public         heap r       postgres    false            �            1259    16572    booking_id_seq    SEQUENCE     �   CREATE SEQUENCE public.booking_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.booking_id_seq;
       public               postgres    false    218            �           0    0    booking_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.booking_id_seq OWNED BY public.booking.id;
          public               postgres    false    217            �            1259    16593    booking_seat    TABLE     �   CREATE TABLE public.booking_seat (
    id integer NOT NULL,
    booking_id integer,
    seat_id integer,
    schedule_id integer,
    travel_date date
);
     DROP TABLE public.booking_seat;
       public         heap r       postgres    false            �            1259    16592    booking_seat_id_seq    SEQUENCE     �   CREATE SEQUENCE public.booking_seat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.booking_seat_id_seq;
       public               postgres    false    220                        0    0    booking_seat_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.booking_seat_id_seq OWNED BY public.booking_seat.id;
          public               postgres    false    219            \           2604    16576 
   booking id    DEFAULT     h   ALTER TABLE ONLY public.booking ALTER COLUMN id SET DEFAULT nextval('public.booking_id_seq'::regclass);
 9   ALTER TABLE public.booking ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217    218            ^           2604    16596    booking_seat id    DEFAULT     r   ALTER TABLE ONLY public.booking_seat ALTER COLUMN id SET DEFAULT nextval('public.booking_seat_id_seq'::regclass);
 >   ALTER TABLE public.booking_seat ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    219    220    220            �          0    16573    booking 
   TABLE DATA           �   COPY public.booking (id, user_id, schedule_id, seat_numbers, number_of_seats, price_per_seat, total_price, source, destination, booking_date_time, travel_date, departure_time) FROM stdin;
    public               postgres    false    218          �          0    16593    booking_seat 
   TABLE DATA           Y   COPY public.booking_seat (id, booking_id, seat_id, schedule_id, travel_date) FROM stdin;
    public               postgres    false    220   B                  0    0    booking_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.booking_id_seq', 13, true);
          public               postgres    false    217                       0    0    booking_seat_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.booking_seat_id_seq', 19, true);
          public               postgres    false    219            `           2606    16581    booking booking_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.booking
    ADD CONSTRAINT booking_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.booking DROP CONSTRAINT booking_pkey;
       public                 postgres    false    218            b           2606    16598    booking_seat booking_seat_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.booking_seat
    ADD CONSTRAINT booking_seat_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.booking_seat DROP CONSTRAINT booking_seat_pkey;
       public                 postgres    false    220            c           2606    16599 )   booking_seat booking_seat_booking_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.booking_seat
    ADD CONSTRAINT booking_seat_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES public.booking(id);
 S   ALTER TABLE ONLY public.booking_seat DROP CONSTRAINT booking_seat_booking_id_fkey;
       public               postgres    false    4704    220    218            �     x��һN�0���)� �u�������R��
�*���M!
D�_>��6)�M�D)�R��������x�<���6��\F򊹊&��ό��"�60P�d��zw��}:�}�.?��HQ٫Rʘ��|S���� ���m�����H#Jm�$e�༁��Л�$�+k%O�960o`�u�i�lU�D�R����Ho�,ZV�ub�XZ��u���DID��:-��%\��W~�h�{X�"���{y/)�mM&$��I��t#I���){�|�|N�0|W˝�      �   d   x�]ϻ�0К��Q���%������W� a�Ԣ�h�r�o�~�@�󳐉Ql	�pyp4lX��lJ�������,�3yo��^���'"/��*�     