package swguan.middleware.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailjetOtpSentResponseVO {
	
	private String Email;
	private String MessageID;
	private String MessageUUID;

}
