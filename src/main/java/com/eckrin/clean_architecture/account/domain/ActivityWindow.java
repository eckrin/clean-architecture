package com.eckrin.clean_architecture.account.domain;

import lombok.NonNull;

import java.util.Collections;
import java.util.List;

public class ActivityWindow {

    private List<Activity> activities;

    public ActivityWindow(@NonNull List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }

}