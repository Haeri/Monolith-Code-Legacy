#!/bin/sh

# copy jar file
cp "./build/libs/monolith.jar" "./Package/monolith.jar"

# zip package
cd "./Package"
zip -r "../package.zip" *
cd ..

# read relese values from file
version=(grep -o "^version:(.*)$" release.txt)
build=(grep -o "^build:(.*)$" release.txt)
beta=(grep -o "^beta:(.*)$" release.txt)
refresh=(grep -o "^refresh:(.*)$" release.txt)

echo "version: $version, build: $build, beta: $beta, refresh: $refresh"

# send package to server
curl -F "key=$1" -F "version=$version" -F "build=$build" -F "beta=$beta" -F "need_fresh=$fresh" -F "release_notes=Some random notes to be honest" -F "file=@package.zip" http://monolith-code.net.tiberius.sui-inter.net/release/addRelease