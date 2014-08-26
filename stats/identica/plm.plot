#set terminal pdf color
#set output "plmlovers.pdf"

set terminal png 
set output "plmlovers.png"
  
set xlabel "" 
#Time"
set ylabel "Individuals"

set grid xtics y2tics my2tics
set noytics
set xdata time
set timefmt "%Y-%m-%dT%H:%M:%S"
set format x "%m/%d\n%Hh"

set boxwidth 7200

#set style line 1 lt 2 lw 3
#set key box linestyle 1
#set key outside 

#set key below
#plot "classes" using 1:2 title "Class time"       with boxes fs solid 0.08,\
#     "data"    using 1:2 title "Exercises passed" with dots

plot "classes" using 1:2 notitle with boxes fs solid 0.08,\
     "data"    using 1:2 notitle with points pt 2


