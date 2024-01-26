// Banker.java
package sd.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
/**
 * Classe représentant l'acteur Banker dans le système Akka.
 * Cet acteur gère les opérations bancaires telles que les dépôts, les retraits
 * et la récupération du solde pour chaque client.
 */
public class Banker extends AbstractActor {
    private final Gestion_bd gestionbd;
    private HashMap<Integer, Integer> clients; // Utilisation de HashMap
    private HashMap<Integer, ActorRef> banker;

    /**
     * Constructeur privé prenant une instance de Gestion_bd comme paramètre.
     * Initialise les attributs de l'acteur Banker.
     *
     * @param gestionbd Instance de Gestion_bd pour les opérations sur la base de données.
     */    private Banker(Gestion_bd gestionbd) {
        this.gestionbd = gestionbd;
        this.clients = new HashMap<>(); // Initialisation de la HashMap
    }

    /**
     * Méthode statique pour créer et retourner les Props nécessaires
     * pour l'instanciation de l'acteur Banker avec une instance de Gestion_bd.
     *
     * @param gestionbd Instance de Gestion_bd pour les opérations sur la base de données.
     * @return Props nécessaires pour créer l'acteur Banker.
     */    public static Props props(Gestion_bd gestionbd) {
        return Props.create(Banker.class, gestionbd);
    }

    /**
     * Méthode pour créer la logique de réception des messages.
     * Cette méthode utilise le modèle de réception d'Akka.
     *
     * @return Logique de réception des messages.
     */    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GetDemandeDepot.class, this::handleDemandeDepot)
                .match(GetDemandeRetrait.class, this::handleDemandeRetrait)
                .match(GetSolde.class, this::handleGetSolde)
                .build();
    }

    /**
     * Méthode pour traiter la demande de dépôt.
     * Cette méthode met à jour le solde du client après un dépôt.
     *
     * @param message Objet GetDemandeDepot encapsulant les données de la demande de dépôt.
     */
    private void handleDemandeDepot(GetDemandeDepot message) {     // Méthode pour traiter la demande de dépôt
        int idClient = message.idClient;
        int montant = message.montant;
        System.out.println(" 💰 Solde avant dépôt  : " + this.gestionbd.getSoldeClient(idClient));
        this.gestionbd.updateSolde(montant, idClient);

        // Mettez à jour le solde après le dépôt en utilisant la nouvelle valeur retournée
        int nouveauSolde = this.gestionbd.getSoldeClient(idClient);
        System.out.println("💰 Depot de " + montant + " pour le client " + idClient + ".  💰💰 Nouveau solde : " + nouveauSolde);
        System.out.println("   ---Voulez-vous effectuer une autre operation ? (1 pour oui, 2 pour quitter)");

    }
    /**
     * Classe interne représentant la demande de dépôt.
     * Cette classe encapsule les données nécessaires pour une demande de dépôt.
     */
     public static class GetDemandeDepot {     // Classe interne représentant la demande de dépôt
        public int idClient;
        public int montant;

        public GetDemandeDepot(int idClient, int montant) {
            this.idClient = idClient;
            this.montant = montant;
        }
    }
    /**
     * Classe interne représentant la demande de retrait.
     * Cette classe encapsule les données nécessaires pour une demande de retrait.
     */

    /**
     * Méthode pour traiter la demande de retrait.
     * Cette méthode vérifie si le retrait est possible et effectue le retrait le cas échéant.
     *
     * @param message Objet GetDemandeRetrait encapsulant les données de la demande de retrait.
     */
    private void handleDemandeRetrait(GetDemandeRetrait message) {
        int idClient = message.idClient;
        int montant = message.montant;

        // Vérifiez si le retrait est possible
        int soldeActuel = this.gestionbd.getSoldeClient(idClient);
        if (soldeActuel >= montant) {
            // Effectuez le retrait
            this.gestionbd.RetraitSolde(montant, idClient);
            int nouveauSolde = this.gestionbd.getSoldeClient(idClient);
            System.out.println("💸 Retrait de " + montant + " pour le client " + idClient + ". 💸💸 Nouveau solde : " + nouveauSolde);
            System.out.println("   ---Voulez-vous effectuer une autre operation ? (1 pour oui, 2 pour quitter)");

        } else {
            System.out.println("❌ Solde insuffisant pour le client " + idClient + ". Retrait de " + montant + " impossible.");
        }
    }/**
     * Classe interne représentant la demande de retrait.
     * Cette classe encapsule les données nécessaires pour une demande de retrait.
     */
    public static class GetDemandeRetrait {
        public int idClient;
        public int montant;

        public GetDemandeRetrait(int idClient, int montant) {   // Classe interne représentant la demande de retrait
            this.idClient = idClient;
            this.montant = montant;
        }
    }

    /**
     * Méthode pour traiter la demande du solde.
     * Cette méthode récupère le solde du client et l'affiche.
     *
     * @param message Objet GetSolde encapsulant les données de la demande de solde.
     */
    private void handleGetSolde(GetSolde message) {
        int idClient = message.idClient;
        int solde = this.gestionbd.getSoldeClient(idClient);
        System.out.println("\uD83D\uDCB3 Solde du client " + idClient + " : " + solde);
        System.out.println("   ---Voulez-vous effectuer une autre operation ? (1 pour oui, 2 pour quitter)");

    }
    /**
     * Classe interne représentant la demande du solde.
     * Cette classe encapsule les données nécessaires pour une demande de solde.
     */
    public static class GetSolde {
        public int idClient;

        public GetSolde(int idClient) {
            this.idClient = idClient;
        }
    }
}
