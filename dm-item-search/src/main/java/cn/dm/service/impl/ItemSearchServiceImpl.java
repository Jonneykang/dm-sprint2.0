package cn.dm.service.impl;
import cn.dm.client.RestDmCinemaClient;
import cn.dm.client.RestDmImageClient;
import cn.dm.client.RestDmItemClient;
import cn.dm.common.*;
import cn.dm.es.document.IESDocument;
import cn.dm.es.common.EsUtils;
import cn.dm.es.query.ItemQuery;
import cn.dm.item.ItemEsQuery;
import cn.dm.item.ItemSearchVo;
import cn.dm.pojo.DmCinema;
import cn.dm.pojo.DmImage;
import cn.dm.pojo.DmItem;
import cn.dm.service.ItemSearchService;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Component
public class ItemSearchServiceImpl implements ItemSearchService {

    @Resource
    private RestDmItemClient restDmItemClient;
    @Resource
    private RestDmImageClient restDmImageClient;
    @Resource
    private RestDmCinemaClient restDmCinemaClient;
    @Resource
    private EsUtils esUtils;

    @Value("${lastUpdatedTimeFile}")
    private String lastUpdatedTimeFile;

    private Logger logger=Logger.getLogger(ItemSearchServiceImpl.class);

    @Override
    public Page<ItemSearchVo> queryItemList(ItemQuery itemQuery) throws Exception {
        ItemEsQuery itemEsQuery=new ItemEsQuery();
        if(EmptyUtils.isNotEmpty(itemQuery.getItemTypeId1()) && itemQuery.getItemTypeId1()!=0){
            itemEsQuery.setMatchParams("itemTypeId1",itemQuery.getItemTypeId1());
        }
        if(EmptyUtils.isNotEmpty(itemQuery.getItemTypeId2()) && itemQuery.getItemTypeId2()!=0){
            itemEsQuery.setMatchParams("itemTypeId2",itemQuery.getItemTypeId2());
        }
        if(EmptyUtils.isNotEmpty(itemQuery.getAreaId()) && itemQuery.getAreaId()!=0 ){
            itemEsQuery.setMatchParams("areaId",itemQuery.getAreaId());
        }
        if(EmptyUtils.isNotEmpty(itemQuery.getKeyword())){
            itemEsQuery.setLikeMatchParams("itemName",itemQuery.getKeyword());
        }
        if(EmptyUtils.isNotEmpty(itemQuery.getStartTime())){
            Long startTimeLong=DateUtil.parse(itemQuery.getStartTime(),"yyyy-MM-dd").getTime();
            itemEsQuery.setGteParams("startTimeLong",startTimeLong);
        }
        if(EmptyUtils.isNotEmpty(itemQuery.getEndTime())){
            Long endTimeLong=DateUtil.parse(itemQuery.getEndTime(),"yyyy-MM-dd").getTime();
            itemEsQuery.setLteParams("startTimeLong",endTimeLong);
        }
        //指定排序的字段("recommend"："推荐","recentShow":"最近演出","recentSell"：最近上架)
        if(EmptyUtils.isNotEmpty(itemQuery.getSort())){
            if(itemQuery.getSort().equals("recommend")){
                itemEsQuery.setDesc("commentCount");
            }else if(itemQuery.getSort().equals("recentShow")){
                itemEsQuery.setDesc("createdTimeLong");
            }else if(itemQuery.getSort().equals("recentSell")){
                itemEsQuery.setDesc("startTimeLong");
            }
        }
        itemEsQuery.setPageNo(itemQuery.getCurrentPage());
        itemEsQuery.setPageSize(itemQuery.getPageSize());
        return esUtils.queryPage(itemEsQuery);
    }

    /***
     * 增量更新mysql数据到ES
     * 导入item数据
     * 对比数据库中的记录数据的“updatedTime”
     * 保存的时间和记录上次更新索引库的时间：
     * 保存在磁盘文件中。如果“updatedTime”的值大于上次保存的时间，
     * 这时调用ES的API导入mysq数据到ES索引库。更新索引库后，
     * 再将磁盘文件中的时间修改为最后上面“updatedTime”时间。
     * 实现定时器定时对比以上两个时间的大小。
     * @throws Exception
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void importItemList()throws Exception{
        Map<String,Object> params=new HashMap<String,Object>();
        String lastUpdatedTime= getLastUpdatedTime();
        if(EmptyUtils.isNotEmpty(lastUpdatedTime)){
            params.put("lastUpdatedTime",lastUpdatedTime);
        }
        //查询出需要同步的数据：最新创建和最新修改数据
        List<DmItem> dmItemList=restDmItemClient.getDmItemListByMap(params);
        List<IESDocument> itemSearchVoList=new ArrayList<IESDocument>();
        if(EmptyUtils.isNotEmpty(dmItemList)){
            for (DmItem dmItem:dmItemList){
                ItemSearchVo itemSearchVo=new ItemSearchVo();
                //更新商品
                BeanUtils.copyProperties(dmItem,itemSearchVo);
                //更新商品图片、区域名称、地址
                List<DmImage> dmImages=restDmImageClient.queryDmImageList(dmItem.getId(), Constants.Image.ImageType.normal,Constants.Image.ImageCategory.item);
                itemSearchVo.setImgUrl(EmptyUtils.isEmpty(dmImages)?null:dmImages.get(0).getImgUrl());
                itemSearchVo.setItemTypeId1(dmItem.getItemType1Id());
                itemSearchVo.setItemTypeId2(dmItem.getItemType2Id());
                itemSearchVo.setStartTime(DateUtil.format(dmItem.getStartTime()));
                itemSearchVo.setEndTime(DateUtil.format(dmItem.getEndTime()));
                //更新影院
                DmCinema dmCinema=restDmCinemaClient.getDmCinemaById(dmItem.getCinemaId());
                itemSearchVo.setAreaId(dmCinema.getAreaId());
                itemSearchVo.setAddress(dmCinema.getAddress());
                itemSearchVo.setAreaName(dmCinema.getAreaName());
                itemSearchVo.setCreatedTimeLong(dmItem.getCreatedTime().getTime());
                itemSearchVo.setCreatedTime(DateUtil.format(dmItem.getCreatedTime()));
                itemSearchVo.setStartTimeLong(dmItem.getStartTime().getTime());
                itemSearchVo.setEndTimeLong(dmItem.getEndTime().getTime());
                itemSearchVo.setCommentCount(dmItem.getCommentCount());
                itemSearchVoList.add(itemSearchVo);
            }
        }
        //更新最后更新时间
        logger.info("<<<<<<<<"+DateUtil.format(new Date())+"更新了"+itemSearchVoList.size()+"数据>>>>>>>>>");
        lastUpdatedTime=DateUtil.format(new Date());
        FileUtils.writeInFile(lastUpdatedTimeFile,lastUpdatedTime);
        if(EmptyUtils.isNotEmpty(itemSearchVoList)){
            esUtils.addBatchESModule(itemSearchVoList);
        }
    }

    public String getLastUpdatedTime() throws IOException {
        FileUtils.createIfNotExist(lastUpdatedTimeFile);
        return FileUtils.readFileByLine(lastUpdatedTimeFile);
    }
}
