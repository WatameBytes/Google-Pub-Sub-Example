# Google Pub/Sub Modular Monolith


A single Spring Boot application (`app/`) plus a set of Gradle sub‑modules that
keep infrastructure code (`business/base-gcp`) and business features
(`business/calculator`, etc.).

## Project Layout
```
Google-Pub-Sub-Example/
├── settings.gradle
├── build.gradle
├── app/
│   └── src/main/java/com/app/
│       ├── api/
│       ├── config/
│       ├── order/
│       └── Application.java
└── business/
    ├── base-gcp/
    └── calculator/
```

## Endpoints
### Calculator
```
GET http://localhost:8080/api/calc/add?a=5&b=3
```

### Order [Google Publisher]
```
POST 
http://localhost:8080/api/order
{
  "customerName": "Customer",
  "items": [
    "item 1",
    "item 2"
  ]
}

POST 
http://localhost:8080/api/order/async
{
  "customerName": "Customer",
  "items": [
    "item 1",
    "item 2"
  ]
}
```