package com.xytsz.xytaj.bean;

/**
 * Created by admin on 2017/3/20.
 *
 *
 */
public class Student {


    private String name;

    private String sex;

    private Student(Builder builder){
        this.name=builder.name;
        this.sex=builder.sex;
    }


    static class Builder {


        private String name;

        private String sex;


        public  Builder setName(String name){

            this.name=name;

            return this;
        }


        public  Builder setAge(String sex){

            this.sex=sex;

            return this;
        }

        public Student build(){


            return new Student(this);
        }


    }

    Student.Builder builder=new Student.Builder();

    Student student=builder.setName("").setAge("").build();


}
