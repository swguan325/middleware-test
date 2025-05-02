package swguan.middleware.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserRequestVO {
	
	private int seqNbr;
	private String email;
	private String pswd;

}
