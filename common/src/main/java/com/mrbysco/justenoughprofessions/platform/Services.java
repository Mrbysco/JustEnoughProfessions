package com.mrbysco.justenoughprofessions.platform;

import com.mrbysco.justenoughprofessions.Constants;
import com.mrbysco.justenoughprofessions.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * This class is used to load services from the service loader
 */
public class Services {

	/**
	 * The platform helper service
	 */
	public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

	/**
	 * Load a service from the service loader
	 * @param clazz The class of the service to load
	 * @param <T> The type of the service
	 * @return The loaded service
	 */
	public static <T> T load(Class<T> clazz) {

		final T loadedService = ServiceLoader.load(clazz)
				.findFirst()
				.orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
		Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
		return loadedService;
	}
}
