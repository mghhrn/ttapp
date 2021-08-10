package io.github.mghhrn.tin.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TherapySessionDto {

    @SerializedName("selectedFrequency")
    private Double selectedFrequency;

    @SerializedName("filteredRange")
    private Integer filteredRange;

    @SerializedName("audioBalance")
    private String audioBalance;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("startedAt")
    private Long startedAt;

    @SerializedName("volume")
    private Integer volume;

    @SerializedName("satisfactionPoint")
    private Integer satisfactionPoint;

    public TherapySessionDto() {
    }

    public TherapySessionDto(Double selectedFrequency, Integer filteredRange, String audioBalance, Integer duration, Long startedAt, Integer volume, Integer satisfactionPoint) {
        this.selectedFrequency = selectedFrequency;
        this.filteredRange = filteredRange;
        this.audioBalance = audioBalance;
        this.duration = duration;
        this.startedAt = startedAt;
        this.volume = volume;
        this.satisfactionPoint = satisfactionPoint;
    }

    public Double getSelectedFrequency() {
        return selectedFrequency;
    }

    public void setSelectedFrequency(Double selectedFrequency) {
        this.selectedFrequency = selectedFrequency;
    }

    public Integer getFilteredRange() {
        return filteredRange;
    }

    public void setFilteredRange(Integer filteredRange) {
        this.filteredRange = filteredRange;
    }

    public String getAudioBalance() {
        return audioBalance;
    }

    public void setAudioBalance(String audioBalance) {
        this.audioBalance = audioBalance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getSatisfactionPoint() {
        return satisfactionPoint;
    }

    public void setSatisfactionPoint(Integer satisfactionPoint) {
        this.satisfactionPoint = satisfactionPoint;
    }
}
