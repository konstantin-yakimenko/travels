package ru.travels;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.NotFoundException;
import ru.travels.model.Location;
import ru.travels.model.User;
import ru.travels.model.UserVisits;
import ru.travels.model.Visit;
import ru.travels.services.LocationService;
import ru.travels.services.UserService;
import ru.travels.services.VisitService;

import java.util.Map;
import java.util.Objects;

import static ru.travels.utils.Utils.castToInt;
import static ru.travels.utils.Utils.toIntEx;
import static ru.travels.utils.Utils.toLongEx;

public class UserController
{
	private final UserService userService;
	private final VisitService visitService;
	private final LocationService locationService;

	private final Empty empty = new Empty();

	public UserController(UserService userService, VisitService visitService, LocationService locationService) {
		super();
		this.userService = userService;
		this.visitService = visitService;
		this.locationService = locationService;
	}

	public User getUser(Request request, Response response) {
		String userId = request.getHeader(Constants.Url.USER_ID);
		if (userId == null) {
			throw new NotFoundException();
		}
		User user = userService.get(castToInt(userId));
		if (user == null) {
			throw new NotFoundException();
		}
		return user;
	}

	public Location getLocation(Request request, Response response) {
		String locationId = request.getHeader(Constants.Url.LOCATION_ID);
		if (locationId == null) {
			throw new NotFoundException();
		}
		Location location = locationService.get(castToInt(locationId));
		if (location == null) {
			throw new NotFoundException();
		}
		return location;
	}

	public Visit getVisit(Request request, Response response) {
		String visitId = request.getHeader(Constants.Url.VISIT_ID);
		if (visitId == null) {
			throw new NotFoundException();
		}
		Visit visit = visitService.get(castToInt(visitId));
		if (visit == null) {
			throw new NotFoundException();
		}
		return visit;
	}

	public UserVisits userVisits(Request request, Response response) {
		String userId = request.getHeader(Constants.Url.USER_ID);
		String country = request.getHeader("country");
		String toDistance = request.getHeader("toDistance");
		String fromDate = request.getHeader("fromDate");
		String toDate = request.getHeader("toDate");

		if (userId == null) {
			throw new NotFoundException();
		}
		return userService.getVisits(castToInt(userId),
				toLongEx(fromDate),
				toLongEx(toDate),
				country,
				toIntEx(toDistance));
	}

	public Avg getAvg(Request request, Response response) {
		String locationId = request.getHeader(Constants.Url.LOCATION_ID);
		String fromDate = request.getHeader("fromDate");
		String toDate = request.getHeader("toDate");
		String fromAge = request.getHeader("fromAge");
		String toAge = request.getHeader("toAge");
		String gender = request.getHeader("gender");

		if (locationId == null) {
			throw new NotFoundException();
		}
		return new Avg(locationService.getAvg(castToInt(locationId),
				toLongEx(fromDate),
				toLongEx(toDate),
				toLongEx(fromAge),
				toLongEx(toAge),
				gender));
	}

	public User getUserNull(Request request, Response response) {
		throw new NotFoundException();
	}

	public Location getLocationNull(Request request, Response response) {
		throw new NotFoundException();
	}

	public Visit getVisitNull(Request request, Response response) {
		throw new NotFoundException();
	}

	public UserVisits userVisit(Request request, Response response) {
		throw new NotFoundException();
	}


	public Empty setUser(Request request, Response response) {
		String userId = request.getHeader(Constants.Url.USER_ID);
		Map<String, Object> user = (Map<String, Object>)request.getBodyAs(Map.class, "User not provided");
		if (Objects.equals(userId, "new")) {
			userService.add(user);
		} else {
			userService.update(castToInt(userId), user);
		}
		return empty;
	}

	public Empty setLocation(Request request, Response response) {
		String locationId = request.getHeader(Constants.Url.LOCATION_ID);
		Map<String, Object> location = (Map<String, Object>)request.getBodyAs(Map.class, "Location not provided");
		if (Objects.equals(locationId, "new")) {
			locationService.add(location);
		} else {
			locationService.update(castToInt(locationId), location);
		}
		return empty;
	}

	public Empty setVisit(Request request, Response response) {
		String visitId = request.getHeader(Constants.Url.VISIT_ID);
		Map<String, Object> visit = (Map<String, Object>)request.getBodyAs(Map.class, "Visit not provided");
		if (Objects.equals(visitId, "new")) {
			visitService.add(visit);
		} else {
			visitService.update(castToInt(visitId), visit);
		}
		return empty;
	}

}

@JsonSerialize
class Empty {}

@JsonSerialize
class Avg {
	private final Double avg;

	public Avg(Double avg) {
		this.avg = avg;
	}

	public Double getAvg() {
		return avg;
	}
}
