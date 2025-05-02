package swguan.middleware.user.transformer;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import swguan.middleware.user.dto.UserLoginResponseDTO;
import swguan.middleware.user.dto.UserRequestDTO;
import swguan.middleware.user.dto.UserResponseDTO;
import swguan.middleware.user.vo.MailjetOtpRecipientRequestVO;
import swguan.middleware.user.vo.MailjetOtpRequestVO;
import swguan.middleware.user.vo.UserLoginResponseVO;
import swguan.middleware.user.vo.UserOtpRequestVO;
import swguan.middleware.user.vo.UserRequestVO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTransformer {

	public static UserRequestVO transform(UserRequestDTO reqDto) {
		return UserRequestVO.builder().email(reqDto.getEmail()).build();
	}

	public static UserResponseDTO transform(String token) {
		return UserResponseDTO.builder().token(token).build();
	}

	public static UserOtpRequestVO transform(String email, String otp) {
		return UserOtpRequestVO.builder().email(email).otp(otp).build();
	}

	public static UserLoginResponseDTO transform(UserLoginResponseVO resLoginVo) {
		return UserLoginResponseDTO.builder().pswdAuthDt(resLoginVo.getPswdAuthDt())
				.emailAuthDt(resLoginVo.getEmailAuthDt()).build();
	}

	public static MailjetOtpRequestVO transformOtp(String email, String otpNbr) {
		final MailjetOtpRecipientRequestVO recipient = MailjetOtpRecipientRequestVO.builder().email(email).build();
		final List<MailjetOtpRecipientRequestVO> recipients = new ArrayList<>();
		recipients.add(recipient);
		return MailjetOtpRequestVO.builder().textPart("驗證碼：" + otpNbr).recipients(recipients).build();
	}

}
