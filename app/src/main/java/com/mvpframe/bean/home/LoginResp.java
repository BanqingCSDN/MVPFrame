package com.mvpframe.bean.home;

import com.mvpframe.bean.BaseResp;

import java.util.List;

/**
 * <功能详细描述>
 *
 */
public class LoginResp extends BaseResp {

    private Userinfo userInfo;

    public Userinfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Userinfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class  Userinfo {
        private String id;
        private String userid;
        private String pwd;
        private String name;
        private String nickName;
        private String birthday;
        private String address;
        private String time;
        private String tongxunlu;

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTongxunlu() {
            return tongxunlu;
        }

        public void setTongxunlu(String tongxunlu) {
            this.tongxunlu = tongxunlu;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }


}
