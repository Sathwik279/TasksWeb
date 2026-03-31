PROJECT_ID="todo-backend-488906"
REGION="asia-south1"
AR_REPO="backend-apps"
SERVICE_NAME="todoapp-backend"

gcloud config set project "$PROJECT_ID"
gcloud services enable run.googleapis.com artifactregistry.googleapis.com iamcredentials.googleapis.com

gcloud artifacts repositories create "$AR_REPO" \
  --repository-format=docker \
  --location="$REGION" \
  --description="Docker images for cloud run"


gcloud run services logs read todoapp-backend \
  --region="$REGION" \
  --project="$PROJECT_ID" \
  --limit=100