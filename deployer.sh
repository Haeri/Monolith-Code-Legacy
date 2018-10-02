#!/bin/sh

# copy jar file
cp "./build/libs/monolith.jar" "./Package/monolith.jar"

# zip package
cd "./Package"
zip -r "../package.zip" *
cd ..

# read relese values from file
version=$(grep -oP "^version:\k(.*)$" release.txt)
build=$(grep -oP "^build:\K(.*)$" release.txt)
beta=$(grep -oP "^beta:\K(.*)$" release.txt)
refresh=$(grep -oP "^refresh:\K(.*)$" release.txt)
text=$(tail -n +5 release.txt)

# send package to server
curl -F "key=$1" -F "version=$version" -F "build=$build" -F "beta=$beta" -F "need_fresh=$fresh" -F "release_notes=Some random notes to be honest" -F "file=@package.zip" http://monolith-code.net.tiberius.sui-inter.net/release/addRelease