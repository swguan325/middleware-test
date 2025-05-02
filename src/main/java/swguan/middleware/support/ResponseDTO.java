
package swguan.middleware.support;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import swguan.middleware.support.enums.ResponseCode;
import swguan.middleware.support.exception.FlowException;

@Slf4j
@Getter
@Setter
public class ResponseDTO<T> {
	private String code;
	private String message;
	private T content;

	public static <T> ResponseDTO<T> success() {
		return new ResponseDTO<>(ResponseCode.OK);
	}

	public static <T> ResponseDTO<T> success(T responseData) {
		log.info("Success ResponseData:: {}", responseData);
		return new ResponseDTO<>(ResponseCode.OK, responseData);
	}

	public static <T> ResponseDTO<T> fail(String code, String message) {
		log.info("Fail ResponseData:: code: {}, message: {}", code, message);
		return new ResponseDTO<>(code, message);
	}

	public static <T> ResponseDTO<?> fail(FlowException e) {
		log.info("Fail ResponseData:: code: {}, message: {}", e.getCode(), e.getMessage());
		return new ResponseDTO<>(e.getCode(), e.getMessage(), e.getErrorContent());
	}

	ResponseDTO() {
	}

	ResponseDTO(ResponseCode responseCode) {
		this.code = responseCode.getCode();
		this.message = responseCode.getMessage();
	}

	ResponseDTO(ResponseCode responseCode, T content) {
		this.code = responseCode.getCode();
		this.message = responseCode.getMessage();
		this.content = content;
	}

	ResponseDTO(String code, String message) {
		this.code = code;
		this.message = message;
	}

	ResponseDTO(String code, String message, T content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

}
