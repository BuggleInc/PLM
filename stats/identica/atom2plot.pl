#! /usr/bin/perl
use strict;
use Time::Local;

# Get all name IDs so that the numerical IDs are sorted alphabetically instead of first seen when sorted by time
# This is to avoid strange graphical effects 
my @id;
open IN, "<jlmlovers.atom" || die "Cannot open jlmlovers.atom: $!\n";
while (my $line = <IN>) {
    if ($line =~ m#<title>(.*?) solves (.*?)!</title>#) {
	push @id,$1;
    }
}
close IN;

my (%name2id); # name |-> numerical ID
my $maxID=0;
map { $name2id{$_} = $maxID++ unless defined($name2id{$_}) } sort @id;
    
open IN, "<jlmlovers.atom" || die "Cannot open jlmlovers.atom: $!\n";
open DATA, ">data" || die "Cannot open data: $!\n";

my ($name,$exo,$time);


while (my $line = <IN>) {
    if ($line =~ m#<title>(.*?) solves (.*?)!</title>#) {
	die "name defined twice at line $line\n" if (defined($name));
	die "exo defined twice at line $line\n" if (defined($exo));
	$name = $1;
	$exo = $2;
    } elsif ($line =~ m#<title>(.*?)</title>#) {
	print "(ignoring entry $1)\n";
	my $l;
	do { $l = <IN> } until ($l =~ m#</entry>#);
    }
    if ($line =~ m#published>(.*)</published>#) {
	die "name not defined at line $line\n" unless (defined($name));
	die "exo not defined at line $line\n" unless (defined($exo));
	
	die "time defined twice at line $line\n" if (defined($time));
	$time = $1;
	
	$time =~ s#\+00:00##; # cleanup the eventual time adjuster
	die "non-nul time adjuster" if ($time =~ m/\+/);
	
	unless (defined($name2id{$name})) {
	    $name2id{$name} = $maxID++;
	}
	die "Unparsable time $time" unless ($time =~ m/(\d\d\d\d)-(\d\d)-(\d\d)T(\d\d):(\d\d):(\d\d)/);
	my ($year,$mon,$day,$hour,$min,$sec) = ($1,$2,$3,$4,$5,$6);
	die "$year" unless $year = 2011;
	die "$mon" unless $mon = 9;
#	my $epoch = timelocal($sec,$min,$hour-2,$day,$mon,$year);
	
#	print DATA "# $name#$exo#$epoch#$time\n";
	$hour += 2;
	print DATA "$year-$mon-${day}T$hour:$min:$sec $name2id{$name}\n";
	($name,$exo,$time) = (undef,undef,undef);
    }
}
close DATA || die "Cannot write data: $!\n";

open CLASSES,">classes" || die "Cannot open classes data file: $!\n";
print CLASSES "2011-09-06T17:00:00 $maxID\n"; # Mar 6/9 - 16h-18h identica tracker were not enabled
print CLASSES "2011-09-07T15:00:00 $maxID\n"; # Mer 7/9 -  8h-10h
print CLASSES "2011-09-09T09:00:00 $maxID\n"; # Ven 9/9 -  8h-10h
print CLASSES "2011-09-09T11:00:00 $maxID\n"; # Ven 9/9 - 10h-12h
print CLASSES "2011-09-09T15:00:00 $maxID\n"; # Ven 9/9 - 14h-16h
  
# Lun 12/9 -  8h-12h
#  Lun 12/9 - 14h-18h
#  Mar 13/9 -  8h-12h
#  Mer 14/9 -  8h-10h
#  Mer 14/9 - 14h-16h
#  Jeu 15/9 -  8h-10h
#  Jeu 15/9 - 14h-16h
  

close CLASSES || die "Cannot write classes data file: $!\n";
print "Max ID: $maxID\n";
  