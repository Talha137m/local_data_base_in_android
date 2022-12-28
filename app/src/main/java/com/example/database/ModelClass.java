package com.example.database;

public class ModelClass {
    private int rollNo;
    private String name;
    private float fee;
    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public int getModelClassListPosition() {
        return getModelClassListPosition();
    }

    public ModelClass(String name, int rollNo, float fee) {
        this.name = name;
        this.rollNo = rollNo;
        this.fee = fee;
    }

    public ModelClass() {
    }

}
