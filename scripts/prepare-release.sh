#!/usr/bin/env bash
# Prepares a release on the current branch: sets the version, builds and tests, and adds the
# artifacts to this repository's Maven layout (the GitHub-hosted repository).
#
# Usage:   scripts/prepare-release.sh 1.0.1
# Maven:   uses `mvn` from PATH, or set MVN=/path/to/mvn
set -euo pipefail

VERSION="${1:-}"
MVN="${MVN:-mvn}"

cd "$(dirname "$0")/.."

if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
  echo "Usage: scripts/prepare-release.sh <major.minor.patch>, e.g. 1.0.1" >&2
  exit 1
fi
if [ "$(git branch --show-current)" = "main" ]; then
  echo "You're on main, which is protected. Create a branch first: git switch -c release/$VERSION" >&2
  exit 1
fi
if [ -d "io/github/oblivion97/jasper-font-solaimanlipi/$VERSION" ]; then
  echo "$VERSION is already released. Pick a new version." >&2
  exit 1
fi

"$MVN" -B -ntp -q versions:set -DnewVersion="$VERSION" -DgenerateBackupPoms=false
echo "pom.xml version set to $VERSION"

# On Git Bash (Windows), turn /c/Users/... into C:/Users/... so Maven gets a valid file URL
if command -v cygpath > /dev/null; then REPO_URL="file:///$(cygpath -m "$(pwd)")"; else REPO_URL="file://$(pwd)"; fi

# Builds, tests, and deploys the jar, sources and pom into the layout
"$MVN" -B -ntp clean deploy -DaltDeploymentRepository="layout::$REPO_URL"
"$MVN" -B -ntp -q clean
echo "Added version $VERSION to the GitHub-hosted Maven repository"

cat <<EOF

Next:
  Update the version in README.md, then:
  git add -A && git commit -m "Release $VERSION" && git push -u origin $(git branch --show-current)
  Open a PR, wait for CI, merge it. Then publish to Maven Central:
  git switch main && git pull && git tag v$VERSION && git push origin v$VERSION
EOF
