PROJECT_ID="todo-backend-488906"
PROJECT_NUMBER="$(gcloud projects describe "$PROJECT_ID" --format='value(projectNumber)')"
POOL_ID="github-pool-2"
PROVIDER_ID="github-provider"
GITHUB_ORG="Sathwik279"
REPO="todo-backend"
SA_NAME="github-cloudrun-deployer"
SA_EMAIL="${SA_NAME}@${PROJECT_ID}.iam.gserviceaccount.com"

WORKLOAD_IDENTITY_POOL_ID="projects/${PROJECT_NUMBER}/locations/global/workloadIdentityPools/${POOL_ID}"

gcloud iam workload-identity-pools create "$POOL_ID" \
 --project="$PROJECT_ID" \
 --location="global" \
 --display-name="Github Actions pool"

 gcloud iam workload-identity-pools providers create-oidc "$PROVIDER_ID" \
 --project="$PROJECT_ID" \
 --location="global" \
 --workload-identity-pool="$POOL_ID" \
 --display-name="Github provider" \
 --issuer-uri="https://token.actions.githubusercontent.com" \
 --attribute-mapping="google.subject=assertion.sub,attribute.actor=assertion.actor,attribute.repository=assertion.repository" \
 --attribute-condition="assertion.repository_owner == '${GITHUB_ORG}'"

gcloud iam service-accounts add-iam-policy-binding "$SA_EMAIL" \
 --project="$PROJECT_ID" \
 --role="roles/iam.workloadIdentityUser" \
 --member="principalSet://iam.googleapis.com/${WORKLOAD_IDENTITY_POOL_ID}/attribute.repository/${GITHUB_ORG}/${REPO}"

 echo "projects/${PROJECT_NUMBER}/locations/global/workloadIdentityPools/${POOL_ID}/providers/${PROVIDER_ID}"
