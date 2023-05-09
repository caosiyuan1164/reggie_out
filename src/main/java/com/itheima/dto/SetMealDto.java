package com.itheima.dto;

import com.itheima.domain.Setmeal;
import com.itheima.domain.SetmealDish;

import java.util.List;

public class SetMealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;

    public List<SetmealDish> getSetmealDishes() {
        return setmealDishes;
    }

    public void setSetmealDishes(List<SetmealDish> setmealDishes) {
        this.setmealDishes = setmealDishes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "SetMealDto{" +
                "setmealDishes=" + setmealDishes +
                ", categoryName='" + categoryName + '\'' +
                '}' + super.toString();
    }
}
