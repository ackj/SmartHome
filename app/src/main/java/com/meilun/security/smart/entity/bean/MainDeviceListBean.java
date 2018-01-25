package com.meilun.security.smart.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: LiuJia on 2018/1/3 0003 15:36.
 * Email: liujia95me@126.com
 */

public class MainDeviceListBean extends BaseBean{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * defenseStatus : cancel
         * defenseStatusDes : 仅24小时模式的探测器处于防御状态，其他探测器处于未防御状态。
         * fid : 08634f6c-8c39-4e79-9870-6fa14c4ff535
         * isCurrent : 0
         * isManager : 1
         * isOnline : 0
         * name : 网关
         * no : G111EU6B1000754
         * password :
         * residence : {"addr":"","addrDet":"","fid":"198d3131-2a76-42b5-8118-95ecd9369b71","name":""}
         * status : 0
         */

        private String defenseStatus;
        private String defenseStatusDes;
        private String fid;
        private int isCurrent;
        private int isManager;
        private int isOnline;
        private String name;
        private String no;
        private String password;
        private ResidenceBean residence;
        private int status;

        public String getDefenseStatus() {
            return defenseStatus;
        }

        public void setDefenseStatus(String defenseStatus) {
            this.defenseStatus = defenseStatus;
        }

        public String getDefenseStatusDes() {
            return defenseStatusDes;
        }

        public void setDefenseStatusDes(String defenseStatusDes) {
            this.defenseStatusDes = defenseStatusDes;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public int getIsCurrent() {
            return isCurrent;
        }

        public void setIsCurrent(int isCurrent) {
            this.isCurrent = isCurrent;
        }

        public int getIsManager() {
            return isManager;
        }

        public void setIsManager(int isManager) {
            this.isManager = isManager;
        }

        public int getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(int isOnline) {
            this.isOnline = isOnline;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public ResidenceBean getResidence() {
            return residence;
        }

        public void setResidence(ResidenceBean residence) {
            this.residence = residence;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class ResidenceBean implements Serializable {
            /**
             * addr :
             * addrDet :
             * fid : 198d3131-2a76-42b5-8118-95ecd9369b71
             * name :
             */

            private String addr;
            private String addrDet;
            private String fid;
            private String name;
            private String lat;
            private String lng;

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }

            public String getAddrDet() {
                return addrDet;
            }

            public void setAddrDet(String addrDet) {
                this.addrDet = addrDet;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
