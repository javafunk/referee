#!/bin/bash

if [[ "$DEBUG" == "true" ]]; then
    set -x
fi

set -e
set -o pipefail

sudo yum install -y gnupg

if ! gpg --list-keys 44CE9D82; then
    gpg --import /var/snap-ci/repo/tclemson.gpg.key
fi

cp -f /var/snap-ci/repo/gradle.properties .

./gradlew uploadArchives

REPO_ID=$(./gradlew nexusStagingList | grep "Repository ID" | cut -d ' ' -f 3 | cut -d ',' -f  1)

./gradlew nexusStagingClose -PrepoId=$REPO_ID
sleep 1m
./gradlew nexusStagingPromote -PrepoId=$REPO_ID