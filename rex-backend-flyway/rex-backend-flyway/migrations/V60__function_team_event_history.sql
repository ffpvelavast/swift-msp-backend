DROP FUNCTION IF EXISTS events.team_event_history(bigint, character);

CREATE OR REPLACE FUNCTION events.team_event_history(current_teamevent_id bigint, user_locale character)
    RETURNS TABLE(created_at timestamp(6) with time zone, actor_fullname citext, actor_listname citext, displaytext text, active boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	
	SELECT 
		teh.created_at,
		ru.fullname,
		ru.listname,
		tes.display_text ->> lower(user_locale) as displaytext,
		teh.active
		FROM events.team_event_history teh 
			INNER join events.team_event_status tes ON tes.id = teh.status_id
			INNER join rexbase.rexuser ru ON ru.id = teh.creator_id
			WHERE teh.teamevent_id=current_teamevent_id
			ORDER BY teh.created_at;
END;
$BODY$;