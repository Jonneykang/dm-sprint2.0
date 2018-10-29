package cn.dm.mapper;
import cn.dm.pojo.DmItemComment;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DmCommentMapper {

	public DmItemComment getDmCommentById(@Param(value = "id") Long id)throws Exception;

	public List<DmItemComment>	getDmCommentListByMap(Map<String, Object> param)throws Exception;

	public Integer getDmCommentCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertDmComment(DmItemComment DmItemComment)throws Exception;

	public Integer updateDmComment(DmItemComment DmItemComment)throws Exception;

	public Integer deleteDmCommentById(@Param(value = "id") Long id)throws Exception;

	public Integer batchDeleteDmComment(Map<String, List<String>> params);

}
