package com.annatala.pixelponies.actors;

/**
 * Created by Trickster on 9/19/2016.
 */
public enum Gender {
    FEMININE("feminine"), MASCULINE("masculine"), NEUTER("neuter");

    private String simpleName;

    Gender(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        return simpleName;
    }

    public static Gender getEnum(String fetchName) {
        Gender targetGender = null;
        for (Gender gender : Gender.values()) {
            if (gender.simpleName.equals(fetchName)) {
                targetGender = gender;
            }
        }
        return targetGender;
    }

}
