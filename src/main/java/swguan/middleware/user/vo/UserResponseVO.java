package swguan.middleware.user.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponseVO {

	private int seqNbr;
	private String pswd;;
	private String email;
	private Boolean isOpen;

}
