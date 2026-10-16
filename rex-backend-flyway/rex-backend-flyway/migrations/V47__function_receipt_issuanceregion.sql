DROP FUNCTION IF EXISTS receipts.select_receipt_issuanceregion(bigint);

CREATE OR REPLACE FUNCTION receipts.select_receipt_issuanceregion(current_platform_id bigint, locale character)

    RETURNS TABLE(issuance_region_id smallint, region_code text, displaytext text, description text) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY

	-- Returns all issuance_regions in preferred locale (with option to later extend to platform_settings)
	SELECT 
		ir.id as issuance_region_id,
		ir.region_code, 
		ir.display_text ->> locale, 
		ir.description ->> locale
	FROM receipts.issuance_region ir order by id;
END;
$BODY$;