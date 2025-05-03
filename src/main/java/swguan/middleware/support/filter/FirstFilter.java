
package swguan.middleware.support.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import swguan.middleware.util.UidUtil;

@Slf4j
@Component
@Order(FirstFilter.DEFAULT_ORDER)
public class FirstFilter implements Filter {

	public static final int DEFAULT_ORDER = Integer.MIN_VALUE;
    public static final String MDC_UID_NODE = "uid";

	private final Set<String> excludeUri = new HashSet<>(Arrays.asList("/v1/actuator/alive"));

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final String guid = UidUtil.getTimeUuid();
		MDC.put(MDC_UID_NODE, guid);

		final boolean nologRequestInfo = excludeUri.contains(((HttpServletRequest) request).getRequestURI());
		if (!nologRequestInfo) {
			log.info("=REQ=URI= :: {}", ((HttpServletRequest) request).getRequestURI());
		}

		try {
			chain.doFilter(request, response);
		} finally {
			if (request instanceof HttpServletRequest && !nologRequestInfo) {
				log.info("=RES=URI= :: {}", ((HttpServletRequest) request).getRequestURI());
			}
			MDC.clear();
		}
	}
}
