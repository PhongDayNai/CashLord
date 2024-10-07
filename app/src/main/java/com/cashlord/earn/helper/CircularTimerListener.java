package com.cashlord.earn.helper;

public interface CircularTimerListener {
    String updateDataOnTick(long remainingTimeInMs);
    void onTimerFinished();
}