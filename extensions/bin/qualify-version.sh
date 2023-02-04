#!/usr/bin/env bash

#set -x
set -e
set -u
set -o pipefail
set -o noclobber
shopt -s nullglob

# stack overflow #59895
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do
  DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE"
done
DIR="$(cd -P "$(dirname "$SOURCE")" && pwd)"

cd "$DIR/.."

MVN_VER=$(./mvnw org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate \
  -Dexpression=project.version -q -DforceStdout)

GIT_BR=$(git symbolic-ref --short HEAD)
if [[ $MVN_VER == *"-SNAPSHOT" ]]; then

  MVN_VER="${MVN_VER%SNAPSHOT}"${GIT_BR}-SNAPSHOT
else
  MVN_VER="${MVN_VER}"-${GIT_BR}
fi

echo $MVN_VER

./mvnw --no-transfer-progress versions:set -DnewVersion=$MVN_VER

#mvn versions:revert
#mvn versions:commit
