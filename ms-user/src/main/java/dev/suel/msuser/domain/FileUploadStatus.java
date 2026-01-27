package dev.suel.msuser.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class FileUploadStatus {
    private final List<String> errors = new ArrayList<>();
    private final Instant createdAt = Instant.now();
    private String id;
    private Status status = Status.STATUS_PROCESSING;
    private Long processed = 0L;
    private Long totalItems = 0L;

    public FileUploadStatus() {
    }

    public static FileUploadStatus withId(String id) {
        FileUploadStatus fileUploadStatus = new FileUploadStatus();
        fileUploadStatus.setId(id);
        return fileUploadStatus;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getProcessed() {
        return processed;
    }

    public void setProcessed(Long processed) {
        this.processed = processed;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Long totalItems) {
        this.totalItems = totalItems;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void increaseProcessedItems() {
        this.processed++;
    }

    public boolean isFinished() {
        return this.status == Status.STATUS_FINISHED || this.status == Status.STATUS_ERROR;
    }

    public enum Status {
        STATUS_ERROR,
        STATUS_PROCESSING,
        STATUS_FINISHED

    }
}
