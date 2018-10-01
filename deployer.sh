#!/bin/sh
cp "./build/libs/monolith.jar" "./Package/monolith.jar"
zip -r "package.zip" "./Package/*"
read version < "./release.txt"
read build < "./release.txt"
read beta < "./release.txt"
read refresh < "./release.txt"
echo "version: $version, build: $build, beta: $beta, refresh: $refresh"
curl -F "key=$1" -F "version=$version" -F "build=$build" -F "beta=$beta" -F "need_fresh=$fresh" -F "release_notes=Some random notes to be honest" -F "file=@package.zip" http://monolith-code.net.tiberius.sui-inter.net/release/addRelease