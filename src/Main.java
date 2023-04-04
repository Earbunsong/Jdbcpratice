import model.Jdbclmpl;
import model.Topic;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Jdbclmpl jdbc;
    private static Scanner scanner;

    public static void main(String[] args) {
        jdbc = new Jdbclmpl();
        scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("1.Select Operator ");
            System.out.println("2.Insert Operator");
            System.out.println("3.Update Operator");
            System.out.println("4.Delete Operator");
            System.out.println("5.Select by ID operator");
            System.out.println("6.Select by Name operator");
            System.out.println("7.Exit program ");
            System.out.println("Choose option 1-7 : ");
            Integer opt = Integer.parseInt(scanner.nextLine());
            switch (opt) {
                case 1:
                    selectTopics();
                    break;
                case 2:
                    System.out.print("Enter Insert name ");
                    String name = scanner.nextLine();
                    System.out.println("Enter Description ");
                    String description = scanner.nextLine();
                    System.out.println("Enter status ");
                    boolean status = Boolean.parseBoolean(scanner.nextLine());
                    Topic topic=new Topic(0,name,description,status);
                    insertTopic(topic);
                    break;
                case 3:
                    updateById();
                    break;
                case 4:

                    deletedById();
                    break;
                case 5:
                    System.out.println("---------------------Select by ID--------------------");
                    System.out.println("Input ID to select ");
                    Integer id= Integer.parseInt(scanner.nextLine());
                    selectByID(id);
                    break;
                case 6:
                    selectByName();
                    break;
            }
        } while (true);
    }

    private static void deletedById(){
        try(Connection conn= jdbc.dataSource().getConnection()) {
            Integer resultId = null;
            System.out.println("Enter  id to Search ");
            Integer id = Integer.parseInt(scanner.nextLine());
            String sidSql ="DELETE FROM topic WHERE id =?";
            PreparedStatement statement = jdbc.dataSource().getConnection().prepareStatement(sidSql);
            statement.setInt(1,id);
            int count = statement.executeUpdate();

            if (count>0){
                System.out.println("Deleted success ");
            }else {
                System.out.println("Deleted not success ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void selectByName(){
        try(Connection conn=jdbc.dataSource().getConnection()) {
            String sidSql ="SELECT * FROM topic WHERE name=?";
            PreparedStatement statement = jdbc.dataSource().getConnection().prepareStatement(sidSql);
            System.out.println("Enter Name to select : ");
            String name = scanner.nextLine();
            statement.setString(1,name);
            Topic topic = new Topic();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                topic.setId(resultSet.getInt("id"));
                topic.setName(resultSet.getString("name"));
                topic.setDescription(resultSet.getString("description"));
                topic.setStatus(resultSet.getBoolean("status"));
            }
            System.out.println(topic);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void selectByID(Integer id){
        try(Connection conn=jdbc.dataSource().getConnection()) {
            String sidSql ="SELECT * FROM topic WHERE id=?";
            PreparedStatement statement = jdbc.dataSource().getConnection().prepareStatement(sidSql);
            statement.setInt(1,id);
            Topic topic = new Topic();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                topic.setId(resultSet.getInt("id"));
                topic.setName(resultSet.getString("name"));
                topic.setDescription(resultSet.getString("description"));
                topic.setStatus(resultSet.getBoolean("status"));
            }
            System.out.println(topic);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    private static void updateById() {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            Integer resultId = null;
            System.out.println("Enter  id to Update ");
            Integer id = Integer.parseInt(scanner.nextLine());
            String sidSql = "SELECT *FROM topic WHERE id=?";
            PreparedStatement statement = jdbc.dataSource().getConnection().prepareStatement(sidSql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultId = resultSet.getInt("id");
            }
            if (resultId != null) {
                System.out.println("Enter name to Update ");
                String name = String.format(scanner.nextLine());
                System.out.println("Enter Description to  ");
                String description = String.format(scanner.nextLine());
                System.out.println("Enter Status to Update ");
                Boolean status = Boolean.getBoolean(scanner.nextLine());
                String sql = "UPDATE topic SET name =?, description =?,status=? WHERE id=?";
                PreparedStatement statementUpdate = jdbc.dataSource().getConnection().prepareStatement(sql);
                statementUpdate.setString(1, name);
                statementUpdate.setString(2, description);
                statementUpdate.setBoolean(3, status);
                statementUpdate.setInt(4, resultId);
                int count = statementUpdate.executeUpdate();
                if (count > 0) {
                    System.out.println("Update Success");
                } else {
                    System.out.println("update not Success ");
                }
            } else {
                System.out.println("search not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Insert record
    private static void insertTopic(Topic topic) {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            String insertSql = "INSERT INTO topic (name , description,status) " +
                    "VALUES(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertSql);
            statement.setString(1, topic.getName());
            statement.setString(2, topic.getDescription());
            statement.setBoolean(3,
                    topic.isStatus());

            int count = statement.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectTopics() {
        try (Connection conn = jdbc.dataSource().getConnection()) {
            {
                String selectSql = "SELECT * FROM topic";
                PreparedStatement statement = conn.prepareStatement(selectSql);
                ResultSet resultSet = statement.executeQuery();
                List<Topic> topics = new ArrayList<>();
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    boolean status = resultSet.getBoolean("status");
                    topics.add(new Topic(id, name, description, status));
                }
                topics.stream().forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}