DROP SEQUENCE IF EXISTS rexbase.beneficiary_favorites_id_seq;

CREATE SEQUENCE rexbase.beneficiary_favorites_id_seq;


-- CLEANUP: Remove old 1-parameter versions to avoid overloading confusion
DROP FUNCTION IF EXISTS rexbase.picklist_members(bigint);
DROP FUNCTION IF EXISTS rexbase.select_beneficiary(bigint);


-- 3. UNIFIED SEARCH
DROP FUNCTION IF EXISTS rexbase.search_members(text, bigint, bigint, integer);
CREATE OR REPLACE FUNCTION rexbase.search_members(search_text text, p_user_id bigint, p_platform_id bigint, p_limit integer DEFAULT 50)
RETURNS TABLE(member_type text, member_id bigint, listname citext, companyname citext, identitytag text, isfavorite boolean) LANGUAGE 'plpgsql' AS $BODY$
BEGIN
    RETURN QUERY
    WITH combo AS (
        SELECT 'BENEFICIARY'::text as m_type, b.id as mid, b.listname as name, e.displayname::citext as comp, org.identity_tag::text as tag,
        (EXISTS (SELECT 1 FROM rexbase.beneficiary_favorites f WHERE f.beneficiary_id = b.id AND f.rexuser_id = p_user_id)) as fav
        FROM rexbase.beneficiary b
        JOIN rexbase.beneficiary_orgdata org ON org.beneficiary_id = b.id
        JOIN rexbase.employer e ON e.id = org.employer_id
        WHERE b.platform_id = p_platform_id AND org.validperiod @> CURRENT_DATE
        UNION ALL
        SELECT 'GUEST'::text, g.id, g.listname, g.organization, NULL, FALSE
        FROM rexbase.guest g WHERE g.platform_id = p_platform_id
    )
    SELECT * FROM combo
    WHERE (search_text IS NULL OR name ILIKE '%' || search_text || '%' OR comp ILIKE '%' || search_text || '%')
    LIMIT p_limit;
END; $BODY$;

-- 4. SELECT BENEFICIARY
DROP FUNCTION IF EXISTS rexbase.select_beneficiary(bigint, bigint);
CREATE OR REPLACE FUNCTION rexbase.select_beneficiary(p_platform_id bigint, p_rexuser_id bigint)
RETURNS TABLE(beneficiary_id bigint, listname citext, fullname citext, identity_tag citext, employer citext, orgunit text, costcenter citext, is_favorite boolean)
LANGUAGE 'plpgsql' AS $BODY$
BEGIN
    RETURN QUERY
    SELECT ben.id, ben.listname, ben.fullname, borg.identity_tag, emp.displayname::citext, borg.orgunit, borg.costcenter,
    (EXISTS (SELECT 1 FROM rexbase.beneficiary_favorites f WHERE f.beneficiary_id = ben.id AND f.rexuser_id = p_rexuser_id))
    FROM rexbase.beneficiary ben
    JOIN rexbase.beneficiary_orgdata borg ON borg.beneficiary_id = ben.id
    JOIN rexbase.employer emp ON emp.id = borg.employer_id
    WHERE (COALESCE(leavingdate,'2999-12-31') + INTERVAL '3 month') >= CURRENT_DATE AND borg.validperiod @> CURRENT_DATE AND ben.platform_id = p_platform_id;
END; $BODY$;

-- 5. PICKLIST MEMBERS (Matching PicklistMemberDto structure)
DROP FUNCTION IF EXISTS rexbase.picklist_members(bigint, bigint);
CREATE OR REPLACE FUNCTION rexbase.picklist_members(current_picklist_id bigint, p_rexuser_id bigint)
RETURNS TABLE(picklist_id bigint, member_type text, member_id bigint, is_favorite boolean, listname citext, companyname citext, identitytag text, orgunit text, costcenter citext)
LANGUAGE 'plpgsql' AS $BODY$
BEGIN
    RETURN QUERY
    SELECT current_picklist_id, 'BENEFICIARY'::text, bl.beneficiary_id,
    (EXISTS (SELECT 1 FROM rexbase.beneficiary_favorites f WHERE f.beneficiary_id = ben.id AND f.rexuser_id = p_rexuser_id)),
    ben.listname, emp.displayname::citext, borg.identity_tag::text, borg.orgunit, borg.costcenter
    FROM rexbase.picklist_to_beneficiary bl
    JOIN rexbase.beneficiary ben ON ben.id = bl.beneficiary_id
    JOIN rexbase.beneficiary_orgdata borg ON borg.beneficiary_id = ben.id
    JOIN rexbase.employer emp ON emp.id = borg.employer_id
    WHERE bl.picklist_id = current_picklist_id
    UNION ALL
    SELECT current_picklist_id, 'GUEST'::text, gl.guest_id, FALSE, gu.listname, gu.organization, NULL, NULL, NULL
    FROM rexbase.picklist_to_guest gl JOIN rexbase.guest gu ON gu.id = gl.guest_id
    WHERE gl.picklist_id = current_picklist_id;
END; $BODY$;

CREATE OR REPLACE FUNCTION rexbase.fn_login_user_profile(
    p_email text)
    RETURNS jsonb
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    result JSONB;
    v_locale TEXT;
BEGIN
    SELECT COALESCE(u.locale, 'de') INTO v_locale FROM rexbase.rexuser u WHERE LOWER(u.emailaddress) = LOWER(p_email);
    IF v_locale IS NULL THEN v_locale := 'de'; END IF;

    SELECT jsonb_build_object(
        'user', jsonb_build_object(
            'id', u.id,
            'firstname', u.firstname,
            'lastname', u.lastname,
            'fullName', u.fullname,
            'email', u.emailaddress,
            'academictitle', u.academictitle,
            'keycloakId', u.keycloakid,
            'platformId', u.platform_id,
            'locale', u.locale,
            'active', u.active
        ),
        'roles', (
            SELECT COALESCE(jsonb_agg(jsonb_build_object(
                'role', r.role_enum,
                'displayText', COALESCE(r.display_text ->> v_locale, r.role_enum)
            )), '[]'::jsonb)
            FROM rexbase.rexuser_roles r WHERE r.rexuser_id = u.id
        ),
        'rights', (
            SELECT COALESCE(jsonb_agg(jsonb_build_object(
                'right', ri.right_enum,
                'displayText', COALESCE(ri.display_text ->> v_locale, ri.right_enum)
            )), '[]'::jsonb)
            FROM rexbase.rexuser_rights ri WHERE ri.rexuser_id = u.id
        ),
        'favorites', (
            SELECT COALESCE(jsonb_agg(f.beneficiary_id), '[]'::jsonb)
            FROM rexbase.beneficiary_favorites f WHERE f.rexuser_id = u.id
        )
    ) INTO result
    FROM rexbase.rexuser u WHERE LOWER(u.emailaddress) = LOWER(p_email) AND u.activeperiod @> CURRENT_DATE;
    RETURN result;
END;
$BODY$;
