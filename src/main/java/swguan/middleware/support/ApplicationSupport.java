
package swguan.middleware.support;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

import java.util.Optional;

import org.springframework.boot.SpringApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationSupport {
	private static final String PROFILE_LOCAL = "local";

	static final String CONFIG_PROFILE = "profile";
	static final String PROFILE_DEFAULT = PROFILE_LOCAL;

	private static boolean configuredProfile = false;

	ApplicationSupport() {
	}

	public static void configureProfile() {
		if (!configuredProfile) {
			getActiveProfile();
			configuredProfile = true;
		}
	}

	public static <T> void run(Class<T> clazz, String[] args) {
		configureProfile();
		SpringApplication.run(clazz, args);
	}

	static void clearProfile() {
		configuredProfile = false;
	}

	private static String getActiveProfile() {
		final String activeProfile = Optional.ofNullable(System.getProperty(CONFIG_PROFILE)).orElseGet(() -> Optional
				.ofNullable(System.getProperty(ACTIVE_PROFILES_PROPERTY_NAME)).orElseGet(() -> PROFILE_DEFAULT));
		log.info("Current profile is {}", activeProfile);
		System.setProperty(CONFIG_PROFILE, activeProfile);
		System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, activeProfile);
		return activeProfile;
	}
}
