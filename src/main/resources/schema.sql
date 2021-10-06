CREATE TABLE if not exists ACCOUNT_OWNER
(
    owner_id    serial primary key,
    name        varchar(100) NOT NULL,
    description nchar(100)   NULL
);

CREATE TABLE if not exists  AKCJE
(
    id    serial primary key,
    nazwa varchar(100) NOT NULL
);

CREATE TABLE if not exists  KATEGORIA
(
    id            serial primary key,
    nazwa         varchar(100) NULL,
    main_category int          NULL
);


CREATE TABLE if not exists  ASORTYMENT
(
    id     int primary key,
    nazwa  varchar(100) NOT NULL,
    del    int          NOT NULL default 0,
    id_kat int          NOT NULL,
    constraint fk_kat foreign key (id_kat)
        references kategoria (id)
);

CREATE TABLE if not exists  sklepy
(
    id    serial primary key,
    sklep varchar(70) NOT NULL,
    shop  varchar(70) NULL
);

CREATE TABLE if not exists  konto
(
    id             serial primary key,
    nazwa          varchar(100)  NULL,
    kwota          decimal(9, 2) NULL,
    opis           varchar(100)  NULL,
    del            int           NULL default 0,
    wlasciciel     int           NULL,
    oprocentowanie decimal(5, 2) NOT NULL,
    account_type   varchar(20)   NOT NULL
);

CREATE TABLE if not exists  ASORTYMENT_SKLEP
(
    id       serial primary key,
    id_aso   int NOT NULL,
    id_sklep int NOT NULL,
    del      int NOT NULL default 0,
    CONSTRAINT fk_aso foreign key (id_aso)
        references ASORTYMENT (id),
    constraint fk_sklep foreign key (id_sklep)
        references sklepy (ID)
);

CREATE TABLE if not exists  AUTOMATIC_PRODUCT_LIST
(
    id             serial primary key,
    ASO_ID         int           NOT NULL,
    PRICE_PER_UNIT money         NOT NULL,
    QUANTITY       decimal(9, 2) NOT NULL,
    DEL            smallint      NOT NULL default 0,
    SHOP_ID        int           NOT NULL,
    ACCOUNT_ID     int           NOT NULL,
    constraint fk_aso foreign key (ASO_ID)
        references ASORTYMENT (id),
    constraint fk_shop foreign key (SHOP_ID)
        references sklepy (id),
    constraint fk_account foreign key (ACCOUNT_ID)
        references konto (id)
);

CREATE TABLE if not exists  budzet
(
    id          serial primary key,
    category    int            NULL,
    miesiac     int            NULL,
    rok         int            NULL,
    planed      decimal(10, 2) NULL,
    used        decimal(10, 2) NULL,
    percentUsed int            NULL
);

CREATE TABLE if not exists  kat_produkt
(
    id       serial primary key,
    produkt  varchar(100) NOT NULL,
    id_kat   int          NULL,
    id_sklep int          NULL,
    constraint fk_kat foreign key (id_kat)
        references KATEGORIA (id),
    constraint fk_shop foreign key (id_sklep)
        references sklepy (id)
);

CREATE TABLE if not exists  licznik
(
    id serial primary key
);

CREATE TABLE if not exists  log_table
(
    id       serial primary key,
    log_data varchar(2000) NULL
);

CREATE TABLE if not exists  main_category
(
    id   serial primary key,
    name varchar(30) NULL
);


CREATE TABLE if not exists  paragon_log
(
    id          serial primary key,
    id_paragonu int   NOT NULL,
    cena_przed  money NOT NULL,
    cena_po     money NOT NULL,
    konto       int   NOT NULL,
    data_synch  date  NOT NULL
);

CREATE TABLE if not exists  paragony
(
    id          serial primary key,
    data        date         NOT NULL,
    nr_paragonu varchar(50)  NOT NULL,
    suma        money        NOT NULL,
    konto       int          NOT NULL,
    opis        varchar(150) NULL,
    del         int          NULL default 0,
    ID_sklep    int          NULL,
    CREATED     timestamp    NOT NULL,
    constraint fk_shop foreign key (ID_sklep)
        references sklepy (id)
);

CREATE TABLE if not exists  paragony_szczegoly
(
    ID                serial primary key,
    id_paragonu       int           NOT NULL,
    cena              decimal(8, 2) NULL,
    opis              varchar(150)  NULL,
    del               int           NULL default 0,
    ilosc             decimal(6, 3) NULL,
    cena_za_jednostke money         NULL,
    kategoria         int           NULL,
    ID_ASO            int           NOT NULL,
    CREATED           timestamp     NOT NULL,
    rabat             money         NOT NULL,
    constraint fk_invoice foreign key (id_paragonu)
        references paragony (id)
);

CREATE TABLE if not exists  przychody
(
    id    serial primary key,
    kwota money        NULL,
    opis  varchar(100) NULL,
    konto int          NULL,
    data  date         NULL
);

CREATE TABLE if not exists  rapOrg
(
    minVal   varchar(100) NULL,
    maxVal   varchar(100) NULL,
    parametr int          NULL,
    sesja    int          NULL
);

CREATE TABLE if not exists  SALARY_TYPE
(
    id   serial primary key,
    text varchar(50) NULL
);

ALTER TABLE account_owner
    alter column description set default '';

ALTER TABLE ASORTYMENT
    alter column del set default 0;

ALTER TABLE ASORTYMENT
    alter column id_kat set default 1;

ALTER TABLE AUTOMATIC_PRODUCT_LIST
    alter column SHOP_ID set default 8;

ALTER TABLE AUTOMATIC_PRODUCT_LIST
    alter column ACCOUNT_ID set default 3;


ALTER TABLE kat_produkt
    alter column id_sklep set default 0;


ALTER TABLE kategoria
    alter column main_category set default 1;


ALTER TABLE konto
    alter column oprocentowanie set default 0;

ALTER TABLE konto
    alter column account_type set default 'ROR';


ALTER TABLE paragon_log
    alter column konto set default 1;

ALTER TABLE paragon_log
    alter column data_synch set default CURRENT_TIMESTAMP;

ALTER TABLE paragony
    alter column data set default CURRENT_TIMESTAMP;

ALTER TABLE paragony
    alter column konto set default 1;

ALTER TABLE paragony
    alter column opis set default '';

ALTER TABLE paragony
    alter column ID_sklep set default 0;

ALTER TABLE paragony
    alter column CREATED set default CURRENT_TIMESTAMP;


ALTER TABLE paragony_szczegoly
    alter column CREATED set default CURRENT_TIMESTAMP;

ALTER TABLE paragony_szczegoly
    alter column rabat set default 0.0;

ALTER TABLE przychody
    alter column kwota set default 0;

ALTER TABLE przychody
    alter column data set default CURRENT_TIMESTAMP;

