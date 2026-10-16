
------------------------------ select function for tax_categories -------------------------

DROP FUNCTION IF EXISTS taxation.select_taxcategory(bigint,character);

CREATE OR REPLACE FUNCTION taxation.select_taxcategory(current_platform_id bigint,locale character)

    RETURNS TABLE(taxcategory_id bigint, displaytext text,helptext text, description text, sortid smallint) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY

	--- select all ACTIVE taxation categories of the current PLATFORM
	Select 
	taxcc.id as taxcategory_id,
	taxcc.display_text ->> lower(locale) as displaytext,
	taxcc.help_text ->> lower(locale) as helptext,
	taxcc.description ->> lower(locale) as description,
	taxcc.sortid
	from taxation.taxcategory_client taxcc
	where taxcc.activeperiod @> CURRENT_DATE and taxcc.platform_id=current_platform_id
	order by sortid;
	
END;
$BODY$;

------------------------------ select function for tax_reasons -------------------------
DROP FUNCTION IF EXISTS taxation.select_taxreason(bigint,character);

CREATE OR REPLACE FUNCTION taxation.select_taxreason(current_taxcategory_id bigint, locale character)

    RETURNS TABLE(taxreason_id bigint, displaytext text,helptext text, description text, taxation_rule_id bigint, sortid smallint) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY

	--- select all ACTIVE taxation reasons assigned to the current taxcategory
	Select 
	taxcr.id as taxreason_id,
	taxcr.display_text ->> lower(locale) as displaytext,
	taxcr.help_text ->> lower(locale) as helptext,
	taxcr.description ->> lower(locale) as description,
	taxcr.taxation_rule_id,
	taxcr.sortid
	from taxation.taxreason_client taxcr
	where taxcr.activeperiod @> CURRENT_DATE and taxcr.taxcategory_client_id=current_taxcategory_id
	order by sortid;
	
END;
$BODY$;

