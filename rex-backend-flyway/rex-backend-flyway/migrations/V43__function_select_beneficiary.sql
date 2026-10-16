DROP FUNCTION IF EXISTS rexbase.select_beneficiary(bigint);

CREATE OR REPLACE FUNCTION rexbase.select_beneficiary(current_platform_id bigint)
    RETURNS TABLE(beneficiary_id bigint, listname citext, fullname citext, identity_tag citext,employer citext,orgunit text,costcenter citext,companysite citext) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY

	--- select all beneficiaries = employees of the companies, which have an actice workcontract
	--- OR have left the company not longer ago then three months in the past
	--- the employees are shown with their CURRENT organisational details
	Select 
		ben.id as beneficiary_id,
		ben.listname,
		ben.fullname,
		borg.identity_tag,
		emp.displayname as employer,
		borg.orgunit,
		borg.costcenter,
		borg.companysite
	from rexbase.beneficiary ben
	JOIN rexbase.beneficiary_orgdata borg on borg.beneficiary_id = ben.id
	JOIN rexbase.employer emp on emp.id = borg.employer_id
	where (coalesce(leavingdate,'2999-12-31') +  INTERVAL '3 month') >= CURRENT_DATE
			AND borg.validperiod @> CURRENT_DATE
			AND ben.platform_id=current_platform_id;
END;
$BODY$;


