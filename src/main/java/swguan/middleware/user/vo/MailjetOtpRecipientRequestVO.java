package swguan.middleware.user.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MailjetOtpRecipientRequestVO {

	@JsonProperty("Email")
	private String email;

}
