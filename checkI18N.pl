#! /usr/bin/perl
#
# Search for the classical errors about the translations

use strict;

my $error = 0;

# Check that every exercise is present in the po4a configuration
sub all_exo_are_translated() {
    my %todo;
    # Search exercises on disk
    my @exercises = qx(find resources -name '*Entity.java');
    foreach my $exo (sort @exercises) {
	chomp ($exo);
	next if $exo =~ /turtleart/; # no mission text for those exos
	$exo =~ s/Entity.java//;
	$todo{"$exo"} = 1;
#	print "TODO '$exo'\n";
    }
    # Search hints for common errors on disk
    my @hints = qx(find resources -name '*CommonErr*.html');
    foreach my $h (sort @hints) {
	chomp ($h);
	next unless $h =~ m|CommonErr..html|;
	$h =~ s/.html$//;
	$todo{"$h"} = 1;
#	print "TODO '$h'\n";
    }
    # Search lessons on disk
    my @lessons = qx(find resources/lessons -name 'description.html');
    foreach my $lesson (sort @lessons) {
	chomp ($lesson);
	$lesson =~ s/.html$//;
	$todo{"$lesson"} = 1;
#	print "TODO '$lesson'\n";
    }
    # Search world documentation on disk
    my @worlds = qx(find src -name '*World.html');
    foreach my $w (sort @worlds) {
	chomp ($w);
	$w =~ s/.html$//;
	$todo{"$w"} = 1;
    }
    
    # read the config file, removing the exo we find from the todo
    my $FH;
    open (FH,"<po4a.conf") || die "Cannot open po4a.conf: $!\n";
    while (my $line = <FH>) {
#	next unless $line =~ m|resources/exercises|;
	next unless $line =~ m|\[type: html\]|;
	chomp($line);
	my $exo = $line;
	$exo =~ s/\[type: html\] //;
	$exo =~ s/.html.*$//;
	if (defined $todo{"$exo"}) {
#	    print "found $exo\n";
	    delete $todo{"$exo"};
	} else {
	    $error++;
	    print "$exo: only in po4a.conf\n";
	}
	my $tr = $line;
	$tr =~ s/^.*\$lang:(.*?).\$lang.html/$1/;
	if ($exo ne $tr) {
	    $error++;
	    print "Orig and translated do not match: $line\n";
	}
    }
    
    # Complain about the exos that are not translated
    foreach my $exo (sort keys %todo) {
	$error ++;
	print "$exo: missing in po4a.conf\n";
    }
}

all_exo_are_translated();
print "#errors: $error\n";