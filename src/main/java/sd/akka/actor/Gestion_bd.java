//gestion_bd.java
package sd.akka.actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * La classe Gestion_bd gère les opérations liées à la base de données,
 * telles que la connexion, la mise à jour du solde client, les retraits,
 * et la récupération du solde client.
 */
public class Gestion_bd {

    List<String> listOperations = new ArrayList<String>();
    LocalDateTime myDateobj = LocalDateTime.now();
    DateTimeFormatter myformaobject = DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss");
    static Connection con;

    /**
     * Établit une connexion à la base de données MySQL.
     */
    public void Connexion() {
        try {// Charger le pilote JDBC MySQL.

            // Établir la connexion à la base de données MySQL.
            // "jdbc:mysql://localhost:3306/connexion" spécifie l'URL de connexion à la base de données.
            // "root" est le nom d'utilisateur MySQL, et "root" est le mot de passe.
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/connexion?characterEncoding=utf-8",
                    "root", "root");
            System.out.println("⚡ Connexion à MySQL réussie ⚡");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour le solde d'un client dans la base de données.
     *
     * @param montant   Le montant à ajouter au solde actuel.
     * @param idClient  L'identifiant du client.
     */
    public void updateSolde(int montant, int idClient) {
        try {
            // Obtenez le solde actuel du client
            int soldeActuel = getSoldeClient(idClient);

            // Ajoutez le montant au solde actuel
            int nouveauSolde = soldeActuel + montant;

            // Mettez à jour le solde dans la base de données
            String updateQuery = "UPDATE Client SET solde = ? WHERE idClient = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, nouveauSolde);
                preparedStatement.setInt(2, idClient);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Effectue un retrait sur le solde d'un client dans la base de données.
     *
     * @param montant   Le montant à retirer du solde client.
     * @param idClient  L'identifiant du client.
     * @return          True si le retrait est réussi, False sinon.
     */    public boolean RetraitSolde(int montant, int idClient) {
        try {
            int id = idClient;
            int solde = montant;

            int s = getSoldeClient(idClient);
            if (s - montant >= 10) {
                // Requête préparée
                PreparedStatement preparedStatement = con.prepareStatement("UPDATE Client"
                        + " SET solde = (solde-?) "
                        + "WHERE idClient = ?");

                // Donne les attributs à la requête préparée
                preparedStatement.setInt(1, montant);
                preparedStatement.setInt(2, idClient);

                // Exécution de la requête
                preparedStatement.executeUpdate();
                System.out.println("Le client " + id + " a fait un retrait de " + solde + " à " + myDateobj.format(myformaobject));
                System.out.println();

                // Fermeture du preparedStatement
                preparedStatement.close();

                return true;
            } else {
                System.out.println("❌ Retrait impossible : Solde insuffisant pour le client " + idClient);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Obtient le solde d'un client depuis la base de données.
     *
     * @param idClient  L'identifiant du client.
     * @return          Le solde du client.
     */    public int getSoldeClient(int idClient) {
        int solde = 0;
        int id = idClient;
        try {
            // Création de la requête préparée
            PreparedStatement preparedStatement = con.prepareStatement("SELECT solde FROM Client WHERE IDCLIENT=?");

            // Donne les attributs à la requête préparée
            preparedStatement.setInt(1, idClient);

            // Exécution de la requête
            ResultSet resultat = preparedStatement.executeQuery();

            while (resultat.next()) {
                solde = resultat.getInt("solde");
                System.out.println("Le client " + id + " a consulté son solde  qui est de " + solde + " à " + myDateobj.format(myformaobject));
            }

            // Fermeture du ResultSet
            resultat.close();
            // Fermeture du PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solde;
    }





    /*                          FONCTIONELLE
    // Méthode pour récupérer tous les clients de la base de données
    public static Hashtable<Integer, Integer> getAllClient() {
        // Hashtable que l'on va renvoyer
        Hashtable<Integer, Integer> lesClients = new Hashtable<>();
        try {
            Statement statement = con.createStatement();

            // Pour chaque ligne de la table Client
            ResultSet resultat = statement.executeQuery("SELECT idClient, idBanquier FROM Client");
            int idClient;
            int idBanquier;
            while (resultat.next()) {
                idClient = resultat.getInt("idClient");
                idBanquier = resultat.getInt("idBanquier");
                lesClients.put(idClient, idBanquier);
            }
            resultat.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesClients;
    }

    // Méthode pour récupérer tous les banquiers de la base de données
    public ArrayList<Integer> getAllBanquier() {
        ArrayList<Integer> lesBanquiers = new ArrayList<>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultat = statement.executeQuery("SELECT * FROM Banquier");
            int idBanquier;

            // Pour chaque ligne de la table Banker
            while (resultat.next()) {
                idBanquier = resultat.getInt("idBanquier");
                lesBanquiers.add(idBanquier);
            }
            resultat.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesBanquiers;
    }                                                                                    */

}
