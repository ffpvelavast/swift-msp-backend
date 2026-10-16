DROP FUNCTION IF EXISTS rexbase.select_projectnumber(bigint, text, integer);

CREATE OR REPLACE FUNCTION rexbase.select_projectnumber(
    current_platform_id bigint,
    search_text text DEFAULT NULL,
    max_rows integer DEFAULT 50
)
RETURNS TABLE(
    projectnumber_id bigint,
    projectnumber text,
    projecttitle citext,
    projectsubtitle citext
)
LANGUAGE sql
STABLE
PARALLEL SAFE
AS $BODY$
    SELECT
        pn.id AS projectnumber_id,
        pn.projectnumber,
        pn.projecttitle,
        pn.projectsubtitle
    FROM rexbase.projectnumbers pn
    WHERE pn.platform_id = current_platform_id
      AND pn.activeperiod @> CURRENT_DATE
      AND (
            search_text IS NULL
         OR btrim(search_text) = ''
         OR pn.projectnumber ILIKE '%' || search_text || '%'
         OR pn.projecttitle::text ILIKE '%' || search_text || '%'
         OR pn.projectsubtitle::text ILIKE '%' || search_text || '%'
      )
    ORDER BY
      CASE
        WHEN search_text IS NULL OR btrim(search_text) = '' THEN 4
        WHEN pn.projectnumber ILIKE search_text || '%' THEN 1
        WHEN pn.projecttitle::text ILIKE search_text || '%' THEN 2
        WHEN pn.projectsubtitle::text ILIKE search_text || '%' THEN 3
        ELSE 4
      END,
      pn.projectnumber,
      pn.projecttitle
    LIMIT GREATEST(max_rows, 1);
$BODY$;