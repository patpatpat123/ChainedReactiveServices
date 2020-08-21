package com.example.demo;

import java.util.List;

public class CartoonCharacter {

    private List<Integer> characterIds;

    public CartoonCharacter(List<Integer> characterIds) {
        this.characterIds = characterIds;
    }

    public List<Integer> getCharacterIds() {
        return characterIds;
    }

    public void setCharacterIds(List<Integer> characterIds) {
        this.characterIds = characterIds;
    }

    @Override
    public String toString() {
        return "CartoonCharacter{" +
                "characterIds=" + characterIds +
                '}';
    }

}
