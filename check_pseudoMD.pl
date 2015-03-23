#! /usr/bin/perl

use strict;

my @langs = qw,python java scala c,;

my $errorCount = 0;

sub error($) {
    print $_[0];
    $errorCount++;
}

print "Checking for errors about language pseudo-markup ([!python] etc)\n";

foreach my $file (qx(find src -name '*html')) {
    chomp $file;
    
    open my $fh, '<', $file or die "unable to open file '$file' for reading : $!";
    my @arr = <$fh>;
    close $fh;  # close as soon as possible
    
    for my $line (@arr) {
	for my $lang1 (@langs) {
	    error "$file: [$lang1] instead of [!$lang1].\n  $line\n"
	      if ($line =~ /\[$lang1\]/);
	    for my $lang2 (@langs) {
		error "$file: [$lang1|$lang2] instead of [!$lang1|$lang2].\n  $line\n"
		  if ($line =~ /\[$lang1\|$lang2\]/);
		for my $lang3 (@langs) {
		    error "$file: [$lang1|$lang2|$lang3] instead of [!$lang1|$lang2|$lang3].\n  $line\n"
		      if ($line =~ /\[$lang1\|$lang2\|$lang3\]/);
		}
	    }
	}
	error "$file: [!/] instead of [/!].\n  $line" 
	  if ($line =~ m|\[!/\]|);
    }
}

print "$errorCount errors found.\n";
exit $errorCount;