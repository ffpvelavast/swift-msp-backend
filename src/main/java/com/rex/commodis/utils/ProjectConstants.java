/** ProjectConstants: part of the REX multi-tenant expense management platform. */
package com.rex.commodis.utils;

import java.util.Locale;

/**
 * Created on Ağustos, 2020
 *
 * @author Faruk
 */
public final class ProjectConstants {

	// FIXME : Customize project constants for your application.

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final Locale TURKISH_LOCALE = new Locale.Builder().setLanguage("tr").setRegion("TR").build();

	public static final String CORPORATE_EVENT = "CORPORATEEVENT";

	public static final String TEAM_EVENT = "TEAMEVENT";

	private ProjectConstants() {

		throw new UnsupportedOperationException();
	}

}
