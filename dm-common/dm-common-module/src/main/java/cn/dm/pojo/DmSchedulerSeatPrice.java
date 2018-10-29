package cn.dm.pojo;
import java.io.Serializable;
import java.util.Date;
/***
*   节目排期座位价格表
*/
public class DmSchedulerSeatPrice implements Serializable {
    //主键
    private Long id;
    //价格
    private Double price;
    //区域级别(1:A;2:B;3:C)
    private Integer areaLevel;
    //排期Id
    private Long scheduleId;
    //
    private Date createdTime;
    //
    private Date updatedTime;

    private Integer seatNum;
    //get set 方法
    public void setId (Long  id){
        this.id=id;
    }
    public  Long getId(){
        return this.id;
    }
    public void setPrice (Double  price){
        this.price=price;
    }
    public  Double getPrice(){
        return this.price;
    }
    public void setAreaLevel (Integer  areaLevel){
        this.areaLevel=areaLevel;
    }
    public  Integer getAreaLevel(){
        return this.areaLevel;
    }
    public void setScheduleId (Long  scheduleId){
        this.scheduleId=scheduleId;
    }
    public  Long getScheduleId(){
        return this.scheduleId;
    }
    public void setCreatedTime (Date  createdTime){
        this.createdTime=createdTime;
    }
    public  Date getCreatedTime(){
        return this.createdTime;
    }
    public void setUpdatedTime (Date  updatedTime){
        this.updatedTime=updatedTime;
    }
    public  Date getUpdatedTime(){
        return this.updatedTime;
    }

    public Integer getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(Integer seatNum) {
        this.seatNum = seatNum;
    }
}
