package com.example.safwat.carstation.Model;

/**
 * Created by minato on 5/2/2017.
 */

public class ModelOfMainMenu {
    private int image ;
    private String name;

    public ModelOfMainMenu(int image, String name){
        this.image= image;
        this.name = name;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
