package swguan.middleware.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.extern.slf4j.Slf4j;
import swguan.middleware.support.enums.ResponseCode;
import swguan.middleware.support.exception.FlowException;
import swguan.middleware.support.header.HeaderTokenDTO;
import swguan.middleware.user.dto.UserAuthRequestDTO;
import swguan.middleware.user.dto.UserLoginResponseDTO;
import swguan.middleware.user.dto.UserRequestDTO;
import swguan.middleware.user.vo.UserLoginResponseVO;
import swguan.middleware.user.vo.UserResponseVO;
import swguan.middleware.user.vo.dao.UserDAO;
import swguan.middleware.user.vo.dao.UserLoginDAO;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserDAO userDAO;

	@Mock
	private UserLoginDAO userLoginDAO;

	@Test
	public void insertUser_NormalCase() throws FlowException {
		log.debug("=== insertUser_NormalCase ===");

		UserRequestDTO reqDto = new UserRequestDTO();
		reqDto.setEmail("abc@gmail.com");
		reqDto.setPswd("pswd");

		final FlowException expectedException = new FlowException(ResponseCode.ACCT_NEED_LOGIN);
		FlowException actualException = null;
		try {
			userService.insertUser(reqDto);
		} catch (final FlowException e) {
			actualException = e;
		}
		verify(userDAO, new Times(1)).insertUser(ArgumentMatchers.any());
		assertEquals(actualException.getCode(), expectedException.getCode());

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

	@Test
	public void insertUser_ErrorCase() {
		log.debug("=== insertUser_ErrorCase_Email ===");

		UserRequestDTO reqDto = new UserRequestDTO();
		reqDto.setEmail("a@b.c");
		reqDto.setPswd("pswd");

		final FlowException expectedException = new FlowException(ResponseCode.EMAIL_NOT_ALLOW);
		FlowException actualException = null;
		try {
			userService.insertUser(reqDto);
		} catch (final FlowException e) {
			actualException = e;
		}
		verify(userDAO, new Times(0)).insertUser(ArgumentMatchers.any());
		assertEquals(actualException.getCode(), expectedException.getCode());

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

	@Test
	public void selectUser_ErrorCase_PASSWORD() throws Exception {
		log.debug("=== selectUser_ErrorCase ===");

		UserRequestDTO reqDto = new UserRequestDTO();
		reqDto.setEmail("abc@gmail.com");
		reqDto.setPswd("pswd");

		UserResponseVO reqVo = new UserResponseVO();
		reqVo.setEmail(reqDto.getEmail());
		reqVo.setPswd("asdw");

		when(userDAO.selectUserByEmail(ArgumentMatchers.any())).thenReturn(reqVo);

		final FlowException expectedException = new FlowException(ResponseCode.PSWD_NOT_MATCH);
		FlowException actualException = null;
		try {
			userService.selectUser(reqDto);
		} catch (final FlowException e) {
			actualException = e;
		}
		assertEquals(actualException.getCode(), expectedException.getCode());

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

	@Test
	public void authUser_ErrorCase_EXPIRE() throws FlowException {
		log.debug("=== authUser_ErrorCase_EXPIRE ===");

		final FlowException expectedException = new FlowException(ResponseCode.LOGIN_EXPIRE);
		FlowException actualException = null;
		try {
			userService.authUser(new HeaderTokenDTO(), new UserAuthRequestDTO());
		} catch (final FlowException e) {
			actualException = e;
		}
		assertEquals(actualException.getCode(), expectedException.getCode());

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

	@Test
	public void getLastLogin_NormalCase() throws FlowException {
		log.debug("=== getLastLogin_NormalCase ===");

		final HeaderTokenDTO headerTokenDTO = new HeaderTokenDTO();
		headerTokenDTO.setUserSeqNbr(1);
		headerTokenDTO.setLoginSeqNbr(99);

		final UserResponseVO resVo = new UserResponseVO();
		resVo.setEmail("abc@gmail.com");

		final UserLoginResponseVO expected = new UserLoginResponseVO();
		expected.setPswdAuthDt("2025/05/02 20:33:13");
		expected.setEmailAuthDt("2025/05/02 20:33:32");

		when(userDAO.selectUserBySeqNbr(ArgumentMatchers.anyInt())).thenReturn(resVo);
		when(userLoginDAO.selectLoginByEmail(ArgumentMatchers.any())).thenReturn(expected);

		final UserLoginResponseDTO actual = userService.getLastLogin(headerTokenDTO);

		assertEquals(actual.getPswdAuthDt(), expected.getPswdAuthDt());
		assertEquals(actual.getEmailAuthDt(), expected.getEmailAuthDt());

		log.debug("=== actual: {} ===", actual);
	}

}
