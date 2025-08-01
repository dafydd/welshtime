#!/usr/bin/env bash
set -euo pipefail

# release versions are only valid if on main branch
# anywhere else must navigate by check-in hash
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [[ "$CURRENT_BRANCH" != "main" ]]; then
    echo "$CURRENT_BRANCH isn't 'main' so VERSION set to 'SNAPSHOT'"
    VERSION=$(cat VERSION)
    PATCH="SNAPSHOT"
    FULL="SNAPSHOT"
else
    VERSION=$(cat VERSION)
    PATCH=$(git rev-list --count HEAD)
    FULL="$VERSION.$PATCH"
fi

mkdir -p app/src/main/resources
echo "$FULL" > app/src/main/resources/RELEASE_VERSION
echo "established RELEASE_VERSION:$FULL"