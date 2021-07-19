package com.caletateam.caletapp.app.md.models;

public class Activity {
    private float left_arm, right_arm, down;

    public Activity(float left_arm, float right_arm, float down) {
        this.left_arm = left_arm;
        this.right_arm = right_arm;
        this.down = down;
    }

    /*Brazo derecho: sqrt( right ** 2 + up ** 2 )
    Brazo izquierdo: sqrt( left ** 2 + up ** 2 )
    Piernas: down*/
}
