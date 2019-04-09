# Match on these pod name stubs. Values are container names we want to follow.

%podtypes = (
    "maker",  "maker-bot",
    "instrument", "instrument-craft-shop",
    "dbwrapper",  "dbwrapper",
    "pipeline", "pipeline-node",
    );

while (<>) 
{
    for $pattern (keys %podtypes) {
	if ($_ =~ /($pattern.+?) /) {
	    print "Found pod $1 with container $podtypes{$pattern}\n";
	    $pods{$1} = $podtypes{$pattern};
	}
    }
}
    
    
$cmd = " -hold -fa 'Monospace' -fs 10 -e kubectl logs -f";

for (keys %pods) {
     system("xterm -T $_ $cmd $_  $pods{$_} &");
}


    
