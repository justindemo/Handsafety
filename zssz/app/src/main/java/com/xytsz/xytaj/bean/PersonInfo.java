package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 *
 */
public class PersonInfo implements Serializable {


    /**
     * DeptName : 仓储队
     * ID : 371
     * userName : 向阳天
     * Dept_id : 1
     * Area_id : 1
     * sex : 男
     * login_ID : xyt
     * password : 123456
     * telephone : 1111
     * role_ID : 1
     * Dirty : true
     * Key :
     * SubEntities : []
     */

    private String DeptName;
    private int ID;
    private String userName;
    private int Dept_id;
    private int Area_id;
    private String sex;
    private String login_ID;
    private String password;
    private String telephone;
    private int role_ID;
    private boolean Dirty;
    private String Key;
    private List<?> SubEntities;

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

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

    public int getDept_id() {
        return Dept_id;
    }

    public void setDept_id(int Dept_id) {
        this.Dept_id = Dept_id;
    }

    public int getArea_id() {
        return Area_id;
    }

    public void setArea_id(int Area_id) {
        this.Area_id = Area_id;
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

    public int getRole_ID() {
        return role_ID;
    }

    public void setRole_ID(int role_ID) {
        this.role_ID = role_ID;
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
