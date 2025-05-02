
package swguan.middleware.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequestDTO {

	@NotBlank
	@Size(min = 6, max = 50)
	private String email;

	@NotBlank
	private String pswd;

}
