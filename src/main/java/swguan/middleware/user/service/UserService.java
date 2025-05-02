package swguan.middleware.user.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import swguan.middleware.support.enums.ResponseCode;
import swguan.middleware.support.exception.FlowException;
import swguan.middleware.support.header.HeaderTokenDTO;
import swguan.middleware.user.dto.UserAuthRequestDTO;
import swguan.middleware.user.dto.UserLoginResponseDTO;
import swguan.middleware.user.dto.UserRequestDTO;
import swguan.middleware.user.dto.UserResponseDTO;
import swguan.middleware.user.repository.MailjetOtpRepository;
import swguan.middleware.user.transformer.UserTransformer;
import swguan.middleware.user.util.OtpUtil;
import swguan.middleware.user.vo.MailjetOtpRequestVO;
import swguan.middleware.user.vo.UserLoginResponseVO;
import swguan.middleware.user.vo.UserOtpResponseVO;
import swguan.middleware.user.vo.UserRequestVO;
import swguan.middleware.user.vo.UserResponseVO;
import swguan.middleware.user.vo.dao.UserDAO;
import swguan.middleware.user.vo.dao.UserLoginDAO;
import swguan.middleware.user.vo.dao.UserOtpDAO;
import swguan.middleware.util.JwtTokenUtil;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserLoginDAO userLoginDAO;

	@Autowired
	private UserOtpDAO userOtpDAO;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MailjetOtpRepository mailjetOtpRepository;

	public void insertUser(UserRequestDTO reqDto) throws FlowException {
		checkEmail(reqDto.getEmail());
		final UserRequestVO reqVo = UserTransformer.transform(reqDto);
		reqVo.setPswd(DigestUtils.sha512Hex(reqDto.getPswd()));
		try {
			userDAO.insertUser(reqVo);
		} catch (Exception e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				throw new FlowException(ResponseCode.EMAIL_EXIST);
			}
			throw e;
		}
		throw new FlowException(ResponseCode.ACCT_NEED_LOGIN);
	}

	private void checkEmail(String email) throws FlowException {
		if (!Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email).find()) {
			throw new FlowException(ResponseCode.EMAIL_NOT_ALLOW);
		}
	}

	public UserResponseDTO selectUser(UserRequestDTO reqDto) throws Exception {
		final UserRequestVO reqVo = UserTransformer.transform(reqDto);
		final UserResponseVO resVo = userDAO.selectUserByEmail(reqVo);
		if (resVo != null) {
			comparePswd(reqDto.getPswd(), resVo.getPswd());
			final String otpStr = new String(OtpUtil.genOtp());
			userOtpDAO.insertOtp(UserTransformer.transform(reqVo.getEmail(), otpStr));
			// 寄信
			MailjetOtpRequestVO reqOtpVo = UserTransformer.transformOtp(resVo.getEmail(), otpStr);
			mailjetOtpRepository.sendMailotp(reqOtpVo);
			if (!resVo.getIsOpen()) {
				UserResponseDTO resDto = UserTransformer.transform(jwtTokenUtil.generateToken(resVo.getSeqNbr(), null));
				throw new FlowException(ResponseCode.ACCT_NOT_OPEN, resDto);
			} else {
				userLoginDAO.insertLogin(reqVo);
				UserResponseDTO resDto = UserTransformer
						.transform(jwtTokenUtil.generateToken(resVo.getSeqNbr(), reqVo.getSeqNbr()));
				throw new FlowException(ResponseCode.EMAIL_NOT_AUTH, resDto);
			}

		}
		throw new FlowException(ResponseCode.USER_NOT_FOUND);
	}

	private void comparePswd(String inputPswd, String userPswd) throws FlowException {
		if (!userPswd.equals(DigestUtils.sha512Hex(inputPswd))) {
			throw new FlowException(ResponseCode.PSWD_NOT_MATCH);
		}
	}

	@Transactional
	public void authUser(HeaderTokenDTO headerTokenDTO, UserAuthRequestDTO reqDto) throws FlowException {
		if (headerTokenDTO.getUserSeqNbr() != null) {
			final UserResponseVO resVo = userDAO.selectUserBySeqNbr(headerTokenDTO.getUserSeqNbr());
			final UserOtpResponseVO resOtpVo = userOtpDAO.selectOtpByEmail(resVo.getEmail());
			if (resOtpVo != null && reqDto.getOtp().equals(resOtpVo.getOtp())) {
				userOtpDAO.updateOtpByEmail(resVo.getEmail());

				if (headerTokenDTO.getLoginSeqNbr() != null) {
					userLoginDAO.updateLoginBySeqNbr(headerTokenDTO.getLoginSeqNbr());
				}

				if (!resVo.getIsOpen()) {
					userDAO.updateUserBySeqNbr(resVo.getSeqNbr());
				}
				return;
			}

			throw new FlowException(ResponseCode.OTP_NOT_MATCH);
		}
		throw new FlowException(ResponseCode.LOGIN_EXPIRE);
	}

	public UserLoginResponseDTO getLastLogin(HeaderTokenDTO headerTokenDTO) throws FlowException {
		if (headerTokenDTO.getUserSeqNbr() != null) {
			final UserResponseVO resVo = userDAO.selectUserBySeqNbr(headerTokenDTO.getUserSeqNbr());
			final UserLoginResponseVO resLoginVo = userLoginDAO.selectLoginByEmail(resVo.getEmail());
			if (resLoginVo == null) {
				throw new FlowException(ResponseCode.LOGIN_NOT_FOUND);
			}
			if (resLoginVo.getEmailAuthDt() == null) {
				throw new FlowException(ResponseCode.EMAIL_NOT_AUTH);
			}
			return UserTransformer.transform(resLoginVo);
		}
		throw new FlowException(ResponseCode.LOGIN_EXPIRE);
	}

}
