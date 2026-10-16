DROP FUNCTION IF EXISTS rexbase.picklist_members(bigint);

CREATE OR REPLACE FUNCTION rexbase.picklist_members(current_picklist_id bigint)
    RETURNS TABLE(member_type text, member_id bigint,listname citext, companyname citext) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY

	WITH beneficiary_list AS 
		(
		SELECT 
        	'BENEFICIARY'::text AS member_type,
            bl.beneficiary_id as member_id,
			ben.listname,
            emp.displayname AS companyname
           FROM rexbase.picklist_to_beneficiary bl
           	JOIN rexbase.beneficiary ben ON ben.id = bl.beneficiary_id
             JOIN rexbase.beneficiary_orgdata borg ON borg.beneficiary_id = ben.id
             JOIN rexbase.employer emp ON emp.id = borg.employer_id
          WHERE borg.validperiod @> CURRENT_DATE AND bl.picklist_id = current_picklist_id
		),
		guest_list AS 
		(
         SELECT gl.picklist_id,
            'GUEST'::text AS member_type,
            gu.id as member_id,
			gu.listname,
            gu.organization AS companyname
           FROM rexbase.picklist_to_guest gl
             JOIN rexbase.guest gu ON gu.id = gl.guest_id
		   WHERE gl.picklist_id = current_picklist_id
        )
	
	SELECT ben.member_type, ben.member_id, ben.listname, ben.companyname FROM beneficiary_list ben
		UNION ALL
	SELECT gst.member_type, gst.member_id, gst.listname, gst.companyname FROM guest_list gst;
	
END;
$BODY$;




