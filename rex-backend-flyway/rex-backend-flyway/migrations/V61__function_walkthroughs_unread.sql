DROP FUNCTION IF EXISTS rexbase.walkthroughs_unread(bigint, bigint, character);

CREATE OR REPLACE FUNCTION rexbase.walkthroughs_unread(
	current_platform_id bigint,
	current_rexuser_id bigint,
	locale character)
    RETURNS TABLE(walkthrough_id bigint, sortid bigint, internal_title text, 
					title text, subtitle text, description text, picture_file text) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	
	--- select all walkthroughs from the current platform, 
	--- which are active and which have not been marked as read by the user
	SELECT 
		wth.id as walkthrough_id,
		wth.sortid,
		wth.internal_title,
		wth.title ->> lower(locale) as title,
		wth.subtitle ->> lower(locale) as subtitle,
		wth.description ->> lower(locale) as description,
		wth.picture_file
	FROM rexbase.walkthroughs wth

		WHERE (wth.platform_id=current_platform_id AND wth.activeperiod @> CURRENT_DATE)
			AND wth.id NOT IN 
			(SELECT wthr.walkthrough_id 
			FROM rexbase.walkthroughs_read wthr where wthr.rexuser_id=current_rexuser_id)
			ORDER BY Sortid;
END;
$BODY$;