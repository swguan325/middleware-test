
package swguan.middleware.support.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK("200", "success"),
    PSWD_NOT_MATCH("101", "Password not match"),
    USER_NOT_FOUND("102", "User not found, please register"),
    ACCT_NEED_LOGIN("103", "Account creat, please login"),
    ACCT_NOT_OPEN("104", "Account not open, please open"),
    LOGIN_NOT_FOUND("105", "Login not found, please login"),
    EMAIL_NOT_AUTH("106", "Please finish email auth after login"),
	EMAIL_NOT_ALLOW("107", "Email not allow"),
    EMAIL_EXIST("108", "Email exist"),
    OTP_NOT_MATCH("109", "OTP not match"),
    LOGIN_EXPIRE("888", "Login token expire"),
    COMMON_ERROR("999", "Common error"),
    ;

    private final String code;
    private final String message;
}
