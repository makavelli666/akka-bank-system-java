// app.java
package sd.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import sd.akka.actor.Banker;
import sd.akka.actor.Gestion_bd;

import java.io.IOException;
import java.util.Scanner;
/**
 * Classe principale de l'application.
 * Cette classe gère l'initialisation du système Akka, la création des acteurs,
 * et l'interaction utilisateur pour effectuer des opérations bancaires.
 */
public class App {

    /**
     * Méthode principale de l'application.
     * Elle initialise le système Akka, crée l'acteur Banker et interagit avec l'utilisateur.
     *
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        System.out.println("        ╔════════════════════════════════════════════╗");
        System.out.println("        ║                     \uD83C\uDFE6\uD83C\uDFE6                  ║ ");
        System.out.println("        ║   Bienvenue dans le système de gestion de  ║ ");
        System.out.println("        ║           comptes bancaires Akka!          ║");
        System.out.println("        ╚════════════════════════════════════════════╝");
        System.out.println("             - Initialisation en cours . . . ");

        Utils.compteARebours();
        Gestion_bd c = new Gestion_bd();
        c.Connexion();

        ActorSystem actorSystem = ActorSystem.create();
        ActorRef banqueActorRef = actorSystem.actorOf(Banker.props(c), "Gestion_transaction");
        Scanner scanner = new Scanner(System.in);
        boolean exitRequested = false;

        while (!exitRequested) {
            System.out.println("\n");
            System.out.println("═════════════════════════════════════════════════════════");
            System.out.println("                Choisissez une operation :");
            System.out.println("                    1. \uD83D\uDCB8Retrait");
            System.out.println("                    2. \uD83D\uDCB0Depot");
            System.out.println("                    3. \uD83D\uDCA1Solde");
            System.out.println("                    4. \uD83D\uDEAAQuitter");
            System.out.println("═════════════════════════════════════════════════════════");



            int choixOperation = scanner.nextInt();
            switch (choixOperation) {
                case 4:
                    exitRequested = true;
                    break;
                default:
                    System.out.println("\nChoisissez un client (1-4) :");
                    int idClient = scanner.nextInt();

                    switch (choixOperation) {
                        case 1:
                            System.out.println("\n💸💸💸 Operation de Retrait 💸💸💸");
                            System.out.println("Solde avant retrait pour le client " + idClient + " : " + c.getSoldeClient(idClient));
                            System.out.println("Veuillez saisir le montant du retrait : ");
                            banqueActorRef.tell(new Banker.GetDemandeRetrait(idClient, scanner.nextInt()), ActorRef.noSender());
                            System.out.println("Solde après retrait pour le client " + idClient + " : " + c.getSoldeClient(idClient));
                            break;
                        case 2:
                            System.out.println("\n💰💰💰 Operation de Depot 💰💰💰");
                            System.out.println("Solde avant depot pour le client " + idClient + " : " + c.getSoldeClient(idClient));
                            System.out.println("Veuillez saisir le montant du depot : ");
                            banqueActorRef.tell(new Banker.GetDemandeDepot(idClient, scanner.nextInt()), ActorRef.noSender());  // Correction ici
                            System.out.println("Solde après depot pour le client " + idClient + " : " + c.getSoldeClient(idClient));
                            break;

                        case 3:
                            System.out.println("\n\uD83D\uDCA1\uD83D\uDCA1\uD83D\uDCA1 Operation de Consultation de Solde \uD83D\uDCA1\uD83D\uDCA1\uD83D\uDCA1");
                            System.out.println("Demande de solde pour le client " + idClient + " : " + c.getSoldeClient(idClient));
                            banqueActorRef.tell(new Banker.GetSolde(idClient), ActorRef.noSender());
                            break;
                        default:
                            System.out.println("❌ Operation non reconnue. Veuillez reessayer.");
                    }
                    System.out.println("\n");
                    System.out.println("  ╔════════════════════════════════════════════════════════════════════════════");
                    System.out.println("  ║ Voulez-vous effectuer une autre operation ? (1 pour oui, 2 pour quitter)");
                    System.out.println("  ╚════════════════════════════════════════════════════════════════════════════");
                    int choixContinuer = scanner.nextInt();
                    if (choixContinuer == 2) {
                        exitRequested = true;
                    }
            }
        }
        System.out.println("\n🎉🎉🎉 Merci d'avoir utilise le systeme de gestion de comptes bancaires Akka! 🎉🎉🎉");
        Utils.pause(1750); // Pause de 5 secondes
        System.out.println("\n Le programme se termine .   .   . ");
        System.out.println(" Vous allez etre redirige vers un nouveau terminal ...  ");
        actorSystem.terminate();

        try {
            Utils.pause(5000); // Pause de 2 secondes

            // Effacer l'écran du terminal
            Utils.clearScreen();

        } finally {

        }
    }

    /**
     * Classe utilitaire contenant des méthodes pour la gestion du temps et de l'écran.
     */
static class Utils {    // ptit class pour gerer les arrets et le compte , etc ...............
    public static void pause(int milliseconds) { /**
     * Met en pause l'exécution du programme pour la durée spécifiée en millisecondes.
     *
     * @param milliseconds La durée de la pause en millisecondes.
     */
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace(); } }

        /**
         * Effectue un compte à rebours simple avant l'initialisation du système.
         */
    private static void compteARebours() {
        try {
            System.out.print("                             3\n ");
            Thread.sleep(1000); // Pause de 1 seconde
            System.out.print("                            2\n ");
            Thread.sleep(1000); // Pause de 1 seconde
            System.out.print("                            1\n ");
            Thread.sleep(1000); // Pause de 1 seconde
        } catch (InterruptedException e) {
            e.printStackTrace();                     } }


        /**
         * Fonction pour effacer l'écran du terminal (alternative).
         */
    private static void clearScreen() {     // Fonction pour effacer l'écran du terminal (alternative)
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Pour Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Pour Linux et macOS
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}}
