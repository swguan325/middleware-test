package swguan.middleware.support.header;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import swguan.middleware.util.JwtTokenUtil;

@Slf4j
@Component
public class HeaderTokenDTOInterceptor extends HandlerInterceptorAdapter {

	@Autowired(required = false)
	private HeaderTokenDTOListener headerTokenDTOListener;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final String BEARER = "Bearer";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HeaderTokenDTO headerTokenDTO = null;

		final String reqHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (reqHeader != null && reqHeader.startsWith(BEARER)) {

			final String jwtToken = reqHeader.replace(BEARER, "");

			headerTokenDTO = jwtTokenUtil.getHeaderTokenDTO(jwtToken);
			headerTokenDTO.setIsLogin(headerTokenDTO.getLoginSeqNbr() != null ? true : false);
		} else {
			headerTokenDTO = new HeaderTokenDTO();
		}
		HeaderContext.setHeaderTokenDTO(headerTokenDTO);

		log.debug("set headerTokenDTO: {}", headerTokenDTO);

		if (Objects.nonNull(headerTokenDTOListener)) {
			try {
				headerTokenDTOListener.afterHeaderTokenDtoSet(request, headerTokenDTO);
			} catch (final Exception e) {
				log.warn("afterHeaderTokenDtoSet method call error", e);
			}
		}

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HeaderContext.removeHeaderTokenDTO();
		log.debug("remove headerTokenDTO");
		super.postHandle(request, response, handler, modelAndView);
	}

}
