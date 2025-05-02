package swguan.middleware.support.header;

import javax.servlet.http.HttpServletRequest;

public interface HeaderTokenDTOListener {

	public void afterHeaderTokenDtoSet(final HttpServletRequest request, final HeaderTokenDTO headerDto);

}
