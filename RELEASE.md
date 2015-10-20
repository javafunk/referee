Referee
=======

Release Steps
-------------

In order to release, you need a gradle.properties file in the project root directory containing something like:

```
signing.keyId=<gpg-key-id>
signing.password=<gpg-passphrase>
signing.secretKeyRingFile=<gpg-secring-file-location>

oss-releases.username=<sonatype-username>
oss-releases.password=<sonatype-password>
oss-releases.url=<sonatype-url>
```

Once this is present, execute the following commands:

```
./gradlew uploadArchives

export REPO_ID=$(./gradlew nexusStagingList | grep "Repository ID" | cut -d ' ' -f 3 | cut -d ',' -f 1)

./gradlew nexusStagingClose -PrepoId=$REPO_ID
./gradlew nexusStagingPromote -PrepoId=$REPO_ID

# or maybe just

./gradlew nexusStagingRelease
```