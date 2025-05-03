package swguan.middleware.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.codec.digest.DigestUtils;
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
import swguan.middleware.user.dto.UserResponseDTO;
import swguan.middleware.user.repository.MailjetOtpRepository;
import swguan.middleware.user.vo.UserLoginResponseVO;
import swguan.middleware.user.vo.UserOtpResponseVO;
import swguan.middleware.user.vo.UserResponseVO;
import swguan.middleware.user.vo.dao.UserDAO;
import swguan.middleware.user.vo.dao.UserLoginDAO;
import swguan.middleware.user.vo.dao.UserOtpDAO;
import swguan.middleware.util.JwtTokenUtil;

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

	@Mock
	private UserOtpDAO userOtpDAO;

	@Mock
	private MailjetOtpRepository mailjetOtpRepository;

	@Mock
	private JwtTokenUtil jwtTokenUtil;

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
	public void insertUser_ErrorCase_Email() {
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
	public void selectUser_NormalCase() throws Exception {
		log.debug("=== selectUser_NormalCase ===");

		UserRequestDTO reqDto = new UserRequestDTO();
		reqDto.setEmail("abc@gmail.com");
		reqDto.setPswd("pswd");

		UserResponseVO resVo = new UserResponseVO();
		resVo.setEmail(reqDto.getEmail());
		resVo.setPswd(DigestUtils.sha512Hex(reqDto.getPswd()));
		resVo.setIsOpen(true);

		when(userDAO.selectUserByEmail(ArgumentMatchers.any())).thenReturn(resVo);
		when(jwtTokenUtil.generateToken(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn("jwtToken");

		final FlowException expectedException = new FlowException(ResponseCode.EMAIL_NOT_AUTH);
		FlowException actualException = null;
		try {
			userService.selectUser(reqDto);
		} catch (final FlowException e) {
			actualException = e;
		}
		assertEquals(actualException.getCode(), expectedException.getCode());
		assertEquals(((UserResponseDTO) actualException.getErrorContent()).getToken(), "jwtToken");

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

	@Test
	public void selectUser_ErrorCase_PASSWORD() throws Exception {
		log.debug("=== selectUser_ErrorCase_PASSWORD ===");

		UserRequestDTO reqDto = new UserRequestDTO();
		reqDto.setEmail("abc@gmail.com");
		reqDto.setPswd("pswd");

		UserResponseVO resVo = new UserResponseVO();
		resVo.setEmail(reqDto.getEmail());
		resVo.setPswd("asdw");

		when(userDAO.selectUserByEmail(ArgumentMatchers.any())).thenReturn(resVo);

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
	public void authUser_NormalCase() throws FlowException {
		log.debug("=== authUser_NormalCase ===");

		final UserAuthRequestDTO reqDto = new UserAuthRequestDTO();
		reqDto.setOtp("123456");

		final HeaderTokenDTO headerTokenDTO = new HeaderTokenDTO();
		headerTokenDTO.setUserSeqNbr(1);
		headerTokenDTO.setLoginSeqNbr(99);

		final UserResponseVO resVo = new UserResponseVO();
		resVo.setEmail("abc@gmail.com");
		resVo.setIsOpen(true);

		final UserOtpResponseVO resOtpVo = new UserOtpResponseVO();
		resOtpVo.setOtp("123456");

		when(userDAO.selectUserBySeqNbr(ArgumentMatchers.anyInt())).thenReturn(resVo);
		when(userOtpDAO.selectOtpByEmail(ArgumentMatchers.any())).thenReturn(resOtpVo);

		userService.authUser(headerTokenDTO, reqDto);

		verify(userLoginDAO, new Times(1)).updateLoginBySeqNbr(ArgumentMatchers.anyInt());
		verify(userDAO, new Times(0)).updateUserBySeqNbr(ArgumentMatchers.anyInt());
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

	@Test
	public void getLastLogin_ErrorCase_LOGIN() throws FlowException {
		log.debug("=== getLastLogin_ErrorCase_LOGIN ===");

		final HeaderTokenDTO headerTokenDTO = new HeaderTokenDTO();
		headerTokenDTO.setUserSeqNbr(1);
		headerTokenDTO.setLoginSeqNbr(99);

		final UserResponseVO resVo = new UserResponseVO();
		resVo.setEmail("abc@gmail.com");

		when(userDAO.selectUserBySeqNbr(ArgumentMatchers.anyInt())).thenReturn(resVo);

		final FlowException expectedException = new FlowException(ResponseCode.LOGIN_NOT_FOUND);
		FlowException actualException = null;
		try {
			userService.getLastLogin(headerTokenDTO);
		} catch (final FlowException e) {
			actualException = e;
		}
		assertEquals(actualException.getCode(), expectedException.getCode());

		log.debug("=== cdoe: {}, message: {} ===", actualException.getCode(), actualException.getMessage());
	}

}
