ALTER TABLE masterdata.taxation_nonevent
    ADD COLUMN IF NOT EXISTS allow_beneficary boolean NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS allow_guest boolean NOT NULL DEFAULT TRUE;
