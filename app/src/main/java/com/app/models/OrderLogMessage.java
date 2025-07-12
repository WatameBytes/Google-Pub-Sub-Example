package com.app.models;

import com.business.gcp.PubSubRoute;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.Instant;

@Data
@PubSubRoute(topic = "order_logs_topic_koyori")
public class OrderLogMessage {

    @JsonProperty("correlationId")
    private String correlationId;

    /** RFC 3339 instant; BigQuery TIMESTAMP is UTC internally */
    @JsonProperty("log_data_ts")
    private Instant logDataTs;

    /** YYYYMMDD as INT64 (e.g. 20250601) */
    @JsonProperty("sample_datetime")
    private int sampleDatetime;

    @JsonProperty("log_details")
    private LogDetails logDetails;
}
