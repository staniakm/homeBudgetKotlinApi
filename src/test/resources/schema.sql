CREATE ROLE postgres WITH
    NOLOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    NOREPLICATION
    CONNECTION LIMIT -1;


CREATE PROCEDURE public.addasotoshop(IN product character varying, IN shopid integer)
AS '
DECLARE
    asoId int;
begin
    if exists (select 1 from assortment where name = product) then
        begin
            --pobieramy id asortymentu
            select id into asoId from assortment where name = product limit 1;
            --jeśli asortyment oznaczony jako usunięty - podnosimy
            if exists (select 1 from assortment where id = asoId and del = true)  then
                update assortment set del = false where id = asoId;
            end if;
            --sprawdzamy czy asortyment istniej już w sklepie
            if exists (select 1 from shop_assortment where shop = shopId and aso = asoId) then
                begin
                    --jeśli istnieje to sprawdzamy czy jest usunięty i ewentualnie podnosimy
                    if (select del from shop_assortment where shop = shopId and aso = asoId) = true then
                        update shop_assortment set del = false where shop = shopId and aso = asoId;
                    end if;
                end;
            else
                --jeśli asortyment istnieje ale nie ma go w splepie to dpisujemy
                insert into shop_assortment (aso, shop) values (asoId, shopId);
            end if;
        end;
    else
        --jeśli asortyment nie istnieje należy dodać
        begin
            insert into assortment(name) values (UPPER(product));
            select id into asoId from assortment where name = UPPER(product);
            -- select id into asoId from assortment where name = upper(product) limit 1
            --skoro asortyment nie istniał to napewno nie było go w sklepie - dodajemy
            insert into shop_assortment (aso, shop) values (asoId, shopId);

        end;
    end if;

end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.addasotoshop(IN product character varying, IN shopid integer) OWNER TO postgres;

--
-- TOC entry 254 (class 1255 OID 16598)
-- Name: autoinvoice(); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.autoinvoice()

AS '
declare
    invoice_id bigint;
begin
    if not exists(select 1 from invoice
                  where shop = 8
                    and account = 3
                    and del = false
                    and extract(month from date)=extract(month from current_date)
                    and extract(year from date) = extract(year from current_date)
                    and invoice_number like ''Rachunki %'') then
        begin
            with rows as (
                insert into invoice(date, invoice_number,sum, shop, account)
                    VALUES (current_date, concat(''Rachunki '' , (extract(month from current_date)::varchar)), 0, 8, 3) RETURNING id
            )
            SELECT id into invoice_id FROM rows;

            insert into invoice_details (invoice, assortment, price, amount, unit_price, description, del, category )
            select invoice_id, l.aso, (l.price * l.quantity), l.quantity, l.price,concat(''Rachunki '' , extract(month from current_date)::varchar),
                   false , a.category
            from automatic_invoice_products l
                     join assortment a on l.aso = a.id
                     join shop s ON s.id = l.shop
                     join account ac ON ac.id = l.account
            where l.del =false;

            call recalculateInvoice (invoice_id);
        end;
    end if;
end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.autoinvoice() OWNER TO postgres;

--
-- TOC entry 263 (class 1255 OID 16817)
-- Name: changeinvoiceaccount(integer, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.changeinvoiceaccount(IN invoice_id integer, IN new_account_id integer)

AS '
begin

    if exists(select id from account where id = new_account_id) then
        update account set money = money + invoice.sum
        from invoice where invoice.id = invoice_id
                       and account.id = invoice.account;

        update invoice set account = a.id
        from account a where a.id=new_account_id
                         and invoice.id = invoice_id;

        update account set money = money - invoice.sum
        from invoice where invoice.id = invoice_id
                       and account.id = invoice.account;
    end if;
end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.changeinvoiceaccount(IN invoice_id integer, IN new_account_id integer) OWNER TO postgres;

--
-- TOC entry 264 (class 1255 OID 16818)
-- Name: changeinvoiceaccount(bigint, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.changeinvoiceaccount(IN invoice_id bigint, IN new_account_id integer)

AS '
begin

    if exists(select id from account where id = new_account_id) then
        update account set money = money + invoice.sum
        from invoice where invoice.id = invoice_id
                       and account.id = invoice.account;

        update invoice set account = a.id
        from account a where a.id=new_account_id
                         and invoice.id = invoice_id;

        update account set money = money - invoice.sum
        from invoice where invoice.id = invoice_id
                       and account.id = invoice.account;
    end if;
end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.changeinvoiceaccount(IN invoice_id bigint, IN new_account_id integer) OWNER TO postgres;

--
-- TOC entry 255 (class 1255 OID 16599)
-- Name: copybudgetfromlastmonth(); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.copybudgetfromlastmonth()

AS '
begin
    with con (previous_year, previous_month, current_year, current_month) as (
        values(extract(year from (current_date - interval ''1 month'') ),
               extract(month from (current_date- interval ''1 month'')),
               extract(year from current_date ),
               extract(month from current_date))
    )

    insert into budget(category, month, year, planned, used, percentage)
    select category, t.month, t.year, planned, 0 as used, 0 as percentage from
                                                                              (
                                                                                  select category,con.current_month as month ,con.current_year as year,planned
                                                                                  from budget b, con where b.year = con.previous_year and b.month = con.previous_month
                                                                                  union
                                                                                  select id, con.current_month ,con.current_year, 0 from category k, con
                                                                                  where not exists (select 1 from budget b where b.category = k.id and b.month = con.previous_month and b.year = con.previous_year)
                                                                              ) as t, con
    where not exists (select 1 from budget as b where t.category = b.category and b.month = con.current_month and b.year = con.current_year);


end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.copybudgetfromlastmonth() OWNER TO postgres;

--
-- TOC entry 256 (class 1255 OID 16600)
-- Name: generatereportbycategory(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generatereportbycategory() RETURNS TABLE(name character varying, sum numeric, percentage numeric)

AS '
declare
    total_sum decimal;
begin
    return query
        WITH t as (
            select k.name, sum(ps.price)
            from invoice p
                     join invoice_details ps on p.ID = ps.invoice and ps.del = false
                     join category k on k.id = ps.category
                     left join report_settings as rDat on rDat.parameter = 1 and rDat.session_id = pg_backend_pid()
                     left join report_settings kat on kat.parameter = 2 and kat.session_id =   pg_backend_pid()
                     left join report_settings rSkl on rSkl.parameter = 3 and rSkl.session_id =  pg_backend_pid()
            where p.del =false
              and (rDat.session_id is null or p.date between rDat.min_val::date and rDat.max_val::date)
              and (rSkl.session_id is null or p.shop = rSkl.min_val::INTEGER)
              and (kat.session_id is null or kat.min_val::INTEGER = ps.category)
            group by k.name
        )
        SELECT t.name, t.sum,
               cast((t.sum/(select sum(t1.sum) from t as t1))*100 as decimal(10,2)) from t ;
end
' LANGUAGE PLPGSQL;


ALTER FUNCTION public.generatereportbycategory() OWNER TO postgres;

--
-- TOC entry 257 (class 1255 OID 16601)
-- Name: generatereportbycategoryandaccount(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generatereportbycategoryandaccount() RETURNS TABLE(owner_name character varying, category_name character varying, sum numeric, percentage numeric)

AS '
declare
    total_sum decimal;
begin
    return query
        WITH t as (
            select ao.owner_name, k.name as category_name, sum(ps.price)
            from invoice p
                     join invoice_details ps on p.ID = ps.invoice and ps.del = false
                     join category k on k.id = ps.category
                     join account a on a.id = p.account
                     join account_owner ao ON ao.id = a."owner"
                     left join report_settings as rDat on rDat.parameter = 1 and rDat.session_id = pg_backend_pid()
                     left join report_settings kat on kat.parameter = 2 and kat.session_id =   pg_backend_pid()
                     left join report_settings rSkl on rSkl.parameter = 3 and rSkl.session_id =  pg_backend_pid()
            where p.del =false
              and ps.del = false
              and (rDat.session_id is null or p.date between rDat.min_val::date and rDat.max_val::date)
              and (rSkl.session_id is null or p.shop = rSkl.min_val::INTEGER)
              and (kat.session_id is null or kat.min_val::INTEGER = ps.category)
            group by ao.owner_name, k.name
        )
        SELECT t.owner_name, t.category_name, t.sum,
               cast((t.sum/(select sum(t1.sum) from t as t1))*100 as decimal(10,2)) as percentage from t
        order by owner_name, percentage desc;
end
' LANGUAGE PLPGSQL;


ALTER FUNCTION public.generatereportbycategoryandaccount() OWNER TO postgres;

--
-- TOC entry 258 (class 1255 OID 16602)
-- Name: generatestandardreport(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.generatestandardreport() RETURNS TABLE(month integer, year integer, owner_1 numeric, owner_2 numeric, common numeric, expence_sum numeric, income numeric, savings numeric)

AS '
begin
    return query
        WITH t as (
            select distinct extract(MONTH from date) as month, extract(year from date) as year, account, sum(ps.price) sums
            from invoice p
                     join invoice_details ps on p.ID = ps.invoice and ps.del = false
                     left join report_settings as rDat on rDat.parameter = 1 and rDat.session_id = pg_backend_pid()
                     left join report_settings kat on kat.parameter = 2 and kat.session_id =   pg_backend_pid()
                     left join report_settings rSkl on rSkl.parameter = 3 and rSkl.session_id =  pg_backend_pid()
            where p.del =false
              and (rDat.session_id is null or p.date between rDat.min_val::date and rDat.max_val::date)
              and (rSkl.session_id is null or p.shop = rSkl.min_val::INTEGER)
              and (kat.session_id is null or kat.min_val::INTEGER = ps.category)
            group by account, extract(month from p.date), extract (year from  p.date)
        )

        SELECT t.month::INTEGER ,
               t.year::INTEGER,
               coalesce((select coalesce(sum(sums),0) from t t1
                                                               join account k on k.ID = t1.account
                         where k.owner = 1 and k.del=false and t1.month = t.month and t1.year=t.year),0) as owner_1,
               coalesce((select coalesce(sum(sums),0) from t t2
                                                               join account k on k.ID = t2.account
                         where k.owner=2 and k.del=false and t2.month = t.month and t2.year=t.year),0) as owner_2,
               coalesce((select coalesce(sum(sums),0)
                         from t t3 join account k on k.ID = t3.account
                         where k.owner=3 and k.del=false and t3.month = t.month and t3.year=t.year),0) common,
               cast(sum(sums) as decimal(9,2)) expence_sum,
               cast(coalesce((select sum(value)
                              from income inc
                              where extract(year from inc.date) = t.year
                                and extract(month from inc.date) = t.month),0) as decimal(9,2)) as income
                , cast(coalesce((select sum(value)
                                 from income inc where extract(year from inc.date) = t.year
                                                   and extract(month from inc.date) = t.month),0) - cast(sum(sums) as decimal(9,2)) as decimal(9,2)) as savings
        from t
                 left join report_settings rDat on rDat.parameter = 1 and rDat.session_id = pg_backend_pid()
                 left join report_settings kat on kat.parameter = 2 and kat.session_id =  pg_backend_pid()
                 left join report_settings rSkl on rSkl.parameter = 3 and rSkl.session_id = pg_backend_pid()
        group by t.month, t.year;
end
' LANGUAGE PLPGSQL;


ALTER FUNCTION public.generatestandardreport() OWNER TO postgres;

--
-- TOC entry 259 (class 1255 OID 16603)
-- Name: recalculatebudget(date); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.recalculatebudget(IN recalculation_date date DEFAULT CURRENT_DATE)

AS '
begin
    call copybudgetfromlastmonth ();
    update budget b set used = coalesce(s.kwota,0)
    from budget b1
             left join (
        select sum(ps.price) kwota, a.category, extract(month from p.date) miesiac from invoice_details ps
                                                                                            join invoice p ON p.id = ps.invoice
                                                                                            join assortment a ON a.id = ps.assortment
        where extract(month from p.date) = extract(month from recalculation_date) and extract(year from p.date) = extract(year from recalculation_date) and ps.del = false
        group by a.category, extract(month from p.date)) as s on s.category = b1.category and s.miesiac = b1.month
    where b1.id = b.id
      and b1.used <> coalesce(s.kwota,0)
      and b1.month = extract(month from recalculation_date) and b1.year = extract(year from recalculation_date);

    update budget set percentage = (used*100/planned)
    where month = extract(month from recalculation_date) and year = extract(year from recalculation_date) and planned > 0;

end
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.recalculatebudget(IN recalculation_date date) OWNER TO postgres;

--
-- TOC entry 260 (class 1255 OID 16604)
-- Name: recalculateinvoice(integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.recalculateinvoice(IN invoice_id bigint DEFAULT '-1'::bigint)

AS '
DECLARE
    month_date date;
    invoice_to_update integer;
begin
    if (invoice_id = -1) then
        select max(id) into invoice_to_update from invoice;
    else
        select invoice_id into invoice_to_update ;
    end if;

    begin
        update invoice_details i set price = (i.amount * i.unit_price) - i.discount, category = a.category
        from assortment a
        where i.assortment = a.id and i.invoice = invoice_to_update and i.del = false;
        select date into month_date from invoice where id = invoice_to_update;
        update invoice set sum = (select sum(price) from invoice_details where invoice = invoice_to_update) where id = invoice_to_update;
    end;
end;
'  LANGUAGE PLPGSQL;


ALTER PROCEDURE public.recalculateinvoice(IN invoice_id bigint) OWNER TO postgres;

--
-- TOC entry 241 (class 1255 OID 16791)
-- Name: recalculateselectedbudget(integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.recalculateselectedbudget(IN selectedbudget integer)

AS '
begin
    if (select planned from budget where id = selectedBudget) > 0 then
        update budget set percentage = (used*100/planned)
        where id = selectedBudget ;
    end if;

end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.recalculateselectedbudget(IN selectedbudget integer) OWNER TO postgres;

--
-- TOC entry 261 (class 1255 OID 16605)
-- Name: showinvoicelist(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.showinvoicelist() RETURNS TABLE(invoice_id bigint, invoice_date text, invoice_number character varying, sum numeric, owner_name character varying, shop_name character varying)

AS '
declare
    total_sum decimal;
begin
    return query
        WITH t as (
            select p.id, to_char( p.date::date, ''yyyy-MM-dd''),p.invoice_number,p.sum, ao.owner_name, shop.name as shop_name
            from invoice p
                     join account a on a.id = p.account
                     join account_owner ao ON ao.id = a."owner"
                     join shop ON shop.id = p.shop
                     left join report_settings as rDat on rDat.parameter = 1 and rDat.session_id = pg_backend_pid()
                     left join report_settings rSkl on rSkl.parameter = 3 and rSkl.session_id =  pg_backend_pid()
            where p.del =false
              and (rDat.session_id is null or p.date between rDat.min_val::date and rDat.max_val::date)
              and (rSkl.session_id is null or p.shop = rSkl.min_val::INTEGER)
        )
        SELECT * from t;
end
' LANGUAGE PLPGSQL;


ALTER FUNCTION public.showinvoicelist() OWNER TO postgres;

--
-- TOC entry 262 (class 1255 OID 16606)
-- Name: updateasocategory(integer, character varying, character varying, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.updateasocategory(IN assortment_id integer, IN category_name character varying, IN assortment_name character varying, IN category_id integer DEFAULT '-1'::integer)

AS '
begin
    if (category_id = -1 and category_name <> '''') then
        begin
            --dodajemy kategorię
            if not exists (select 1 from category where upper(name) = upper(category_name)) then
                begin
                    insert into category(name) values (category_name);
                    update assortment set category = (select coalesce(id, 1) from category where name= category_name) where id = assortment_id;
                    insert into budget(category, month, year, planned, used, percentage)
                    select k.id, l.id, extract(year from current_date),0,0,0 from category k
                                                                                      cross join counter l
                    where name = category_name
                      and l.id between extract(month from current_date) and 12;
                end;
            else
                begin
                    update assortment set category = (select coalesce(id, 1) from category where name = category_name) where id = assortment_id;
                end;
            end if;
        end;
    elsif(category_id > -1 and category_name <>'''') then
        begin
            update assortment set category = category_id where id = assortment_id and coalesce(category, -1) <> category_id;
        end;
    end if;

end;
' LANGUAGE PLPGSQL;


ALTER PROCEDURE public.updateasocategory(IN assortment_id integer, IN category_name character varying, IN assortment_name character varying, IN category_id integer) OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 16607)
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 211 (class 1259 OID 16608)
-- Name: account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account (
                                id integer DEFAULT nextval('public.account_id_seq'::regclass) NOT NULL,
                                account_name character varying(200) NOT NULL,
                                description character varying(200) NOT NULL,
                                money numeric(15,2) NOT NULL,
                                del boolean DEFAULT false NOT NULL,
                                owner integer NOT NULL
);


ALTER TABLE public.account OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 16616)
-- Name: account_owner_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_owner_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_owner_id_seq OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 16613)
-- Name: account_owner; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account_owner (
                                      id integer DEFAULT nextval('public.account_owner_id_seq'::regclass) NOT NULL,
                                      owner_name character varying(100) NOT NULL,
                                      description character varying(200)
);


ALTER TABLE public.account_owner OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16617)
-- Name: assortment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.assortment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.assortment_id_seq OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16618)
-- Name: assortment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assortment (
                                   id integer DEFAULT nextval('public.assortment_id_seq'::regclass) NOT NULL,
                                   name character varying(100) NOT NULL,
                                   del boolean DEFAULT false NOT NULL,
                                   category integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.assortment OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16624)
-- Name: automatic_invoice_products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.automatic_invoice_products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.automatic_invoice_products_id_seq OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16625)
-- Name: automatic_invoice_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.automatic_invoice_products (
                                                   id integer DEFAULT nextval('public.automatic_invoice_products_id_seq'::regclass) NOT NULL,
                                                   aso integer NOT NULL,
                                                   price numeric(15,2) NOT NULL,
                                                   quantity numeric(15,2) NOT NULL,
                                                   del boolean DEFAULT false NOT NULL,
                                                   shop integer NOT NULL,
                                                   account integer NOT NULL
);


ALTER TABLE public.automatic_invoice_products OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16630)
-- Name: budget_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.budget_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.budget_id_seq OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16631)
-- Name: budget; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.budget (
                               id integer DEFAULT nextval('public.budget_id_seq'::regclass) NOT NULL,
                               category integer NOT NULL,
                               month integer NOT NULL,
                               year integer NOT NULL,
                               planned numeric(15,2) NOT NULL,
                               used numeric(15,2) NOT NULL,
                               percentage integer NOT NULL
);


ALTER TABLE public.budget OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16635)
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_id_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16636)
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
                                 id integer DEFAULT nextval('public.category_id_seq'::regclass) NOT NULL,
                                 name character varying(100) NOT NULL
);


ALTER TABLE public.category OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16643)
-- Name: counter_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.counter_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.counter_id_seq OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16640)
-- Name: counter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.counter (
    id integer DEFAULT nextval('public.counter_id_seq'::regclass) NOT NULL
);


ALTER TABLE public.counter OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16644)
-- Name: income_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.income_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.income_id_seq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16645)
-- Name: income; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.income (
                               id integer DEFAULT nextval('public.income_id_seq'::regclass) NOT NULL,
                               value numeric(15,2) NOT NULL,
                               description character varying(100) NOT NULL,
                               account integer NOT NULL,
                               date date NOT NULL
);


ALTER TABLE public.income OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16649)
-- Name: invoice_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.invoice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoice_id_seq OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16650)
-- Name: invoice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice (
                                id bigint DEFAULT nextval('public.invoice_id_seq'::regclass) NOT NULL,
                                date date NOT NULL,
                                invoice_number character varying(50) NOT NULL,
                                sum numeric(15,2) NOT NULL,
                                description character varying(100) DEFAULT ''::character varying,
                                del boolean DEFAULT false NOT NULL,
                                account integer NOT NULL,
                                shop integer NOT NULL,
                                created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.invoice OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 16657)
-- Name: invoice_details_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.invoice_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.invoice_details_id_seq OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 16658)
-- Name: invoice_details; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invoice_details (
                                        id bigint DEFAULT nextval('public.invoice_details_id_seq'::regclass) NOT NULL,
                                        invoice bigint NOT NULL,
                                        price numeric(15,2) NOT NULL,
                                        amount numeric(15,3) NOT NULL,
                                        unit_price numeric(15,2) NOT NULL,
                                        discount numeric(15,2) DEFAULT 0 NOT NULL,
                                        description character varying(100) DEFAULT ''::character varying NOT NULL,
                                        del boolean DEFAULT false NOT NULL,
                                        category integer DEFAULT 1 NOT NULL,
                                        assortment integer NOT NULL,
                                        created timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


ALTER TABLE public.invoice_details OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 16792)
-- Name: media_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.media_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.media_type_id_seq OWNER TO postgres;

--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 236
-- Name: media_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--


--
-- TOC entry 237 (class 1259 OID 16793)
-- Name: media_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.media_type (
                                   id integer DEFAULT nextval('public.media_type_id_seq'::regclass) NOT NULL,
                                   name character varying NOT NULL,
                                   del boolean DEFAULT false NOT NULL
);


ALTER TABLE public.media_type OWNER TO postgres;

CREATE UNIQUE INDEX name_unique ON media_type (UPPER(name));
--
-- TOC entry 238 (class 1259 OID 16801)
-- Name: media_usage_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.media_usage_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.media_usage_id_seq OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 16803)
-- Name: media_usage; Type: TABLE; Schema: public; Owner: postgres
--


CREATE TABLE public.media_usage (
                                    id integer DEFAULT nextval('public.media_usage_id_seq'::regclass) NOT NULL,
                                    media_type integer NOT NULL,
                                    year integer NOT NULL,
                                    month integer NOT NULL,
                                    meter_read double precision NOT NULL,
                                    read_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE public.media_usage OWNER TO postgres;

ALTER TABLE IF EXISTS public.media_usage
    ADD CONSTRAINT year_month_type_unique UNIQUE (year, month, media_type);

CREATE TABLE public.report_settings (
                                        min_val character varying(100),
                                        max_val character varying(100),
                                        parameter integer,
                                        session_id integer
);


ALTER TABLE public.report_settings OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 16670)
-- Name: salary_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.salary_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.salary_type_id_seq OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 16671)
-- Name: salary_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salary_type (
                                    id integer DEFAULT nextval('public.salary_type_id_seq'::regclass) NOT NULL,
                                    name character varying NOT NULL
);


ALTER TABLE public.salary_type OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 16677)
-- Name: shop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.shop_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.shop_id_seq OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 16678)
-- Name: shop; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop (
                             id integer DEFAULT nextval('public.shop_id_seq'::regclass) NOT NULL,
                             name character varying(100) NOT NULL
);


ALTER TABLE public.shop OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 16682)
-- Name: shop_assortment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shop_assortment (
                                        aso integer NOT NULL,
                                        shop integer NOT NULL,
                                        del boolean DEFAULT false NOT NULL
);


ALTER TABLE public.shop_assortment OWNER TO postgres;

--
-- TOC entry 3278 (class 2604 OID 16796)
-- Name: media_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_type ALTER COLUMN id SET DEFAULT nextval('public.media_type_id_seq'::regclass);


--
-- TOC entry 3279 (class 2604 OID 16806)
-- Name: media_usage id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_usage ALTER COLUMN id SET DEFAULT nextval('public.media_usage_id_seq'::regclass);


--
-- TOC entry 3284 (class 2606 OID 16689)
-- Name: account_owner account_owner_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account_owner
    ADD CONSTRAINT account_owner_pk PRIMARY KEY (id);


--
-- TOC entry 3282 (class 2606 OID 16691)
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- TOC entry 3286 (class 2606 OID 16693)
-- Name: assortment assortment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assortment
    ADD CONSTRAINT assortment_pkey PRIMARY KEY (id);


--
-- TOC entry 3288 (class 2606 OID 16695)
-- Name: automatic_invoice_products automatic_invoice_products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_invoice_products
    ADD CONSTRAINT automatic_invoice_products_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 16697)
-- Name: budget budget_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.budget
    ADD CONSTRAINT budget_pkey PRIMARY KEY (id);


--
-- TOC entry 3294 (class 2606 OID 16699)
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- TOC entry 3296 (class 2606 OID 16701)
-- Name: counter counter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.counter
    ADD CONSTRAINT counter_pkey PRIMARY KEY (id);


--
-- TOC entry 3298 (class 2606 OID 16703)
-- Name: income income_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.income
    ADD CONSTRAINT income_pkey PRIMARY KEY (id);


--
-- TOC entry 3302 (class 2606 OID 16705)
-- Name: invoice_details invoice_details_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_details
    ADD CONSTRAINT invoice_details_pkey PRIMARY KEY (id);


--
-- TOC entry 3300 (class 2606 OID 16707)
-- Name: invoice invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- TOC entry 3310 (class 2606 OID 16800)
-- Name: media_type media_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_type
    ADD CONSTRAINT media_type_pkey PRIMARY KEY (id);


--
-- TOC entry 3312 (class 2606 OID 16809)
-- Name: media_usage media_usage_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_usage
    ADD CONSTRAINT media_usage_pkey PRIMARY KEY (id);


--
-- TOC entry 3308 (class 2606 OID 16709)
-- Name: shop_assortment pk_shop_aso; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_assortment
    ADD CONSTRAINT pk_shop_aso PRIMARY KEY (aso, shop);


--
-- TOC entry 3304 (class 2606 OID 16711)
-- Name: salary_type salary_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salary_type
    ADD CONSTRAINT salary_type_pkey PRIMARY KEY (id);


--
-- TOC entry 3306 (class 2606 OID 16713)
-- Name: shop shop_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop
    ADD CONSTRAINT shop_pkey PRIMARY KEY (id);


--
-- TOC entry 3292 (class 2606 OID 16715)
-- Name: budget unique_budget_index; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.budget
    ADD CONSTRAINT unique_budget_index UNIQUE (category, month, year);


--
-- TOC entry 3313 (class 2606 OID 16716)
-- Name: account fk_account_owner_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT fk_account_owner_id FOREIGN KEY (owner) REFERENCES public.account_owner(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3314 (class 2606 OID 16721)
-- Name: assortment fk_assortment_category_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assortment
    ADD CONSTRAINT fk_assortment_category_id FOREIGN KEY (category) REFERENCES public.category(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3315 (class 2606 OID 16726)
-- Name: automatic_invoice_products fk_automatic_invoice_products_account_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_invoice_products
    ADD CONSTRAINT fk_automatic_invoice_products_account_id FOREIGN KEY (account) REFERENCES public.account(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3316 (class 2606 OID 16731)
-- Name: automatic_invoice_products fk_automatic_invoice_products_aso_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_invoice_products
    ADD CONSTRAINT fk_automatic_invoice_products_aso_id FOREIGN KEY (aso) REFERENCES public.assortment(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3317 (class 2606 OID 16736)
-- Name: automatic_invoice_products fk_automatic_invoice_products_shop_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.automatic_invoice_products
    ADD CONSTRAINT fk_automatic_invoice_products_shop_id FOREIGN KEY (shop) REFERENCES public.shop(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3318 (class 2606 OID 16741)
-- Name: budget fk_budget_category_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.budget
    ADD CONSTRAINT fk_budget_category_id FOREIGN KEY (category) REFERENCES public.category(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3319 (class 2606 OID 16746)
-- Name: income fk_income_account_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.income
    ADD CONSTRAINT fk_income_account_id FOREIGN KEY (account) REFERENCES public.account(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3320 (class 2606 OID 16751)
-- Name: invoice fk_invoice_account_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk_invoice_account_id FOREIGN KEY (account) REFERENCES public.account(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3322 (class 2606 OID 16756)
-- Name: invoice_details fk_invoice_details_assortment_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_details
    ADD CONSTRAINT fk_invoice_details_assortment_id FOREIGN KEY (assortment) REFERENCES public.assortment(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3323 (class 2606 OID 16761)
-- Name: invoice_details fk_invoice_details_category_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_details
    ADD CONSTRAINT fk_invoice_details_category_id FOREIGN KEY (category) REFERENCES public.category(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3324 (class 2606 OID 16766)
-- Name: invoice_details fk_invoice_details_invoice_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice_details
    ADD CONSTRAINT fk_invoice_details_invoice_id FOREIGN KEY (invoice) REFERENCES public.invoice(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3321 (class 2606 OID 16771)
-- Name: invoice fk_invoice_shop_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT fk_invoice_shop_id FOREIGN KEY (shop) REFERENCES public.shop(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3325 (class 2606 OID 16776)
-- Name: shop_assortment fk_shop_assortment_aso_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_assortment
    ADD CONSTRAINT fk_shop_assortment_aso_id FOREIGN KEY (aso) REFERENCES public.assortment(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3326 (class 2606 OID 16781)
-- Name: shop_assortment fk_shop_assortment_shop_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shop_assortment
    ADD CONSTRAINT fk_shop_assortment_shop_id FOREIGN KEY (shop) REFERENCES public.shop(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- TOC entry 3327 (class 2606 OID 16811)
-- Name: media_usage media_type_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_usage
    ADD CONSTRAINT media_type_fk FOREIGN KEY (media_type) REFERENCES public.media_type(id) NOT VALID;


