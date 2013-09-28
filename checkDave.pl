#! /usr/bin/perl

use strict;

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
	$line =~ s/throw new RuntimeException\(Game.i18n.tr\((.*?)\)\);/$1/;
	
	if      ($line =~ /"Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."/) {
	    
	    # traversals
	} elsif ($line =~ /"Sorry Dave, I cannot let you use forward\(\) in this exercise. Use setPos\(x,y\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use backward\(\) in this exercise. Use setPos\(x,y\) instead."/) {

	    # dogHouse
	} elsif ($line =~ /"Sorry Dave, I cannot let you use right\(\) in this exercise. Use left\(\) instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use left\(\) both in lines \{0\} and \{1\} in this exercise. You can write left\(\) only once in this exercise."/) {
	    
	    # mazes
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setX\(x\) in this exercise. Walk to your goal instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setY\(y\) in this exercise. Walk to your goal instead."/) {
	} elsif ($line =~ /"Sorry Dave, I cannot let you use setPos\(x,y\) in this exercise. Walk to your goal instead."/) {
	} else {
	    $error++;
	    print STDERR "$file : $line";
	}
    }
}
print "#errors: $error\n";