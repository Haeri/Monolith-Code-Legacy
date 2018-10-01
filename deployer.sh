#!/bin/sh
cp "./build/libs/monolith.jar" "./Package/monolith.jar"
zip -r "package.zip" "./Package/"
curl -F "key=$1" -F "version=1.0.0" -F "build=60" -F "beta=false" -F "need_fresh=false" -F "release_notes=Some random notes to be honest" -F "file=@package.zip" http://monolith-code.net.tiberius.sui-inter.net/release/addRelease