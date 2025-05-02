package swguan.middleware.user.util;

import java.security.SecureRandom;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OtpUtil {

	private static final int OTP_LENGTH = 6;
	private static final String OTP_NUMS = "0123456789";

	public static char[] genOtp() {
		SecureRandom rndm_method = new SecureRandom();

		char[] otp = new char[OTP_LENGTH];

		for (int i = 0; i < OTP_LENGTH; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp[i] = OTP_NUMS.charAt(rndm_method.nextInt(OTP_NUMS.length()));
		}
		return otp;
	}

}
