package ru.travels;

public class Constants
{
	/**
	 * These define the URL parmaeters used in the route definition strings (e.g. '{userId}').
	 */
	public class Url
	{
		public static final String USER_ID = "userId";
		public static final String LOCATION_ID = "locationId";
		public static final String VISIT_ID = "visitId";
	}

	/**
	 * These define the route names used in naming each route definitions.  These names are used
	 * to retrieve URL patterns within the controllers by name to create links in responses.
	 */
	public class Routes
	{
		public static final String USER_GET = "users.get";
		public static final String LOCATION_GET = "location.get";
		public static final String VISIT_GET = "visit.get";

		public static final String USER_VISITS = "users.visits";
		public static final String LOCATION_AVG = "location.avg";

		public static final String USER_NULL = "user.null";
		public static final String LOCATION_NULL = "location.null";
		public static final String VISIT_NULL = "visit.null";
		public static final String USER_VISIT = "user.visit";
		
		public static final String USER_UPDATE = "user.update";
		public static final String LOCATION_UPDATE = "location.update";
		public static final String VISIT_UPDATE = "visit.update";

		public static final String USERS_ADD = "user.add";
		public static final String LOCATIONS_ADD = "location.add";
		public static final String VISIT_ADD = "visit.add";
    }
}
