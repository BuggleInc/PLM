#! /bin/sh

cp=../src/:.:`grep 'kind="lib"' .classpath|sed -e 's|.*path="|../|' -e 's/".*//'|tr '\n' :`
cd bin
java -cp $cp jlm.core.model.TestRunner