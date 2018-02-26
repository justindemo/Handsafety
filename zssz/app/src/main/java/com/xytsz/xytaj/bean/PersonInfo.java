package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 *
 */
public class PersonInfo implements Serializable {


    /**
     * ID : 1
     * userName : 王蒙蒙
     * sex : 男
     * login_ID : wmm
     * password : 123456
     * telephone : 0000000
     * department_ID : 1
     * Dirty : true
     * Key :
     * SubEntities : []
     */

    private int ID;
    private String userName;
    private String sex;
    private String login_ID;
    private String password;
    private String telephone;
    private int department_ID;
    private boolean Dirty;
    private String Key;
    private List<?> SubEntities;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLogin_ID() {
        return login_ID;
    }

    public void setLogin_ID(String login_ID) {
        this.login_ID = login_ID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getDepartment_ID() {
        return department_ID;
    }

    public void setDepartment_ID(int department_ID) {
        this.department_ID = department_ID;
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
