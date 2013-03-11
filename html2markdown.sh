#!/bin/bash

orig=''

if [ $# == 0 ]
then
	rep=$(pwd)
elif [ $# == 1 ]
then
    rep=$1
    orig=$(pwd)
    orig="$orig/"
else
	rep=$1
fi

if [[ -d $rep ]]
then

 ls -F $rep > tempo
 while read line;
    do
        var=${line: -1} 
        name=${line%.*}
        ext=${line##*.}
        
        if [[ $var == '/' ]]
        then
            ls -F "$rep/${line%/}" > tempo1
            while read line1;
                do
                    echo "${line%/}/${line1%}" >> tempo
                done < tempo1
         elif [[ $var != '@' ]] && [[ $var != '|' ]]
         then
            if [ "$ext" == "html" ]
        	then
        		`cat $rep/$name.html | python html2markdown.py > $rep/$name.md`
        		echo "Conversion de $rep/$name.html -> $rep/$name.md"
        	fi
        fi
    done < tempo

rm tempo*
else
 echo "Erreur $rep n'est pas un repertoire"
fi