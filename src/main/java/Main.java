import java.sql.*;
import java.util.Scanner;

public class Main {

    static Connection connection;
    private static final String HOST = "localhost";
    private static final String DATABASE = "TestDB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "KmtKIitARKw4j3Db7tL5";

    public static void main(String[] args) throws SQLException {
        String url = String.format("jdbc:postgresql://%s/%s?user=%s&password=%s", HOST, DATABASE, USER, PASSWORD);
        try{
            connection = DriverManager.getConnection(url, USER, PASSWORD);
            connection.setAutoCommit(false);
            if(connection == null){
                System.out.println("Подключение не удалось");
            } else {
                System.out.println("Соединение установлено");
                System.out.print("Введите id: ");
                int id = new Scanner(System.in).nextInt();
                if(checkValue(id)){
                    System.out.println("Этот id же занят");
                }
                else {
                    System.out.print("Введите имя: ");
                    String name = new Scanner(System.in).nextLine();
                    if(insertValue(id, name)){
                        System.out.println("Все записи успешно добавлены");
                        connection.commit();
                    }
                }
            }
        } catch (SQLException e){
            connection.rollback();
            System.out.println("Данные не были добавлены");
            e.printStackTrace();
        }
    }

    public static boolean checkValue(int checkedValue){
        String SQL = "SELECT * FROM test WHERE id=?;";
        try(PreparedStatement ps = connection.prepareStatement(SQL)){
            ps.setInt(1, checkedValue);
            ResultSet result = ps.executeQuery();
            while(result.next()){
                return true;
            }
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean insertValue(int value, String newName){
        String query = "INSERT INTO test(id, name) VALUES(?, ?)";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, value);
            ps.setString(2, newName);
            int update = ps.executeUpdate();
            if(update > 0){
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}