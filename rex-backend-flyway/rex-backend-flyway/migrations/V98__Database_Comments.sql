----------------------------------------------------------------------------
------- Adding comments to fields that are not easy to understand ----------
----------------------------------------------------------------------------

-- COMMENT ON TABLE <schema>.<table> IS 'my comment';
-- COMMENT ON COLUMN <schema>.<table>.<column> IS 'my comment';


COMMENT ON COLUMN rexbase.platform_configuration.corporate_event_preview 
				is 'the number of month that a corporate event template is visible in create event dropdown prior to the assumed eventdate 
					(generated column *event_date* in table *events.corporate_event_templates*)'