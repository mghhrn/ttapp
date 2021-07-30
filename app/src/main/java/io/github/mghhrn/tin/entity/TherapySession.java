package io.github.mghhrn.tin.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "therapy_session")
public class TherapySession {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "selected_frequency")
    private double selectedFrequency;

    @ColumnInfo(name = "filtered_range")
    private int filteredRange;

    @ColumnInfo(name = "audio_balance")
    private String audioBalance;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "started_at")
    private Date startedAt;

    @ColumnInfo(name = "volume")
    private int volume;

    @ColumnInfo(name = "user_id")
    private long userId;

    @ColumnInfo(name = "satisfaction_point")
    private int satisfactionPoint;

    @ColumnInfo(name = "sent_to_server")
    private boolean sentToServer = false;


    public TherapySession() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSelectedFrequency() {
        return selectedFrequency;
    }

    public void setSelectedFrequency(double selectedFrequency) {
        this.selectedFrequency = selectedFrequency;
    }

    public int getFilteredRange() {
        return filteredRange;
    }

    public void setFilteredRange(int filteredRange) {
        this.filteredRange = filteredRange;
    }

    public String getAudioBalance() {
        return audioBalance;
    }

    public void setAudioBalance(String audioBalance) {
        this.audioBalance = audioBalance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSatisfactionPoint() {
        return satisfactionPoint;
    }

    public void setSatisfactionPoint(int satisfactionPoint) {
        this.satisfactionPoint = satisfactionPoint;
    }

    public boolean isSentToServer() {
        return sentToServer;
    }

    public void setSentToServer(boolean sentToServer) {
        this.sentToServer = sentToServer;
    }
}
