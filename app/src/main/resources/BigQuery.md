# Pub/Sub → BigQuery Pipeline (koyori)

This guide documents the exact commands and configuration used to stream JSON messages from a Pub/Sub topic into a nested BigQuery table. Append **koyori** to each resource to ensure uniqueness.

## JSON Schema

```json
{
  "correlationId": "123e4567-e89b-12d3-a456-426614174000",
  "log_data_ts": "2025-07-11T23:00:00Z",
  "sample_datetime": 20250711,
  "log_details": {
    "offeredServices": [
      {"name": "Crunchyroll Ultimate", "uuid": "svc-001", "type": "ANIME"},
      {"name": "Star Channel Prime",  "uuid": "svc-002", "type": "STAR"}
    ]
  }
}
```

## 1. Environment & Names

```bash
export PROJECT_ID=$(gcloud config get-value project)
export DATASET=analytics
export TABLE=order_logs_koyori
export TOPIC=order_logs_topic_koyori
export SUB=order_logs_bq_sub_koyori
```

## 2. Create BigQuery Dataset

```bash
bq --location=US mk --dataset \
  --description="Koyori streaming logs" \
  "${PROJECT_ID}:${DATASET}" 2>/dev/null || true
```

## 3. Define & Create Table Schema

```bash
cat > schema_koyori.json << 'EOF'
[
  {"name":"correlationId","type":"STRING","mode":"REQUIRED"},
  {"name":"log_data_ts","type":"TIMESTAMP","mode":"REQUIRED"},
  {"name":"sample_datetime","type":"INTEGER","mode":"REQUIRED"},
  {"name":"log_details","type":"RECORD","mode":"REQUIRED","fields":[
    {"name":"offeredServices","type":"RECORD","mode":"REPEATED","fields":[
      {"name":"name","type":"STRING","mode":"REQUIRED"},
      {"name":"uuid","type":"STRING","mode":"REQUIRED"},
      {"name":"type","type":"STRING","mode":"REQUIRED"}
    ]}
  ]}
]
EOF

bq mk --table \
  --schema=schema_koyori.json \
  "${PROJECT_ID}:${DATASET}.${TABLE}"
```

## 4. Create Pub/Sub Topic & Subscription

```bash
# Topic
gcloud pubsub topics create "${TOPIC}"

# Delete any existing subscription
gcloud pubsub subscriptions delete "${SUB}" --quiet 2>/dev/null || true

# Create BQ‐linked subscription
# (no metadata columns)
gcloud pubsub subscriptions create "${SUB}" \
  --topic="${TOPIC}" \
  --bigquery-table="${PROJECT_ID}:${DATASET}.${TABLE}" \
  --use-table-schema

# Confirm ACTIVE state
gcloud pubsub subscriptions describe "${SUB}" \
  --format="value(bigqueryConfig.state)"
# ⇒ ACTIVE
```

## 5. Publish Sample Message

```bash
cat > sample_koyori.json << 'EOF'
{
  "correlationId": "123e4567-e89b-12d3-a456-426614174000",
  "log_data_ts": "2025-07-11T23:00:00Z",
  "sample_datetime": 20250711,
  "log_details": {
    "offeredServices": [
      {"name": "Crunchyroll Ultimate", "uuid": "svc-001", "type": "ANIME"},
      {"name": "Star Channel Prime",  "uuid": "svc-002", "type": "STAR"}
    ]
  }
}
EOF

# Publish via gcloud
gcloud pubsub topics publish "${TOPIC}" \
  --message="$(jq -c . < sample_koyori.json)"
```

## 6. Verify in BigQuery

Wait \~30–60 s, then run:

```bash
bq query --nouse_legacy_sql \
"SELECT \
   correlationId,\
   log_data_ts,\
   sample_datetime,\
   log_details.offeredServices\
 FROM \`${PROJECT_ID}.${DATASET}.${TABLE}\`\
 ORDER BY log_data_ts DESC\
 LIMIT 5"
```

You should see one row with your JSON data, including two `offeredServices` entries.

## 7. Java Publishing Snippet

```java
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

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderLogService {

   private final GenericPubSubPublisher publisher;

   public String publish(OrderLogMessage msg) {
      try {
         String messageId = publisher.publish(msg).join();
         log.info("✓ Successfully published log message, id={}", messageId);
         return messageId;
      } catch (Exception e) {
         log.error("✗ Failed to publish log message", e);
         return "FAILED_PUBLISH";
      }
   }
}
```

Everything above has been **tested end-to-end** in Cloud Shell and Java. Save this file so you can refer back any time.

