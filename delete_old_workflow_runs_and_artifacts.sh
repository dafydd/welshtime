#!/bin/bash

export GH_PAGER=""

DRY_RUN=false
if [[ "$1" == "--dry-run" ]]; then
   DRY_RUN=true
   echo "🔍 Dry run mode enabled. No deletions will be performed."
fi

GH_USERNAME=$(gh auth status 2>/dev/null | awk '/account/ {for (i=1; i<=NF; i++) if ($i == "account") print $(i+1)}')
echo "GitHub Username: $GH_USERNAME"
if [[ -z "$GH_USERNAME" ]]; then
    echo "❌ Error: Not logged into GitHub CLI. Run 'gh auth login' first."
    exit 1
fi

PROJECT_NAME=$(basename -s .git "$(git config --get remote.origin.url)")
if [[ -z "$PROJECT_NAME" ]]; then
    echo "❌ Error: No remote GitHub repository found. Is this a Git repo?"
    exit 1
fi

echo "🔹 GitHub User: $GH_USERNAME"
echo "🔹 Repository: $PROJECT_NAME"
echo "🔹 Cleaning up old workflow runs and artifacts..."

# Delete Workflow Runs Older Than 30 Days 
# macOS (BSD date)
if date -v -30d &>/dev/null; then
    DATE_30_DAYS_AGO=$(date -u -v -30d +"%Y-%m-%dT%H:%M:%SZ")
else
    # Linux (GNU date)
    DATE_30_DAYS_AGO=$(date -u -d "30 days ago" +"%Y-%m-%dT%H:%M:%SZ")
fi

WORKFLOW_RUNS=$(gh run list --limit 100 --json databaseId,createdAt --jq '[.[] | select(.createdAt < "'"$DATE_30_DAYS_AGO"'") | .databaseId]')
if [[ -z "$WORKFLOW_RUNS" ]]; then
    echo "✅ No old workflow runs to delete."
else
    echo "🗑️ Workflow runs to be deleted:"
    echo "$WORKFLOW_RUNS"

    if [[ "$DRY_RUN" == "false" ]]; then
       echo "🚨 Deleting workflow runs..."
       echo "$WORKFLOW_RUNS" | xargs -I {} gh run delete {}
       echo "✅ Workflow runs deleted."
    else
       echo "🛑 Dry run mode: No workflow runs deleted."
    fi
fi

# Delete All Remaining Artifacts (Orphaned or Not)
ARTIFACT_IDS=$(gh api repos/$GH_USERNAME/$PROJECT_NAME/actions/artifacts --jq '.artifacts | .[].id')

if [[ -z "$ARTIFACT_IDS" ]]; then
    echo "✅ No artifacts found. Nothing to delete."
else
    echo "🗑️ Artifacts to be deleted:"
    echo "$ARTIFACT_IDS"

    if [[ "$DRY_RUN" == "false" ]]; then
       echo "🚨 Deleting artifacts..."
       echo "$ARTIFACT_IDS" | xargs -I {} gh api repos/$GH_USERNAME/$PROJECT_NAME/actions/artifacts/{} -X DELETE
       echo "✅ All artifacts deleted."
    else
       echo "🛑 Dry run mode: No artifacts deleted."
    fi
fi

echo "✅ Cleanup complete!"
