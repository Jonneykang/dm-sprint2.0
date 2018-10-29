package cn.dm.pojo;

import java.io.Serializable;
import java.util.Date;

/***
 *   订单联系人表
 */
public class DmOrderLinkUser implements Serializable {
    //主键
    private Long id;
    //订单id
    private Long orderId;
    //联系人id
    private Long linkUserId;
    //座位位置
    private Integer x;
    private Integer y;
    //座位价格
    private Double price;
    //入住人姓名逗号分隔
    private String linkUserName;
    //
    private Date createdTime;
    //
    private Date updatedTime;

    //get set 方法
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setLinkUserId(Long linkUserId) {
        this.linkUserId = linkUserId;
    }

    public Long getLinkUserId() {
        return this.linkUserId;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }

    public String getLinkUserName() {
        return this.linkUserName;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() {
        return this.updatedTime;
    }
}
