package ru.travels;

import org.restexpress.RestExpress;
import org.restexpress.util.Environment;
import ru.travels.repositories.LocationRepository;
import ru.travels.repositories.Tarantool;
import ru.travels.repositories.UserRepository;
import ru.travels.repositories.VisitRepository;
import ru.travels.services.LocationService;
import ru.travels.services.UserService;
import ru.travels.services.VisitService;
import ru.travels.utils.InitLoad;

import java.util.Properties;

public class Configuration
extends Environment
{
	private static final String DEFAULT_EXECUTOR_THREAD_POOL_SIZE = "20";

	private static final String PORT_PROPERTY = "port";
	private static final String BASE_URL_PROPERTY = "base.url";
	private static final String EXECUTOR_THREAD_POOL_SIZE = "executor.threadPool.size";

	private int port;
	private String baseUrl;
	private int executorThreadPoolSize;

	private UserController userController;
	private Tarantool tarantool;

	@Override
	protected void fillValues(Properties p)
	{
		this.port = Integer.parseInt(p.getProperty(PORT_PROPERTY, String.valueOf(RestExpress.DEFAULT_PORT)));
		this.baseUrl = p.getProperty(BASE_URL_PROPERTY, "http://localhost:" + String.valueOf(port));
		this.executorThreadPoolSize = Integer.parseInt(p.getProperty(EXECUTOR_THREAD_POOL_SIZE, DEFAULT_EXECUTOR_THREAD_POOL_SIZE));
		initialize();
	}

	private void initialize()
	{
		tarantool = new Tarantool("test", "test");
		UserRepository userRepository = new UserRepository(tarantool);
		VisitRepository visitRepository = new VisitRepository(tarantool);
		LocationRepository locationRepository = new LocationRepository(tarantool);
		userController = new UserController(
				new UserService(userRepository),
				new VisitService(visitRepository),
				new LocationService(locationRepository));
		new InitLoad(locationRepository, userRepository, visitRepository).loadArchData();
	}

	public int getPort()
	{
		return port;
	}
	
	public String getBaseUrl()
	{
		return baseUrl;
	}
	
	public int getExecutorThreadPoolSize()
	{
		return executorThreadPoolSize;
	}

	public UserController getUserController()
	{
		return userController;
	}
}
