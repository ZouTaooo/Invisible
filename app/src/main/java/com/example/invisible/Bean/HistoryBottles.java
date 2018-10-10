package com.example.invisible.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryBottles {

    /**
     * body : [{"re_user":"zttt","re_content":"adasdadasd","re_date":"201405050050","pre_content":"asdasd"},{"re_user":"zttt","re_content":"20181009测试回复1","re_date":"anything","pre_content":"测试1"},{"re_user":"zttt","re_content":"20181009测试回复2","re_date":"anything","pre_content":"测试2"}]
     * status : 1
     * msg : 成功
     */

    @SerializedName("body")
    private List<HistoryItem> HistoryBottles;

    public List<HistoryItem> getHistoryBottles() {
        return HistoryBottles;
    }

    public void setHistoryBottles(List<HistoryItem> HistoryBottles) {
        this.HistoryBottles = HistoryBottles;
    }

    public static class HistoryItem {
        /**
         * re_user : zttt
         * re_content : adasdadasd
         * re_date : 201405050050
         * pre_content : asdasd
         */

        private String re_user;
        private String re_content;
        private String re_date;
        private String pre_content;

        public String getRe_user() {
            return re_user;
        }

        public void setRe_user(String re_user) {
            this.re_user = re_user;
        }

        public String getRe_content() {
            return re_content;
        }

        public void setRe_content(String re_content) {
            this.re_content = re_content;
        }

        public String getRe_date() {
            return re_date;
        }

        public void setRe_date(String re_date) {
            this.re_date = re_date;
        }

        public String getPre_content() {
            return pre_content;
        }

        public void setPre_content(String pre_content) {
            this.pre_content = pre_content;
        }
    }
}
