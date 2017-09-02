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
    
    for my $targetLang (@langs) {
	my $text = join(":", @arr);
	
	$text =~ s#\[\!thelang/?\]#\[\!java\]Java\[/\!\]\[\!python\]python\[/\!\]\[\!scala\]Scala\[/\!\]\[\!c\]C\[/\!\]#sg;

	# Check for typos
	for my $lang1 (@langs) {
	    error "$file: [$lang1] instead of [\!$lang1]."
	      if ($text =~ /\[$lang1\]/);
	    for my $lang2 (@langs) {
		error "$file: [$lang1|$lang2] instead of [!$lang1|$lang2]."
		  if ($text =~ /\[$lang1\|$lang2\]/);
		for my $lang3 (@langs) {
		    error "$file: [$lang1|$lang2|$lang3] instead of [!$lang1|$lang2|$lang3]."
		      if ($text =~ /\[$lang1\|$lang2\|$lang3\]/);
		}
	    }
	}
	error "$file: [!/] instead of [/!]." 
	  if ($text =~ m|\[\!/\]|);

        # Check that the conversion does not leave any marker over, regardless of the language
	#
	
	# Change the one-language markers
	for my $lang1 (@langs) {
	    if ($lang1 eq $targetLang) {
		$text =~ s|\[\!$lang1\](.*?)\[/\!\]|$1|sg; # keep it
	    } else {
		$text =~ s|\[\!$lang1\](.*?)\[/\!\]||sg;   # remove it
	    }
	}
	
	# Change the two-languages markers
	for my $lang1 (@langs) {
	    for my $lang2 (@langs) {
		next if $lang1 eq $lang2;
		if ($lang1 eq $targetLang or $lang2 eq $targetLang) {
		    $text =~ s#\[\!$lang1\|$lang2\](.*?)\[/\!\]#$1#sg; # keep it
		} else {
		    $text =~ s#\[\!$lang1\|$lang2\](.*?)\[/\!\]##sg;   # remove it
		}
	    }
	}
	# Change the three-languages markers
	for my $lang1 (@langs) {
	    for my $lang2 (@langs) {
		next if $lang1 eq $lang2;
		for my $lang3 (@langs) {
		    next if $lang1 eq $lang3 or $lang2 eq $lang3;
		    if ($lang1 eq $targetLang or $lang2 eq $targetLang or $lang3 eq $targetLang) {
			$text =~ s#\[\!$lang1\|$lang2\|$lang3\](.*?)\[/\!\]#$1#sg; # keep it
		    } else {
			$text =~ s#\[\!$lang1\|$lang2\|$lang3\](.*?)\[/\!\]##sg;   # remove it
		    }
		}
	    }
	}
	error "$file: [! remains after converting to $targetLang.\n$text\nxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n"
	  if ($text =~ m|\[\!|s);
#	print "LANG: $targetLang\n$text\n\n\n\n";
    }
}

print "$errorCount errors found.\n";
exit $errorCount;