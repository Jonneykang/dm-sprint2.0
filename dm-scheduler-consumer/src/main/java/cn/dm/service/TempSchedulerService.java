package cn.dm.service;


import cn.dm.pojo.DmScheduler;

public interface TempSchedulerService {

    /**
     * 生成排期座位数据
     */
    public void generateScheduleSeatData() throws Exception;


    public void addScheduler(DmScheduler dmScheduler) throws Exception;

}
