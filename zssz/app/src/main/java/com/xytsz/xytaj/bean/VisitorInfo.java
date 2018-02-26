package com.xytsz.xytaj.bean;

import java.util.List;

/**
 * Created by admin on 2017/9/8.
 *  游客的信息
 */
public class VisitorInfo {


    /**
     * id : 1
     * tel : 13679678771
     * name : 王哈哈
     * birthday : 1992-03-19
     * integral : 10000
     * role_id : 0
     * Dirty : true
     * Key :
     * SubEntities : []
     */

    private int id;
    private String tel;
    private String name;
    private String birthday;
    private int integral;
    private int role_id;
    private boolean Dirty;
    private String Key;
    private List<?> SubEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public boolean isDirty() {
        return Dirty;
    }

    public void setDirty(boolean Dirty) {
        this.Dirty = Dirty;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public List<?> getSubEntities() {
        return SubEntities;
    }

    public void setSubEntities(List<?> SubEntities) {
        this.SubEntities = SubEntities;
    }
}
