package com.app.order;

import com.business.gcp.PubSubRoute;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@PubSubRoute(topic = "order-topic", entity = "Order")
@Data
public class OrderRequest {

    @NotBlank
    private String customerName;

    private List<String> items;
}
