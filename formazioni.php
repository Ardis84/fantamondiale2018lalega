<?php

	


function getFormazioneByTeam($teamname){
	$amodeo = 	"Amodeo\n".
				" ------------------\n".
				" Lloris--->5\n".
				" Hummels--->3,5\n".
				" Boateng--->4,5\n".
				" Aldeiweireld--->?\n".
				" Perisic--->6\n".
				" Ljajic--->5,5\n".
				" J. Rodriguez--->?\n".
				" Grosicki--->?\n".
				" Muller--->3,5\n".
				" C. Ronaldo--->18\n".
				" Mitrovic--->5,5\n".
				" \n".
				" *****************\n".
				" Francia 2--->?\n".
				" Fazio--->*sv\n".
				" Kolarov--->*16\n".
				" Rakitic--->*5\n".
				" T. Alcantara--->*6\n".
				" Milik--->?\n".
				" Balde Keita--->?\n".
				" \n".
				" Totale--->51,5";
				
	if($teamname=='amodeo')
		return "\n".$amodeo;
}

function getFormazioni(){
	return "A B C D\nE     F\n  G\n    H";
}

?>