DROP FUNCTION IF EXISTS events.select_team_eventtype(bigint, character);

CREATE OR REPLACE FUNCTION events.select_team_eventtype(
	current_platform_id bigint,
	user_locale character)
    RETURNS TABLE(team_event_type_id bigint, displaytext text, description text) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	
	SELECT 
		tety.id as team_event_type_id,
		tety.display_text ->> lower(user_locale),
		tety.description ->> lower(user_locale)
		FROM events.team_event_type tety
		Where active AND tety.platform_id = current_platform_id
		ORDER BY tety.sortid;
END;
$BODY$;