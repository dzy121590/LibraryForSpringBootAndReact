package com.dongzhiyuan.lib.api.enums;

/**
 * @author 董志远
 */

public enum UserTypeEnum {

    /**
     * 学生枚举值
     */
    STUDENT(1, "学生"),
    /**
     * 教师枚举值
     */
    TEACHER(2, "教师"),
    /**
     * 图书管理员枚举值
     */
    LIB_ADMIN(3, "图书管理员"),
    /**
     * 系统管理员枚举值
     */
    SYS_ADMIN(4, "系统管理员");

    UserTypeEnum(int num, String name) {
        this.num = num;
        this.name = name;
    }

    private int num;
    private String name;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
