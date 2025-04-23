package com.test0423;

public class Car {

    //변수 선언
    String color;
    int speed = 0;

    static int carCount = 0;
    final static int MAXSPEED = 200;
    final static int MINSPEED = 0;

    static int currentCarCount(){
        return carCount;
    }

    public Car(String color, int speed) {
        this.color = color;
        this.speed = speed;
        carCount++;
    }

    //메서드 선언
    int getSpeed() {
        return speed;
    }

    void upSpeed(int value) {
        if(speed + value >= 200){
            speed = 200;
        } else {
            speed = speed + value;
        }
    }

    void downSpeed(int value) {
        if(speed-value <= 0){
            speed=0;
        }else {
            speed = speed-value;
        }
    }

    String getColor(){
        return color;
    }

}
