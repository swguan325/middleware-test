package swguan.middleware.support.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import swguan.middleware.support.ResponseDTO;
import swguan.middleware.support.enums.ResponseCode;
import swguan.middleware.support.exception.FlowException;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionAdvice {

	// FlowException
	@ExceptionHandler(FlowException.class)
	protected ResponseDTO<?> handleFlowException(final FlowException e) {
		return ResponseDTO.fail(e);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseDTO<?> handleException(final HttpMessageNotReadableException e) {
		log.error("{}", e);
		return handleCommonException();
	}

	@ExceptionHandler(NullPointerException.class)
	protected ResponseDTO<?> handleException(final NullPointerException e) {
		log.error("{}", e);
		return handleCommonException();
	}

	private ResponseDTO<?> handleCommonException() {
		return ResponseDTO.fail(ResponseCode.COMMON_ERROR.getCode(), ResponseCode.COMMON_ERROR.getMessage());
	}

	@ExceptionHandler(ExpiredJwtException.class)
	protected ResponseDTO<?> handleException(final ExpiredJwtException e) {
		log.error("{}", e);
		return ResponseDTO.fail(ResponseCode.LOGIN_EXPIRE.getCode(), ResponseCode.LOGIN_EXPIRE.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseDTO<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return handleFlowException(new FlowException(ResponseCode.COMMON_ERROR, errors));
	}
}
