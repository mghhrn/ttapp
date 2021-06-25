package io.github.mghhrn.tin.dto;

import com.google.gson.annotations.SerializedName;

public class SmsVerificationDto {

    @SerializedName("cellphone")
    private String cellphone;

    @SerializedName("verificationCode")
    private String verificationCode;

    public SmsVerificationDto() {}

    public SmsVerificationDto(String cellphone, String verificationCode) {
        this.cellphone = cellphone;
        this.verificationCode = verificationCode;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
