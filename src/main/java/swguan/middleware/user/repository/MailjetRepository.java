package swguan.middleware.user.repository;

import swguan.middleware.user.vo.MailjetOtpRequestVO;

public interface MailjetRepository {

	public void sendMailotp(MailjetOtpRequestVO reqVo) throws Exception;

}
