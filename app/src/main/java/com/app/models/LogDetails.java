package com.app.models;

import lombok.Data;
import java.util.List;

@Data
public class LogDetails {
    private List<OfferedService> offeredServices;
}
