package com.bsw.mydemo;

import org.junit.Test;

public class Blog {
    @Test
    public void order() {
        MySelf mySelf = new MySelf();
        mySelf.cookColaChickenWings(new CuteGirl());
    }
}

class ColaChickenWings {
    private Calorie calorie = new Calorie();
    private Garlic garlic = new Garlic();

    public Calorie getCalorie() {
        return calorie;
    }

    public void setCalorie(Calorie calorie) {
        this.calorie = calorie;
    }

    public Garlic getGarlic() {
        return garlic;
    }

    public void setGarlic(Garlic garlic) {
        this.garlic = garlic;
    }

    class Calorie {

    }

    class Garlic {

    }
}

class MySelf {
    public void cookColaChickenWings(CuteGirl cuteGirl) {
        cuteGirl.eat(new ColaChickenWings());
    }
}

class CuteGirl {
    public void eat(ColaChickenWings colaChickenWings) {
        if (null != colaChickenWings.getGarlic()) {
            colaChickenWings.setGarlic(null);
        }
        if (null != colaChickenWings.getCalorie()) {
            colaChickenWings.setCalorie(null);
        }
        System.out.println("味道还行");
    }
}