PROJECT_ID="todo-backend-488906"
SA_NAME="github-cloudrun-deployer"
SA_EMAIL="${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com"


# ================================
# 4. CREATE SERVICE ACCOUNT
# ================================

gcloud iam service-accounts create "$SA_NAME" \
  --display-name="GitHub Cloud Run Deployer" || echo "SA may exist"

# ================================
# 5. ASSIGN ROLES TO SERVICE ACCOUNT
# ================================

gcloud projects add-iam-policy-binding "$PROJECT_ID" \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/run.admin"

gcloud projects add-iam-policy-binding "$PROJECT_ID" \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/iam.serviceAccountUser"

gcloud projects add-iam-policy-binding "$PROJECT_ID" \
  --member="serviceAccount:${SA_EMAIL}" \
  --role="roles/artifactregistry.writer"