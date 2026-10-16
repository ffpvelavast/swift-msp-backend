------------------------------ select function for tax_reasons -------------------------
DROP FUNCTION IF EXISTS taxation.select_taxreason(bigint, character);

CREATE OR REPLACE FUNCTION taxation.select_taxreason(
    current_taxcategory_id bigint,
    locale character)
    RETURNS TABLE(taxreason_id bigint, displaytext text, helptext text, description text, taxation_type_nonevent text, allow_beneficary boolean, allow_guest boolean, sortid smallint)
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
    tnev.taxation_type,
    tnev.allow_beneficary,
    tnev.allow_guest,
    taxcr.sortid
    from taxation.taxreason_client taxcr
    JOIN masterdata.taxation_nonevent tnev ON tnev.taxation_type = taxcr.taxation_type_nonevent
    where taxcr.activeperiod @> CURRENT_DATE and taxcr.taxcategory_client_id=current_taxcategory_id
    order by sortid;
END;
$BODY$;
