package swguan.middleware.support.header;

public abstract class HeaderContext {

	private static final ThreadLocal<HeaderTokenDTO> reqContext = new ThreadLocal<>();

	public static void setHeaderTokenDTO(HeaderTokenDTO headerDto) {
		reqContext.set(headerDto);
	}

	public static HeaderTokenDTO getHeaderTokenDTO() {
		return reqContext.get();
	}

	public static void removeHeaderTokenDTO() {
		reqContext.remove();
	}

}
