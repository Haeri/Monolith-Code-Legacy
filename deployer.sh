#!/bin/sh

# copy jar file
cp "./build/libs/monolith.jar" "./Package/monolith.jar"

# zip package
cd "./Package"
zip -r "../package.zip" *
cd ..

# read relese values from file
file < "./release.txt"
version = read file
build = read file
beta = read file
refresh = read file

echo "version: $version, build: $build, beta: $beta, refresh: $refresh"

# send package to server
curl -F "key=$1" -F "version=$version" -F "build=$build" -F "beta=$beta" -F "need_fresh=$fresh" -F "release_notes=Some random notes to be honest" -F "file=@package.zip" http://monolith-code.net.tiberius.sui-inter.net/release/addRelease