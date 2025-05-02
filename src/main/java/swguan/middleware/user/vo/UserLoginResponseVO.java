package swguan.middleware.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginResponseVO {

	private int seqNbr;
	private String pswdAuthDt;
	private String emailAuthDt;
	
}
