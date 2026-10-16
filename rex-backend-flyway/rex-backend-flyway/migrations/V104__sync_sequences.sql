-- 1. Beneficiary
ALTER SEQUENCE rexbase.beneficiary_id_seq INCREMENT BY 1;
SELECT setval('rexbase.beneficiary_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.beneficiary), false);

-- 2. Beneficiary Org Data
ALTER SEQUENCE rexbase.beneficiary_orgdata_id_seq INCREMENT BY 1;
SELECT setval('rexbase.beneficiary_orgdata_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.beneficiary_orgdata), false);


-- 3. Employer
ALTER SEQUENCE rexbase.employer_id_seq INCREMENT BY 1;
SELECT setval('rexbase.employer_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.employer), false);

-- 4. Favorite Beneficiary
ALTER SEQUENCE rexbase.beneficiary_favorites_id_seq INCREMENT BY 1;
SELECT setval('rexbase.beneficiary_favorites_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.beneficiary_favorites), false);

-- 5. Guest
ALTER SEQUENCE rexbase.guest_id_seq INCREMENT BY 1;
SELECT setval('rexbase.guest_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.guest), false);

-- 6. Picklist
ALTER SEQUENCE rexbase.picklist_id_seq INCREMENT BY 1;
SELECT setval('rexbase.picklist_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.picklist), false);

-- 7. Platform
ALTER SEQUENCE rexbase.platform_id_seq INCREMENT BY 1;
SELECT setval('rexbase.platform_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.platform), false);

-- 8. Platform Domain
ALTER SEQUENCE rexbase.platform_domain_id_seq INCREMENT BY 1;
SELECT setval('rexbase.platform_domain_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.platform_domain), false);

-- 9. RexUser
ALTER SEQUENCE rexbase.rexuser_id_seq INCREMENT BY 1;
SELECT setval('rexbase.rexuser_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.rexuser), false);


-- 10. User Right
ALTER SEQUENCE rexbase.userright_id_seq INCREMENT BY 1;
SELECT setval('rexbase.userright_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.userright), false);

-- 11. User Role
ALTER SEQUENCE rexbase.userrole_id_seq INCREMENT BY 1;
SELECT setval('rexbase.userrole_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM rexbase.userrole), false);
