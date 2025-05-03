package swguan.middleware.user.util;

import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OtpUtil {

	private static final int OTP_LENGTH = 6;
	private static final String OTP_NUMS = "0123456789";

	public static char[] genOtp() {
		SecureRandom secureRandom = new SecureRandom();

		char[] otp = new char[OTP_LENGTH];

		for (int i = 0; i < OTP_LENGTH; i++) {
			otp[i] = OTP_NUMS.charAt(secureRandom.nextInt(OTP_NUMS.length()));
		}
		return otp;
	}

}
