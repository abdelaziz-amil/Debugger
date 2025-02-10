Time-Travel Debugger (CLI) - Documentation

Utilisation des commandes

Commandes d'exécution

- step : Exécute l'instruction suivante (entre dans les méthodes appelées).

- so (step-over) : Exécute la ligne courante sans entrer dans les appels de méthodes.

- continue : Reprend l'exécution jusqu'au prochain point d'arrêt.

- step-back : Revient à la ligne précédente de l'exécution. (pas dans l'ihm)

- sbn <n> : Revient n étapes en arrière. (pas dans l'ihm)

Commandes d'inspection

- frame : Affiche la frame d'exécution actuelle.

- stack : Affiche la pile d'appel des méthodes.

- receiver : Renvoie l'objet this de la méthode en cours.

- sender : Renvoie l’objet qui a appelé la méthode courante.

- rv (receiver-variables) : Liste les variables d'instance du receveur courant.

- method : Affiche la méthode en cours d'exécution.

- argument : Affiche les arguments de la méthode en cours.

- tmp (temporaries) : Affiche les variables temporaires de la frame courante.

Gestion des Breakpoints

- break <fichier> <ligne> : Ajoute un point d'arrêt à la ligne spécifiée. (pas dans l'ihm)

- breakpoint : Affiche la liste des points d'arrêt actifs.

Inspection des Variables

- pv <nom_variable> : Affiche la valeur de la variable spécifiée. (pas dans l'ihm)

Scenario :
(NB: les scenarios sont uniquements possible sur la version en ligne de commande sur la branche main)


Il y aura deux scenarios d'utilisation qui permette de tester les fonctionnalités interessantes du debugger
la premiere qui montre les fonctionnalités classiques
et la deuxième qui montre la fonctionnalité step back et step back n du debugger.

Scénario 1 : Utilisation classique du débugueur
Dans ce premier cas, nous exécutons un programme simple contenant des appels de méthodes et des affichages à l'écran.
Le programme utilisé est le suivant :

package dbg;

public class JDISimpleDebuggee {
public static void main(String[] args) {
String description = "Simple power printer";
System.out.println(description + " -- starting");
TestReceiv testReceiv = new TestReceiv();
testReceiv.test();
int x = 40;
int power = 2;
printPower(x, power);
}

    public static double power(int x, int power) {
        return Math.pow(x, power);
    }

    public static void printPower(int x, int power) {
        double powerX = power(x, power);
        System.out.println(powerX);
    }
}

Et la classe associée :

package dbg;

public class TestReceiv {
public void test() {
int a = 1;
int b = 2;
System.out.println(a + b);
String test = "test";
System.out.println(test);
}
}

Déroulement du scénario :

1. Lancer JDISimpleDebugger il y a un breakpoint à la ligne 6 qui est fait automatiquement à l'initialisation de la vm

2. Placer un point d'arrêt dans la class JDISimpleDebuggee, ligne 9
   break dbg.JDISimpleDebuggee 9

3. Démarrer l'exécution jusqu'au premier point d'arrêt
   - continue

4. Afficher la pile d'exécution actuelle
   - stack

5. Afficher la valeur des variables locales
   - tmp

6. Passer à la ligne suivante en entrant dans la méthode appelée
   - step
7. Continuer l'execution jusqu'a la ligne 10 sans entrer dans les differentes methodes
   - so
   - so
   - so
   - so

7. Afficher la valeur de la variable test
   pv test
8. ici vous pouvez tester toutes les commandes d'inspection, elles fonctionnes toutes en ligne de commande

8. Reprendre l'exécution du programme jusqu'à la fin
   continue

Scénario 2 : Utilisation de la fonctionnalité Step-Back
Ce second scénario illustre l'utilisation des commandes step-back et sbn permettant de revenir en arrière dans l'exécution.

Déroulement du scénario :

1. Démarrer le débogueur et exécuter le programme normalement
- so
- so
- so

2. Revenir en arrière d'une seule étape avec step-back
   step-back

3. Revenir en arrière de deux étapes avec sbn 2
   sbn 2

4. Si ici vous voulez retourner en arrière vous ne pourrez pas car il n'y a pas d'historique avant ceci 


Grâce à ces fonctionnalités, le débogueur permet de naviguer dans l'historique d'exécution, facilitant l'analyse des erreurs et des comportements inattendus.
