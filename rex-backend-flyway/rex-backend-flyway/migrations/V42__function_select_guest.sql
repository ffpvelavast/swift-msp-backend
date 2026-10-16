DROP FUNCTION IF EXISTS rexbase.select_guests(bigint, bigint, character);

CREATE OR REPLACE FUNCTION rexbase.select_guests(
	current_platform_id bigint,
	current_rexuser_id bigint,
	locale character)
    RETURNS TABLE(guest_id bigint, organisationwide_guest boolean, listname citext, fullname citext, organization citext, guest_enum text, guest_type text) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	
	--- select all guests accessible by the current user
	--- that is organisationwide_guest OR guest created by the current user 
	SELECT 
		guest.id AS guest_id,
		guest.organisationwide_guest,
		guest.listname,
		guest.fullname,
		guest.organization,
		gtype.guest_enum,
		gtype.display_text ->> lower(locale)
		FROM rexbase.guest guest
		JOIN rexbase.guest_type gtype ON gtype.id = guest.default_guesttype_id
		Where guest.platform_id=current_platform_id AND (guest.organisationwide_guest OR creator_rexuser_id=current_rexuser_id);

END;
$BODY$;