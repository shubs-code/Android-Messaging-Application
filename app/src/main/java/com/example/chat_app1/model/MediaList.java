package com.example.chat_app1.model;

public class MediaList {
    private String fileName;
    private String filePath;
    private String fileSize;
    private boolean selected;
    private long timeMillis;

    public MediaList(String fileName, String filePath, String fileSize, boolean selected, long timeMillis) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.selected = selected;
        this.timeMillis = timeMillis;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
}
