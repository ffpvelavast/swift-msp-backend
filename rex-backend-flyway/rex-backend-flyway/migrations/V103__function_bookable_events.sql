DROP FUNCTION IF EXISTS events.bookable_events(bigint);

CREATE OR REPLACE FUNCTION events.bookable_events(
    current_rexuser_id bigint)
    RETURNS TABLE(event_source text, sortid integer, event_id bigint, event_title citext, inherit_participants boolean)
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE
    ROWS 1000

AS $BODY$


BEGIN
    RETURN QUERY

    --- returns all events eligible for booking by the current user ---
    WITH corporate_ev AS
        (
             SELECT 'CORPORATE_EVENT'::text AS event_source,
                corporate_event.id AS event_id,
                corporate_event.sortid,
                corporate_event.event_title,
                case
                    when corporate_event.inheritance_mode = 'INHERITANCE_FORCED' then true
                    when corporate_event.inheritance_mode = 'INHERITANCE_OPTIONAL' then false
                    when corporate_event.inheritance_mode = 'INHERITANCE_FORBIDDEN' then false
                    else
                        false
                end as inherit_participants
                   FROM events.corporate_event
                  WHERE corporate_event.booking_released_at IS NOT NULL
                          AND corporate_event.processing_released_at IS NULL
                        AND corporate_event.bookable_period @> CURRENT_DATE
        ),
        team_ev AS
        (
            SELECT  DISTINCT
                'TEAM_EVENT' AS event_source,
                 team_event.id AS event_id,
                1::integer as sortid,
                team_event.event_title,
                false as inherit_participants
                   FROM events.team_event
            Left JOIN rexbase.picklist_to_beneficiary plben ON plben.picklist_id = team_event.picklist_id
               where team_event.bookable_through >= CURRENT_DATE
                     AND (plben.beneficiary_id=current_rexuser_id OR team_event.rexuser_id=current_rexuser_id)
        )

    --- UNION in postgres applies implicitly a 'distinct' on the selection
    --- UNION ALL does not but is by far quicker (no internal hashing necessary)
    --- since we have a DISTINCT on team_events select statement and there is no overlap
    --- between corporate_event and team_event, we opted fro the performance gain :-)
    SELECT ce.event_source, ce.sortid, ce.event_id, ce.event_title, ce.inherit_participants FROM corporate_ev ce
         UNION ALL
    SELECT te.event_source, te.sortid, te.event_id, te.event_title, te.inherit_participants FROM team_ev te

    ORDER BY event_source DESC,sortid NULLS LAST, event_title;
END;
$BODY$;
