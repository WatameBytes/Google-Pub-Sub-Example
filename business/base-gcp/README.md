## Base-GCP

### 1. Enable Pub/Sub API

1. Go to [Google Cloud Console](https://console.cloud.google.com/).
2. Select your project (or create a new one).
3. In the search bar, type **"Pub/Sub"**.
4. Click on **"Pub/Sub"** under Products & Pages.
5. If prompted, click **"ENABLE API"**.

---

### 2. Create a Topic

1. In the Pub/Sub page, click **"CREATE TOPIC"**
2. Enter a Topic ID (e.g., ``order-topic``).
3. Leave other settings as default.
4. Click **"CREATE"**.

---

### 3. Create a Service Account

1. In the Cloud Console, click the hamburger menu ☰.
2. Navigate to **"IAM & Admin" → "Service Accounts"**.
3. Click **"CREATE SERVICE ACCOUNT"**.
4. Fill in:
    - **Service account name**: ``pubsub-publisher``.
    - **Service account ID**: (auto-filled).
    - **Description**: `Service account for publishing to Pub/Sub`.
5. Click **"CREATE AND CONTINUE"**.
6. In **"Grant this service account access to project"**:
    - Click the **role** dropdown.
    - Search for **"Pub/Sub Publisher"**.
    - Select it and click **"CONTINUE"**.
7. Click **"DONE"**.

---

### 4. Create and Download Service Account Key

1. Click on the service account you just created
2. Go to the **"KEYS"** tab.
3. Click **"ADD KEY" → "Create new key"**.
4. Choose **JSON** format.
5. Click **"CREATE"**.
6. The JSON key file will download automatically — **keep this safe!**

### 5. Configure the Service Account Key
1. Rename downloaded file to `pubsub-publisher.json`.
2. **Move** it into your project at `app/src/main/resources/`.
3. In your `app-application.properties`, add:
```properties
spring.cloud.gcp.credentials.location=classpath:pubsub-publisher.json
spring.cloud.gcp.project-id=<YOUR_PROJECT_ID>
```
4. Verify that your project ID and file path match your setup.

### 6. Inspecting Messages
**Via Console:**
1. In Pub/Sub, select your topic and click **Subscriptions**.
   1. Choose an existing subscription or click **CREATE SUBSCRIPTION**.
2. Click on the **"MESSAGES"** tab
3. Select the subscription, go to **Messages**, then click **PULL**.
