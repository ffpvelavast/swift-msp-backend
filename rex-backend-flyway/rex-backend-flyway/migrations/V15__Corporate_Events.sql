DROP TABLE IF EXISTS events.corporate_event_used;
DROP TABLE IF EXISTS events.corporate_event_templates;

DROP TABLE IF EXISTS events.corporate_event_admins;
DROP TABLE IF EXISTS events.corporate_event;

DROP SEQUENCE IF EXISTS events.corporate_event_id_seq;
DROP SEQUENCE IF EXISTS events.corporate_event_templates_id_seq;
DROP SEQUENCE IF EXISTS events.corporate_event_used_id_seq;

CREATE SEQUENCE events.corporate_event_id_seq;
CREATE SEQUENCE events.corporate_event_templates_id_seq;
CREATE SEQUENCE events.corporate_event_used_id_seq;


---------------------------------------------------------------------------------
------------------------- Section on Corporate Events ---------------------------
---------------------------------------------------------------------------------

-------------------------------- Table corporate_event --------------------------
CREATE TABLE IF NOT EXISTS events.corporate_event
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	--- later to be changed to separate table --- 
    booking_released_at timestamp(6) with time zone,
	booking_released_by text,
	processing_released_at timestamp(6) with time zone,
	processing_released_by text,
	processing_id bigint,
	id bigint NOT NULL DEFAULT nextval('events.corporate_event_id_seq'::regclass),
	sortid integer,
	platform_id bigint not NULL,
	corporate_event_template_id bigint,
	event_employer_id bigint not NULL,
    event_title citext NOT NULL,
	event_manager citext,
	event_budget numeric(19,2),
	event_date date not NULL,
	client_ordernumber text,
	costcenter text,
	posting_account text,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_inheritance' 
	---  ENUM Values: INHERITANCE_FORCED, INHERITANCE_OPTIONAL, INHERITANCE_FORBIDDEN
	inheritance_mode text NOT NULL,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_type' 
	---  ENUM Values: EVENTTYPE_CALCULATED, EVENTTYPE_INTERNAL, EVENTTYPE_EXTERNAL
    event_type text NOT NULL,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_scope' 
	---  ENUM Values: EVENTSCOPE_CALCULATED, EVENTSCOPE_FOOD_BEVERAGE, 'EVENTSCOPE_ENTERTAINMENT'
	event_scope text NOT NULL,
	employees_opentoall boolean NOT NULL default false,
	corporate_unit_id bigint,
	presets_immutable boolean NOT NULL default True,
	taxexempt_applicable boolean NOT NULL default false,
	bookable_from date NOT NULL default '2020-01-01'::date,
	bookable_through date NOT NULL default '2999-12-31'::date,
	bookable_period daterange GENERATED ALWAYS AS (daterange(bookable_from, bookable_through+1,'[)')) STORED,
    CONSTRAINT event_pkey PRIMARY KEY (id),
	CONSTRAINT corpevent_uq unique (platform_id,event_title),
	CONSTRAINT corporate_event_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT corporate_event_to_employer FOREIGN KEY (event_employer_id)
        REFERENCES rexbase.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT inheritance_mode_values 
		CHECK (inheritance_mode IN ('INHERITANCE_FORCED', 'INHERITANCE_OPTIONAL','INHERITANCE_FORBIDDEN')),
	CONSTRAINT event_type_values 
		CHECK (event_type IN ('EVENTTYPE_CALCULATED', 'EVENTTYPE_INTERNAL','EVENTTYPE_EXTERNAL')),
	CONSTRAINT event_scope_values 
		CHECK (event_scope IN ('EVENTSCOPE_CALCULATED', 'EVENTSCOPE_FOOD_BEVERAGE','EVENTSCOPE_ENTERTAINMENT'))
);

-------------------------- Table EventAdmins ------------------------
CREATE TABLE IF NOT EXISTS events.corporate_event_admins
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    event_id bigint NOT NULL,
	rexuser_id bigint NOT NULL,
	active_from date NOT NULL default '2020-01-01'::date,
	active_through date NOT NULL default '2999-12-31'::date,
	active_period daterange GENERATED ALWAYS AS (daterange(active_from, active_through+1,'[)')) STORED,
    CONSTRAINT corporate_eventadmin_pkey PRIMARY KEY (event_id,rexuser_id),
	CONSTRAINT corporate_eventadmin_to_event_id FOREIGN KEY (event_id)
        REFERENCES events.corporate_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT corporate_eventadmin_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-----------------------------------------------------------------------------------------
------------------------------- Templates -----------------------------------------------
-----------------------------------------------------------------------------------------

-------------------------------- Table corporate_event template--------------------------
CREATE TABLE IF NOT EXISTS events.corporate_event_templates
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	--- later to be changed to separate table --- 
	id bigint NOT NULL DEFAULT nextval('events.corporate_event_templates_id_seq'::regclass),
	sortid integer,
	platform_id bigint not NULL,
	event_employer_id bigint not NULL,
	event_year integer NOT NULL,
	event_month integer NOT NULL,
	event_date date GENERATED ALWAYS AS (make_date(event_year,event_month,1)) STORED,

    event_title citext NOT NULL,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_inheritance' 
	---  ENUM Values: INHERITANCE_FORCED, INHERITANCE_OPTIONAL, INHERITANCE_FORBIDDEN
	inheritance_mode text NOT NULL,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_type' 
	---  ENUM Values: EVENTTYPE_CALCULATED, EVENTTYPE_INTERNAL, EVENTTYPE_EXTERNAL
    event_type text NOT NULL,
	--- Value from User-Selection in Screen 'event_detail', Listbox 'event_scope' 
	---  ENUM Values: EVENTSCOPE_CALCULATED, EVENTSCOPE_FOOD_BEVERAGE, 'EVENTSCOPE_ENTERTAINMENT'
	event_scope text NOT NULL,
	employees_opentoall boolean NOT NULL default false,
	corporate_unit_id bigint,
	taxexempt_applicable boolean NOT NULL default True,
	presets_immutable boolean NOT NULL default True,
	
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
	
    CONSTRAINT corporate_event_template_pkey PRIMARY KEY (id),
	CONSTRAINT corporate_event_template_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT corporate_event_template_to_employer FOREIGN KEY (event_employer_id)
        REFERENCES rexbase.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT corporate_event_template_to_corporate_unit FOREIGN KEY (corporate_unit_id)
        REFERENCES masterdata.corporate_unit (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT inheritance_mode_values 
		CHECK (inheritance_mode IN ('INHERITANCE_FORCED', 'INHERITANCE_OPTIONAL','INHERITANCE_FORBIDDEN')),
	CONSTRAINT event_type_values 
		CHECK (event_type IN ('EVENTTYPE_CALCULATED', 'EVENTTYPE_INTERNAL','EVENTTYPE_EXTERNAL')),
	CONSTRAINT event_scope_values 
		CHECK (event_scope IN ('EVENTSCOPE_CALCULATED', 'EVENTSCOPE_FOOD_BEVERAGE','EVENTSCOPE_ENTERTAINMENT'))
);


CREATE TABLE IF NOT EXISTS events.corporate_event_used
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('events.corporate_event_used_id_seq'::regclass),
	corporate_event_template_id bigint NOT NULL,
	platform_id bigint not NULL,
	event_date date not NULL,
	event_yearmonth integer GENERATED ALWAYS as ((extract (year from event_date))* 100 + (extract (month from event_date))) STORED,
	event_year integer GENERATED ALWAYS as (extract (year from event_date)) STORED,
	CONSTRAINT corporate_event_used_pkey PRIMARY KEY (id),
	CONSTRAINT corporate_event_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT corporate_event_used_template FOREIGN KEY (corporate_event_template_id)
        REFERENCES events.corporate_event_templates (id) MATCH SIMPLE
        ON UPDATE NO ACTION
);
	