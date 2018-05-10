package com.xytsz.xytaj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 考题实体类
 * <p>
 *
 */
public class AnwerInfo extends BaseInfo {


    private List<SubDataBean> list;

    public List<SubDataBean> getList() {
        return list;
    }

    public void setList(List<SubDataBean> list) {
        this.list = list;
    }

    public static class SubDataBean implements Serializable {
        /**
         * questionid : 1
         * question : 测试题主要为测试题主要为测试题主要为测试题主要为测试题1
         * optiona : A选项
         * optionb : B选项
         * optionc : C选项
         * optiond : D选项
         * answer : A
         * mark : 5
         */

        private int questionid;
        private String question;
        private String optiona;
        private String optionb;
        private String optionc;
        private String optiond;
        private String answer;
        private int mark;
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isVisibility() {
            return visibility;
        }

        public void setVisibility(boolean visibility) {
            this.visibility = visibility;
        }

        private boolean visibility;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        private String userAnswer;
        private int score;

        public int getQuestionid() {
            return questionid;
        }

        public void setQuestionid(int questionid) {
            this.questionid = questionid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getOptiona() {
            return optiona;
        }

        public void setOptiona(String optiona) {
            this.optiona = optiona;
        }

        public String getOptionb() {
            return optionb;
        }

        public void setOptionb(String optionb) {
            this.optionb = optionb;
        }

        public String getOptionc() {
            return optionc;
        }

        public void setOptionc(String optionc) {
            this.optionc = optionc;
        }

        public String getOptiond() {
            return optiond;
        }

        public void setOptiond(String optiond) {
            this.optiond = optiond;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }
    }
}
