
package swguan.middleware.user.vo.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import swguan.middleware.user.vo.UserRequestVO;
import swguan.middleware.user.vo.UserResponseVO;

@Repository
public class UserDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public UserResponseVO selectUserByEmail(UserRequestVO reqVo) {
        return sqlSessionTemplate.selectOne("DEN_USERS.selectUserByEmail", reqVo);
    }

    public int insertUser(UserRequestVO reqVo) {
        return sqlSessionTemplate.insert("DEN_USERS.insertUser", reqVo);
    }

    public UserResponseVO selectUserBySeqNbr(int seqNbr) {
        return sqlSessionTemplate.selectOne("DEN_USERS.selectUserBySeqNbr", seqNbr);
    }

    public int updateUserBySeqNbr(int seqNbr) {
        return sqlSessionTemplate.update("DEN_USERS.updateUserBySeqNbr", seqNbr);
    }

}
