package io.github.mghhrn.tin.dto;

import com.google.gson.annotations.SerializedName;

public class ProfileDto {

    @SerializedName("gender")
    private String gender;

    @SerializedName("age")
    private Long age;

    public ProfileDto(String gender, Long age) {
        this.gender = gender;
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
