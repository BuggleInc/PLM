#! /usr/bin/perl
use strict;

## Get the mission
while (<>) { 
    last if /<table border="0"><tbody><tr><td valign="top" width="700">/;
}
my $mission = $_;
while (<>) {
    last if /<br><br>/;
    $mission .= $_;
}
$mission =~ s/<[^>]*>//g;

## Get the package name
my $package = `pwd`;
chomp $package;
$package =~ s|/$||;
$package =~ s|.*/||;

## Get the prototype of the function to run
while (<>) { last if /spellcheck="false"/}
chomp;
my $proto = $_;
$proto =~ s/<[^>]*>//g;
$proto =~ s/public //;

## Get the tests
while (<>) { last if /Expected/;}

my $name;
my @worlds;
while (<>) {
    chomp;
    next unless /<tr>/;
    next if /other tests/;
    s/<tr><td>([^(]*)\(//; #)
    if (defined $name && $1 ne $name) {
	die "name redefined from $name to $1\n";
    }
    $name=$1;
    
    s/&.*//;
    s/{/new int[] {/g;
   
#    print "w:$_\n";
    push @worlds,$_;
}
my $upname= ucfirst $name;
print "Write $upname.java\n";
open J,">$upname.java" || die "Cannot open $upname.java: $!\n";

print J "/* automatically converted from the Nick Parlante's excellent exercising site http://javabat.com/ */\n\n";	
print J "package lessons.bat.$package;\n";
print J "import plm.lesson.Lesson;\n";
print J "import plm.universe.World;\n";
print J "import universe.bat.BatExercise;\n";
print J "import universe.bat.BatWorld;\n\n";
print J "public class $upname extends BatExercise {\n";
print J "  public $upname(Lesson lesson) {\n";
print J "    super(lesson);\n";
print J "    \n";
my $count = scalar @worlds;
print J '    World[] myWorlds = new BatWorld['.$count."];\n";
for (my $i=0;$i<scalar @worlds;$i++) {
print J '    myWorlds['.$i.'] = new BatWorld('.($i<3?'VISIBLE':'INVISIBLE').', '.$worlds[$i].";\n";
}
print J "\n    setup(myWorlds,\"$name\");\n";
print J "  }\n\n";
	
print J "  /* BEGIN SKEL */\n";
print J "  public void run(World w) {\n";
print J "    BatWorld bw = (BatWorld) w;\n";
print J "    bw.result = $name(";
my $protoargs = $proto;
$protoargs =~ s/[^(]*\(//; #))
$protoargs =~ s/\).*//;

my $argcount=0;
foreach (split (/,/,$protoargs)) {
    s/^ *//;
    s/ *$//;
    s/ .*//;
    s/int$/Integer/;
    s/boolean$/Boolean/;
    print J ", " if ($argcount>0);
    print J "($_)w.getParameter($argcount)";
    $argcount++;
}
print J ");\n";
print J "  }\n";
print J "  /* END SKEL */\n\n";

print J "  /* BEGIN TEMPLATE */\n";
print J "$proto\n";
print J "  /* BEGIN SOLUTION */\n";
print J "  /* END SOLUTION */\n";
print J "}\n";
print J "  /* END TEMPLATE */\n";
print J "}\n";
close J;

print "Write $upname.html\n";
open H,">$upname.html" || die "Cannot open $upname.html: $!\n";
print H "<h1>$upname</h1>\n";
print H "$mission\n";
print H "\n<p>This exercise was converted to PLM from the excellent exercising site http://javabat.com/</p>\n";
close H;

