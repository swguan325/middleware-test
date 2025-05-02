package swguan.middleware.support.exception;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import swguan.middleware.support.enums.ResponseCode;

@Getter
public class FlowException extends Exception {

	private static final long serialVersionUID = 1L;

	private final String message;
	private final String code;
    private final Object errorContent;

	public FlowException(String code) {
		this.code = code;
		this.message = StringUtils.EMPTY;
        this.errorContent = null;
	}

	public FlowException(String code, String message) {
		this.code = code;
		this.message = message;
        this.errorContent = null;
	}

	public FlowException(ResponseCode res) {
		this.code = res.getCode();
		this.message = res.getMessage();
        this.errorContent = null;
	}

	public FlowException(ResponseCode res, Object errorContent) {
		this.code = res.getCode();
		this.message = res.getMessage();
        this.errorContent = errorContent;
	}

}
