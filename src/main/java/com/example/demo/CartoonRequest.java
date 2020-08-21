package com.example.demo;

import java.util.List;

public class CartoonRequest {

    private String cartoon;
    private List<String> characterNames;

    public CartoonRequest(String cartoon, List<String> characterNames) {
        this.cartoon = cartoon;
        this.characterNames = characterNames;
    }

    public String getCartoon() {
        return cartoon;
    }

    public void setCartoon(String cartoon) {
        this.cartoon = cartoon;
    }

    public List<String> getCharacterNames() {
        return characterNames;
    }

    public void setCharacterNames(List<String> characterNames) {
        this.characterNames = characterNames;
    }

    @Override
    public String toString() {
        return "CartoonRequest{" +
                "cartoon='" + cartoon + '\'' +
                ", characterNames=" + characterNames +
                '}';
    }

}
