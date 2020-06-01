#! /usr/bin/perl

use strict;

print("Check for inconsistencies in 'Sorry Dave' error messages (to ensure that translations works in all bindings)\n");

my @files = qx(find src -name '*.java' -or -name '*.scala' -or -name '*.py');

my $error = 0;

foreach my $file (sort @files) {
    chomp ($file);
    my $filectn;
    my $FH;
    open (FH,"<$file") || die "Cannot open $file: $!\n";
      
    while (<FH>) {
	next unless ($_ =~ /Dave/);
	my $line = $_;
#	$line =~ s/throw new RuntimeException\(Game.i18n.tr\((.*?)\)\);/$1/;
	$line =~ s/^.*?Game.i18n.tr\(("[^"]*")\).*$/$1/;
	$line =~ s/^.*?errorMsg\(("[^"]*")\).*$/$1/;
	
	if      ($line =~ /"Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."/) {
	    
	    # traversals
	} elsif ($line =~ /"Sorry Dave, I cannot let you use forward\(\) in this exercise. Use setPos\(x,y\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use backward\(\) in this exercise. Use setPos\(x,y\) instead."/) {

	    # dogHouse
	} elsif ($line =~ /"Sorry Dave, I cannot let you use right\(\) in this exercise. Use left\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use left\(\) both in lines \{0\} and \{1\} in this exercise. You can write left\(\) only once in this exercise."/) {
	    # welcome.Course
	} elsif ($line =~ /"Sorry Dave, you cannot run backward like this. Exercising is hard enough -- please don't overplay."/) {
	    
	    # mazes
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setX\(x\) in this exercise. Walk to your goal instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setY\(y\) in this exercise. Walk to your goal instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setPos\(x,y\) in this exercise. Walk to your goal instead."/) {

	    # Hanoi
	} elsif ($line =~ /"Sorry Dave, I cannot let you move disks between slots 0 and 2 directly. Use the intermediate slot in all moves."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you move disks counterclockwise. Move from 0 to 1, from 1 to 2 or from 2 to 0 only, not from \{0\} to \{1\}."/) {

	    # Buggles vs. Turtles
	} elsif ($line =~ /"Sorry Dave, I cannot let you use penDown\(\) here. Buggles have brushes, not pens. Use brushDown\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use penUp\(\) here. Buggles have brushes, not pens. Use brushUp\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use brushDown\(\) here. Turtles have pens, not brushes. Use penDown\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use brushUp\(\) here. Turtles have pens, not brushes. Use penUp\(\) instead."/) {

	    # Scala accepts Left() but that's still a bad idea
	} elsif ($line =~ /"Sorry Dave, I cannot let you use Left\(\) with an uppercase. Use left\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use Right\(\) with an uppercase. Use right\(\) instead."/) {

	} else {
	    $error++;
	    $line =~ s/^\w*//;
	    print STDERR "$file: $line";
	}
    }
}
print "#errors: $error\n";