package com.cashlord.earn;

public interface UpdateListener {
    void UpdateListener(
            String coin,
            String time,
            String link,
            String browser,
            String id,
            int index,
            String description,
            String logo,
            String packages
    );
}
