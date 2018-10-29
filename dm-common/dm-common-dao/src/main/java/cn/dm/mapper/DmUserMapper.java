package cn.dm.mapper;
import cn.dm.pojo.DmUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DmUserMapper {
	/**
	 * 根据用户ID查询用户信息
	 * @param id 用户ID
	 * @return 对应ID的用户
	 * @throws Exception
	 */
	public DmUser getDmUserById(@Param(value = "id") Long id) throws Exception;
	/**
	 * 根据微信ID查询用户信息
	 * @param wxUserId 微信ID
	 * @return 对应用户
	 */
	public DmUser findByWxUserId(@Param(value = "wxUserId") String wxUserId) throws Exception;
	/**
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<DmUser>	getDmUserListByMap(Map<String, Object> param) throws Exception;
	/**
	 *
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer getDmUserCountByMap(Map<String, Object> param) throws Exception;
	/**
	 * 使用账号密码登录
	 * @param phone
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public DmUser checkLoginByPassword(@Param("phone") String phone, @Param("password") String password) throws Exception;
	/**
	 * 添加用户
	 * @param dmUser
	 * @return
	 * @throws Exception
	 */
	public Integer insertDmUser(DmUser dmUser) throws Exception;
	/**
	 * 修改用户
	 * @param dmUser
	 * @return
	 * @throws Exception
	 */
	public Integer updateDmUser(DmUser dmUser) throws Exception;
	/**
	 * 修改密码
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public Integer updateDmUserPassword(@Param("id") Long id, @Param("password") String password) throws Exception;
	/**
	 * 根据用户ID删除用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Integer deleteDmUserById(@Param(value = "id") Long id) throws Exception;

	// public Integer batchDeleteDmUser(Map<String,List<String>> params) throws Exception;

}
