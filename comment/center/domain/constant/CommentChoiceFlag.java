package com.guohang.zsu1.comment.center.domain.constant;

/**
 * @Author: 胡宝喜
 * @Date: 2019/2/21 17:46
 * @Description: TODO
 */
public enum CommentChoiceFlag {
    NO(0,"非精选"),YES(1,"精选");
    private CommentChoiceFlag(Integer flag, String desc) {
        this.flag = flag;
        this.desc = desc;
    }
    public Integer getFlag() {
        return flag;
    }
    private Integer flag;
    private String desc;
}
