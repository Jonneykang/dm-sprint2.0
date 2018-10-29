package cn.dm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.dm.mapper.DmCinemaSeatMapper;
import cn.dm.pojo.DmCinemaSeat;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by zezhong.shang on 18-5-15.
 */
@RestController
public class RestDmCinemaSeatService {

     @Autowired
     private DmCinemaSeatMapper dmCinemaSeatMapper;

     @RequestMapping(value = "/getDmCinemaSeatById",method = RequestMethod.POST)
     public DmCinemaSeat getDmCinemaSeatById(@RequestParam("id") Long id)throws Exception{
        return dmCinemaSeatMapper.getDmCinemaSeatById(id);
     }

     @RequestMapping(value = "/getDmCinemaSeatListByMap",method = RequestMethod.POST)
     public List<DmCinemaSeat>	getDmCinemaSeatListByMap(@RequestBody Map<String,Object> param)throws Exception{
        return dmCinemaSeatMapper.getDmCinemaSeatListByMap(param);
     }

     @RequestMapping(value = "/getDmCinemaSeatCountByMap",method = RequestMethod.POST)
     public Integer getDmCinemaSeatCountByMap(@RequestBody Map<String,Object> param)throws Exception{
        return dmCinemaSeatMapper.getDmCinemaSeatCountByMap(param);
     }

     @RequestMapping(value = "/qdtxAddDmCinemaSeat",method = RequestMethod.POST)
     public Integer qdtxAddDmCinemaSeat(@RequestBody DmCinemaSeat dmCinemaSeat)throws Exception{
        dmCinemaSeat.setCreatedTime(new Date());
        return dmCinemaSeatMapper.insertDmCinemaSeat(dmCinemaSeat);
     }

     @RequestMapping(value = "/qdtxModifyDmCinemaSeat",method = RequestMethod.POST)
     public Integer qdtxModifyDmCinemaSeat(@RequestBody DmCinemaSeat dmCinemaSeat)throws Exception{
        dmCinemaSeat.setUpdatedTime(new Date());
        return dmCinemaSeatMapper.updateDmCinemaSeat(dmCinemaSeat);
     }

    @RequestMapping(value = "/queryCinemaSeatArray", method = RequestMethod.POST)
    public List<String> queryCinemaSeatArray(@RequestBody Map<String, Object> params) throws Exception{
        List<String> seatArray=dmCinemaSeatMapper.queryCinemaSeatArray(params);
        return seatArray;
    }
}
