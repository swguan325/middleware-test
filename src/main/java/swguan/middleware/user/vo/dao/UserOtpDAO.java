package swguan.middleware.user.vo.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import swguan.middleware.user.vo.UserOtpRequestVO;
import swguan.middleware.user.vo.UserOtpResponseVO;

@Repository
public class UserOtpDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int insertOtp(UserOtpRequestVO reqVo) {
        return sqlSessionTemplate.insert("DEN_USERS_OTP.insertOtp", reqVo);
    }

    public UserOtpResponseVO selectOtpByEmail(String email) {
        return sqlSessionTemplate.selectOne("DEN_USERS_OTP.selectOtpByEmail", email);
    }

    public int updateOtpByEmail(String email) {
        return sqlSessionTemplate.update("DEN_USERS_OTP.updateOtpByEmail", email);
    }

}
