package se.iths.databaser.Labb3;

import java.util.Scanner;
import java.sql.*;

public class Labb3BobHogvall {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        boolean quit = false;
        while(!quit) {
            printActions();
 //           System.out.println("\nVälj (6 för att visa val):");
            int action = scan.nextInt();;
            scan.nextLine();

            switch(action){
                case 0 -> {System.out.println("\nAvslutar.."); quit = true;}
                case 1 -> read();
                case 2 -> userInputForCreate();
                case 3 -> userInputForUpdate();
                case 4 -> userInputForDelete();
                case 5 -> countRecords();
 //               case 6 -> printActions();
            }
        }
    }

    private static Connection sqlConnection() {
        String url = "jdbc:sqlite:/Users/bobster/Desktop/databaser/Labb3/Labb3BobHogvall.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    private static void read() {
        String sql = "SELECT * FROM records";

        try (Connection conn = sqlConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                System.out.println(rs.getInt("recordId") + "\t" +
                        rs.getString("recordArtist") + "\t" +
                        rs.getString("recordTitle") + "\t" +
                        rs.getString("publicationDate") + "\t");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void userInputForCreate(){
        System.out.println("Artist: ");
        String artist = scan.nextLine();
        System.out.println("Titel: ");
        String title = scan.nextLine();
        System.out.println("Publikationsdatum: ");
        String pDate = scan.nextLine();
        create(artist, title, pDate);
    }
    private static void create(String artist, String title, String pDate) {
        String sql = "INSERT INTO records(recordArtist, recordTitle, publicationDate) " +
                "VALUES(?,?,?)";


        try (Connection conn = sqlConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, artist);
            pstmt.setString(2, title);
            pstmt.setString(3, pDate);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny skiva");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void userInputForUpdate(){
        read();
        System.out.println("Vilken skiva vill du uppdatera? Skriv in id: ");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Artist: ");
        String artist = scan.nextLine();
        System.out.println("Title: ");
        String title = scan.nextLine();
        System.out.println("Publikationsdatum: ");
        String pDate = scan.nextLine();
        update(artist, title, pDate, id);
    }
    private static void update(String artist, String title, String pDate, int id) {
        String sql = "UPDATE records SET recordArtist = ? , "
                + "recordTitle = ? , "
                + "publicationDate = ? "
                + "WHERE recordId = ?";

        try (Connection conn = sqlConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artist);
            pstmt.setString(2, title);
            pstmt.setString(3, pDate);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Skivan är uppdaterad.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void userInputForDelete(){
        read();
        System.out.println("Vilken skiva vill du radera? Skriv in id: ");
        int id = scan.nextInt();
        scan.nextLine();
        delete(id);
    }
    private static void delete(int id) {
        String sql = "DELETE FROM records WHERE recordId = ?";

        try (Connection conn = sqlConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Skivan har tagits bort");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void countRecords() {
        String sql = "SELECT COUNT(ALL) FROM RECORDS";

        try (Connection conn = sqlConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){
            rs.next();
            int count = rs.getInt(1);
            System.out.println("Antal sparade skivor är: " + count +"\n");

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

    }




    
    private static void printActions() {
        System.out.println("""
                Välj:
                0: Stäng av.
                1: Visa alla skivor.
                2: Lägg till en ny skiva.
                3: Uppdatera en skiva.
                4: Radera en skiva.
                5: Visa antal skivor.
                """);
    }
}
