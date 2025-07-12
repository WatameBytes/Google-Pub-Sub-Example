package com.app.models;

import lombok.Data;

@Data
public class OfferedService {
    private String name;
    private String uuid;
    private OfferedType type;   // enum below
}