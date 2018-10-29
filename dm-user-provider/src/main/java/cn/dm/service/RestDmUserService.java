package cn.dm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cn.dm.mapper.DmUserMapper;
import cn.dm.pojo.DmUser;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zezhong.shang on 18-5-15.
 */
@RestController
public class RestDmUserService {

    @Autowired
    private DmUserMapper dmUserMapper;

    @RequestMapping(value = "/getDmUserById", method = RequestMethod.POST)
    public DmUser getDmUserById(@RequestParam("id") Long id) throws Exception {
        return dmUserMapper.getDmUserById(id);
    }

    @RequestMapping(value = "/getDmUserListByMap", method = RequestMethod.POST)
    public List<DmUser> getDmUserListByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmUserMapper.getDmUserListByMap(param);
    }

    @RequestMapping(value = "/getDmUserCountByMap", method = RequestMethod.POST)
    public Integer getDmUserCountByMap(@RequestBody Map<String, Object> param) throws Exception {
        return dmUserMapper.getDmUserCountByMap(param);
    }

    @RequestMapping(value = "/checkLoginByPassword", method = RequestMethod.POST)
    public DmUser checkLoginByPassword(@RequestBody DmUser dmUser) throws Exception {
        return dmUserMapper.checkLoginByPassword(dmUser.getPhone(), dmUser.getPassword());
    }

    @RequestMapping(value = "/qdtxAddDmUser", method = RequestMethod.POST)
    @Transactional
    public Integer qdtxAddDmUser(@RequestBody DmUser dmUser) throws Exception {
        dmUser.setCreatedTime(new Date());
        return dmUserMapper.insertDmUser(dmUser);
    }

    @RequestMapping(value = "/qdtxModifyDmUser", method = RequestMethod.POST)
    @Transactional
    public Integer qdtxModifyDmUser(@RequestBody DmUser dmUser) throws Exception {
        dmUser.setUpdatedTime(new Date());
        return dmUserMapper.updateDmUser(dmUser);
    }

    @RequestMapping(value = "/findByWxUserId", method = RequestMethod.POST)
    public DmUser findByWxUserId(@RequestParam("wxUserId") String wxUserId) throws Exception {
        return dmUserMapper.findByWxUserId(wxUserId);
    }

    @RequestMapping(value = "/createuser")
    @Transactional
    public Integer createDmUser(@RequestBody DmUser dmUser) throws Exception {
        dmUser.setCreatedTime(new Date());
        return dmUserMapper.insertDmUser(dmUser);
    }
}
