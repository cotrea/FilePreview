package cn.keking.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author yanting
 * @date 2018/11/8
 */
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Column(name = "validFlag")
    private Integer validFlag = 1;
    @Column(name = "dateOfCreate")
    private Date dateOfCreate = new Date();
    @Column(name = "dateOfUpdate")
    private Date dateOfUpdate = new Date();
    @Column(name = "memo")
    private String memo = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public void setDateOfCreate(Date dateOfCreate) {
        this.dateOfCreate = dateOfCreate;
    }

    public Date getDateOfUpdate() {
        return dateOfUpdate;
    }

    public void setDateOfUpdate(Date dateOfUpdate) {
        this.dateOfUpdate = dateOfUpdate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BaseEntity() {
        validFlag = 1;
    }
}