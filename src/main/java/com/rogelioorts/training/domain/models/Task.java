package com.rogelioorts.training.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {

    public String id;

    public String description;

    public boolean completed;

}
