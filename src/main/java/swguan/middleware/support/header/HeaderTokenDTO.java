package swguan.middleware.support.header;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeaderTokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userSeqNbr;
	private Integer loginSeqNbr;
	private Boolean isLogin = false;

}
