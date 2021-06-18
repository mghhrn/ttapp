package io.github.mghhrn.tin.dto;

import com.google.gson.annotations.SerializedName;

public class LoginDto {

    @SerializedName("cellphone")
    private String cellphone;

    public LoginDto(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
