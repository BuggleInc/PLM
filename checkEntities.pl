#! /usr/bin/perl
#
# Search for the classical errors done when writing solution Entities

use strict;

my @files = qx(find resources/exercises -name '*Entity.java' -or -name '*Entity.scala' -or -name '*Entity.py');

my $error = 0;

file: foreach my $file (sort @files) {
    chomp ($file);
    my $FH;
    open (FH,"<$file") || die "Cannot open $file: $!\n";

    my $state = 0;
    my $seenTemplate = 0;
    my $seenSolution = 0;
    my $solutionHasContent = 0;
    while (<FH>) {
	if (/BEGIN SKEL/ or /END SKEL/) {
	    $error++;
	    print STDERR "$file: BEGIN SKEL or END SKEL\n";
	    next file;
	}
	
	# Check that the state machine is right
	if ($state eq 0) { # initial content
	    if (/BEGIN TEMPLATE/) {
		$state = 1;
		$seenTemplate = 1;
	    } elsif (/END TEMPLATE/) {
		$error++;
		print STDERR "$file: END TEMPLATE before BEGIN TEMPLATE\n";
		next file;
	    } elsif (/BEGIN SOLUTION/) {
		$state = 2; # Shortcut used in python when there is no template at all
	    } elsif (/END SOLUTION/) {
		$error++;
		print STDERR "$file: END SOLUTION before BEGIN TEMPLATE\n";
		next file;
	    }
	} elsif ($state eq 1) { # within TEMPLATE
	    if (/BEGIN TEMPLATE/) {
		$error++;
		print STDERR "$file: two BEGIN TEMPLATE\n";
		next file;
	    } elsif (/END TEMPLATE/) {
		$error++;
		print STDERR "$file: no content between BEGIN TEMPLATE and END TEMPLATE\n";
		next file;
	    } elsif (/BEGIN SOLUTION/) {
		$state = 2;
		$seenSolution = 1;
	    } elsif (/END SOLUTION/) {
		$error++;
		print STDERR "$file: END SOLUTION before BEGIN SOLUTION\n";
		next file;
	    }
	} elsif ($state eq 2) { # within SOLUTION
	    if (/BEGIN TEMPLATE/) {
		$error++;
		print STDERR "$file: BEGIN TEMPLATE after BEGIN SOLUTION\n";
		next file;
	    } elsif (/END TEMPLATE/) {
		$error++;
		print STDERR "$file: missing END SOLUTION before END TEMPLATE\n";
		next file;
	    } elsif (/BEGIN SOLUTION/) {
		$error++;
		print STDERR "$file: two BEGIN SOLUTION\n";
		next file;
	    } elsif (/END SOLUTION/) {
		$state = 3;
	    } elsif (/[a-zA-Z]/) {
		$solutionHasContent = 1;
	    }
	} elsif ($state eq 3) { # after SOLUTION
	    if (/BEGIN TEMPLATE/) {
		$error++;
		print STDERR "$file: two BEGIN TEMPLATE\n";
		next file;
	    } elsif (/END TEMPLATE/) {
		$state = 4;
	    } elsif (/BEGIN SOLUTION/) {
		$error++;
		print STDERR "$file: two BEGIN SOLUTION\n";
		next file;
	    } elsif (/END SOLUTION/) {
		$error++;
		print STDERR "$file: two END SOLUTION\n";
		next file;
	    }
	} elsif ($state eq 4) { # after TEMPLATE
	    if (/BEGIN TEMPLATE/) {
		$error++;
		print STDERR "$file: two BEGIN TEMPLATE\n";
		next file;
	    } elsif (/END TEMPLATE/) {
		$error++;
		print STDERR "$file: two END TEMPLATE\n";
		next file;
	    } elsif (/BEGIN SOLUTION/) {
		$error++;
		print STDERR "$file: two BEGIN SOLUTION\n";
		next file;
	    } elsif (/END SOLUTION/) {
		$error++;
		print STDERR "$file: two END SOLUTION\n";
		next file;
	    }
	}
    }

    # Done parsing the file
    if ($state eq 0) {
	$error++;
	print STDERR "$file: missing BEGIN TEMPLATE\n";
    } elsif ($state eq 1) {
	$error++;
	print STDERR "$file: missing BEGIN SOLUTION\n";
    } elsif ($state eq 2) {
	$error++;
	print STDERR "$file: missing END SOLUTION\n";
    } elsif (($state eq 3) and ($seenTemplate == 1)) {
	$error++;
	print STDERR "$file: missing END TEMPLATE\n";
    } elsif ($state eq 4 and $solutionHasContent eq 0) {
	$error++;
	print STDERR "$file: SOLUTION has no content\n";
    }
}
print "#errors: $error\n";