------------------------------------------------------------------
---------------- Views for reports views -------------------------
------------------------------------------------------------------

DROP VIEW IF EXISTS taxation.taxation_nonevents_client;
DROP VIEW IF EXISTS reports.team_event_types;
DROP VIEW IF EXISTS reports.corporate_strucure;
DROP VIEW IF EXISTS reports.corporate_event_dropdowns;

------------------------------------------------------------------

CREATE OR REPLACE VIEW reports.taxation_nonevents_client
 AS
 SELECT taxcat.id AS taxcategory_id,
    taxcli.id AS taxreason_id,
    taxcat.sortid AS cat_sortid,
    taxcat.platform_id,
    plt.displayname AS platform,
	taxne.taxation_type,
	taxne.taxation_type_display,
    taxcat.display_text ->> 'de'::text AS cat_display_text_de,
    taxcat.display_text ->> 'en'::text AS cat_display_text_en,
    taxcat.help_text ->> 'de'::text AS cat_help_text_de,
    taxcat.help_text ->> 'en'::text AS cat_help_text_en,
    taxcat.description ->> 'de'::text AS cat_descripton_de,
    taxcat.description ->> 'en'::text AS cat_descripton_en,
    taxcat.eligible_invoice AS cat_eligible_invoice,
    taxcat.eligible_receipt AS cat_eligible_receipt,
    taxcli.sortid AS cli_sortid,
    taxcli.display_text ->> 'de'::text AS cli_display_text_de,
    taxcli.display_text ->> 'en'::text AS cli_display_text_en,
    taxcli.help_text ->> 'de'::text AS cli_help_text_de,
    taxcli.help_text ->> 'en'::text AS cli_help_text_en,
    taxcli.description ->> 'de'::text AS cli_descripton_de,
    taxcli.description ->> 'en'::text AS cli_descripton_en,
    taxcli.eligible_invoice AS cli_eligible_invoice,
    taxcli.eligible_receipt AS cli_eligible_receipt,
    taxcli.permitted_value
   FROM taxation.taxcategory_client taxcat
     JOIN taxation.taxreason_client taxcli ON taxcli.taxcategory_client_id = taxcat.id
	 LEFT JOIN masterdata.taxation_nonevent taxne ON taxne.taxation_type = taxcli.taxation_type_nonevent
     JOIN rexbase.platform plt ON plt.id = taxcat.platform_id
  ORDER BY taxcat.sortid, taxcli.sortid;

------------------------------------------------------------------

CREATE OR REPLACE VIEW reports.team_event_types
 AS
 SELECT 
 	plt.displayname as platform,
 	tevt.platform_id,
    tevt.sortid,
    tevt.display_text ->> 'de'::text AS displaytext_de,
    tevt.display_text ->> 'en'::text AS displaytext_en,
    tevt.description ->> 'de'::text AS description_de,
    tevt.description ->> 'en'::text AS description_en,
    tevt.apply_law40_2,
    tevt.active
   FROM events.team_event_type tevt
   JOIN rexbase.platform plt ON plt.id = tevt.platform_id
  ORDER BY tevt.sortid;

----------------------------------------------------------------------

CREATE OR REPLACE VIEW reports.corporate_strucure
 AS
 SELECT cdiv.platform_id,
    plt.displayname AS platform,
    cdiv.division_name ->> 'de'::text AS division_name_de,
    cdiv.division_name ->> 'en'::text AS division_name_en,
    cunit.unit_name ->> 'de'::text AS unit_name_de,
    cunit.unit_name ->> 'en'::text AS unit_name_en,
    cdiv.id as division_id,
	cunit.id as unit_id,
	cdiv.sortid AS division_sort,
    cunit.sortid AS unit_sort,
    cdiv.activeperiod @> CURRENT_DATE AS division_active,
    cunit.activeperiod @> CURRENT_DATE AS unit_active
   FROM masterdata.corporate_division cdiv
     JOIN masterdata.corporate_unit cunit ON cunit.corporate_division_id = cdiv.id
     JOIN rexbase.platform plt ON plt.id = cdiv.platform_id
  ORDER BY cdiv.sortid, cunit.sortid;
  
----------------------------------------------------------------------
 
CREATE OR REPLACE VIEW reports.corporate_event_dropdowns
 AS
 SELECT screen_code::varchar(25),
    listbox_code::varchar(25),
    listbox_enum::varchar(25),
    sortid,
    display_text ->> 'de'::text AS displaytext_de,
    display_text ->> 'en'::text AS displaytext_en,
    description ->> 'de'::text AS description_de,
    description ->> 'en'::text AS description_en
   FROM rexbase.listboxes
  ORDER BY screen_code, listbox_code, sortid;

