#!/usr/bin/env bash

set -euo pipefail

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <buildnumber> <git-shasum>"
    exit 1
fi

# make the zip
./gradlew --console=plain --info :app:distZip


BUILDNUMBER="$1"
GITSHA="$2"
TEXT_FILE="app-${BUILDNUMBER}-${GITSHA}.txt"
DIST_DIR="app/build/distributions"
ZIP_FILE=$(find "$DIST_DIR" -name "*.zip" | head -n 1)
DATE=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
COMMIT_SHA="$GITHUB_SHA"
REPO="$GITHUB_REPOSITORY"
COMMIT_URL="https://github.com/$REPO/commit/$COMMIT_SHA"

# release versions are only valid if on main branch
# anywhere else must navigate by check-in hash
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$CURRENT_BRANCH" != "main" ]]; then
    VERSION=$(cat VERSION)
    PATCH="SNAPSHOT"
    FULL_VERSION="SNAPSHOT"
else
    VERSION=$(cat VERSION)
    PATCH=$(git rev-list --count HEAD)
    FULL_VERSION="$VERSION.$PATCH"
fi

if [ ! -f "$ZIP_FILE" ]; then
    echo "ZIP file not found."
    exit 1
fi

echo "Creating metadata file: $TEXT_FILE"
{
echo "commit:$GITSHA"
echo "build:$BUILDNUMBER"
echo "version:$FULL_VERSION"
echo "timestamp:$DATE"
echo "code:$COMMIT_URL"
} > "$TEXT_FILE"

echo "Adding $TEXT_FILE to ZIP archive: $ZIP_FILE"
zip -j "$ZIP_FILE" "$TEXT_FILE"

rm "$TEXT_FILE"
echo "Done. ZIP updated with: $TEXT_FILE"

BASEZIP="${ZIP_FILE%.*}"
mv "$ZIP_FILE" "$BASEZIP-${FULL_VERSION}-${BUILDNUMBER}-${GITSHA}.zip"
echo "Renamed zip file is $BASEZIP-${FULL_VERSION}-${BUILDNUMBER}-${GITSHA}.zip"
