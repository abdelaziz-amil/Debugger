Time-Travel Debugger (CLI) - Documentation

Utilisation des commandes

Commandes d'exécution

step : Exécute l'instruction suivante (entre dans les méthodes appelées).

so (step-over) : Exécute la ligne courante sans entrer dans les appels de méthodes.

continue : Reprend l'exécution jusqu'au prochain point d'arrêt.

step-back : Revient à la ligne précédente de l'exécution.

sbn <n> : Revient n étapes en arrière.

Commandes d'inspection

frame : Affiche la frame d'exécution actuelle.

stack : Affiche la pile d'appel des méthodes.

receiver : Renvoie l'objet this de la méthode en cours.

sender : Renvoie l’objet qui a appelé la méthode courante.

rv (receiver-variables) : Liste les variables d'instance du receveur courant.

method : Affiche la méthode en cours d'exécution.

argument : Affiche les arguments de la méthode en cours.

tmp (temporaries) : Affiche les variables temporaires de la frame courante.

Gestion des Breakpoints

break <fichier> <ligne> : Ajoute un point d'arrêt à la ligne spécifiée.

breakpoint : Affiche la liste des points d'arrêt actifs.

Inspection des Variables

pv <nom_variable> : Affiche la valeur de la variable spécifiée.