package swguan.middleware.user.repository;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import swguan.middleware.user.vo.MailjetOtpRequestVO;
import swguan.middleware.user.vo.MailjetOtpResponseVO;

@Repository
public class MailjetOtpRepository implements MailjetRepository {

	@Value("${mailjet.url}")
	private String mailjetUri;

	@Value("${mailjet.apikey}")
	private String apikey;

	@Value("${mailjet.secret}")
	private String secret;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void sendMailotp(MailjetOtpRequestVO reqVo) throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(apikey, secret);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<MailjetOtpRequestVO> entity = new HttpEntity<>(reqVo, httpHeaders);
		restTemplate.exchange(mailjetUri, HttpMethod.POST, entity, MailjetOtpResponseVO.class);
	}

}
