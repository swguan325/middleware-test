package swguan.middleware.user.vo.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import swguan.middleware.user.vo.UserLoginResponseVO;
import swguan.middleware.user.vo.UserRequestVO;

@Repository
public class UserLoginDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int insertLogin(UserRequestVO reqVo) {
        return sqlSessionTemplate.insert("DEN_USERS_LOGIN.insertLogin", reqVo);
    }

    public int updateLoginBySeqNbr(int seqNbr) {
        return sqlSessionTemplate.update("DEN_USERS_LOGIN.updateLoginBySeqNbr", seqNbr);
    }

    public UserLoginResponseVO selectLoginByEmail(String email) {
        return sqlSessionTemplate.selectOne("DEN_USERS_LOGIN.selectLoginByEmail", email);
    }
}
