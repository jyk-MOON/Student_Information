package com.example.entity;

import java.io.Serializable;

/**
 * 管理员
*/
public class UserT extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;
    /** 邮箱 */
    private String email;
    /** 头像 */
    private String avatar;
    /** 生日 */
    private String birthday;
    /**  钱 */
    private float money;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday(){
        return birthday;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public float getMoney(){
        return money;
    }

    public void setMoney(float money){
        this.money = money;
    }
}