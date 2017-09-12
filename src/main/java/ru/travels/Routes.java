package ru.travels;

import io.netty.handler.codec.http.HttpMethod;
import org.restexpress.RestExpress;

public abstract class Routes
{
	public static void define(Configuration config, RestExpress server)
    {
		server.uri("/users/{userId}", config.getUserController())
				.action("getUser", HttpMethod.GET)
				.name(Constants.Routes.USER_GET);

		server.uri("/locations/{locationId}", config.getUserController())
				.action("getLocation", HttpMethod.GET)
				.name(Constants.Routes.LOCATION_GET);

		server.uri("/visits/{visitId}", config.getUserController())
				.action("getVisit", HttpMethod.GET)
				.name(Constants.Routes.VISIT_GET);

		server.uri("/users/{userId}/visits", config.getUserController())
				.action("userVisits", HttpMethod.GET)
				.name(Constants.Routes.USER_VISITS);

		server.uri("/locations/{locationId}/avg", config.getUserController())
				.action("getAvg", HttpMethod.GET)
				.name(Constants.Routes.LOCATION_AVG);

		server.uri("/user", config.getUserController())
				.action("getUserNull", HttpMethod.GET)
				.name(Constants.Routes.USER_NULL);

		server.uri("/location", config.getUserController())
				.action("getLocationNull", HttpMethod.GET)
				.name(Constants.Routes.LOCATION_NULL);

		server.uri("/visit", config.getUserController())
				.action("getVisitNull", HttpMethod.GET)
				.name(Constants.Routes.VISIT_NULL);

		server.uri("/users/{userId}/visit", config.getUserController())
				.action("userVisit", HttpMethod.GET)
				.name(Constants.Routes.USER_VISIT);

		server.uri("/users/{userId}", config.getUserController())
				.action("setUser", HttpMethod.POST)
				.name(Constants.Routes.USER_UPDATE);

		server.uri("/locations/{locationId}", config.getUserController())
				.action("setLocation", HttpMethod.POST)
				.name(Constants.Routes.LOCATION_UPDATE);

		server.uri("/visits/{visitId}", config.getUserController())
				.action("setVisit", HttpMethod.POST)
				.name(Constants.Routes.VISIT_UPDATE);

	}
}
