package com.itheima.dto;

import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;

import java.util.ArrayList;
import java.util.List;

public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<DishFlavor>();

    private String categoryName;

    private Integer copies;

    public List<DishFlavor> getFlavors() {
        return flavors;
    }

    public void setFlavors(List<DishFlavor> flavors) {
        this.flavors = flavors;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "DishDto{" +
                "flavors=" + flavors +
                ", categoryName='" + categoryName + '\'' +
                ", copies=" + copies +
                '}' + super.toString();
    }
}
