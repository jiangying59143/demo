package com.mall.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class KillProduct extends BaseTO implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @org.hibernate.annotations.ColumnDefault("0")
    private Integer killStoreNumber;

    @org.hibernate.annotations.ColumnDefault("0")
    private Integer killedNumber;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKillStoreNumber() {
        return killStoreNumber;
    }

    public void setKillStoreNumber(Integer killStoreNumber) {
        this.killStoreNumber = killStoreNumber;
    }

    public Integer getKilledNumber() {
        return killedNumber;
    }

    public void setKilledNumber(Integer killedNumber) {
        this.killedNumber = killedNumber;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
