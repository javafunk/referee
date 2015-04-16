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

./gradlew uploadArchives \
    -Psigning.keyId=$SIGNING_KEY_ID \
    -Psigning.password=$SIGNING_PASSWORD \
    -Psigning.secretKeyRingFile=$SIGNING_SECRET_KEY_RING_FILE \
    -Poss-releases.username=$SONATYPE_USERNAME \
    -Poss-releases.password=$SONATYPE_PASSWORD \
    -Poss-releases.url=$SONATYPE_URL

./gradlew nexusStagingRelease \
    -Poss-releases.username=$SONATYPE_USERNAME \
    -Poss-releases.password=$SONATYPE_PASSWORD \
    -Poss-releases.url=$SONATYPE_URL