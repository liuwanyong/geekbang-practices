package com.geektimes.projects.user.enums;

public enum UserType {

    NORMAL,
    VIP;

    UserType(){

    }

    public static void main(String[] args){
        UserType.VIP.ordinal();
    }
}
