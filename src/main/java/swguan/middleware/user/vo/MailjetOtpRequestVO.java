package swguan.middleware.user.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MailjetOtpRequestVO {

	@Builder.Default
	@JsonProperty("FromEmail")
	private String fromEmail = "swguan325@gmail.com";
	
	@Builder.Default
	@JsonProperty("FromName")
	private String fromName = "swguan";

	@JsonProperty("Recipients")
	private List<MailjetOtpRecipientRequestVO> recipients;
	
	@Builder.Default
	@JsonProperty("Subject")
	private String subject = "請進行二階段認證";
	
	@JsonProperty("Text-part")
	private String textPart;

}
