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

        Topic topic = new Topic();
        System.out.println("Enter name : ");
        topic.setName(scanner.nextLine());
        System.out.println("Enter description : ");
        topic.setDescription(scanner.nextLine());
        topic.setStatus(true);
        insertTopic(topic);
        selectTopics();
    }


    //Insert record
    private static void insertTopic(Topic topic){
        try (Connection conn = jdbc.dataSource().getConnection()){
            String insertSql = "INSERT INTO topic (name , description,status) "+
                    "VALUES(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(insertSql);
            statement.setString(1 ,topic.getName());
            statement.setString(2,topic.getDescription());
            statement.setBoolean(3,
                    topic.isStatus());

            int count = statement.executeUpdate();
            System.out.println(count);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void selectTopics(){
        try(Connection conn =jdbc.dataSource().getConnection()) {
            {
                String selectSql  = "SELECT * FROM topic";
                PreparedStatement statement = conn.prepareStatement(selectSql);
                ResultSet resultSet = statement.executeQuery();
                List<Topic> topics = new ArrayList<>();
                while (resultSet.next()){
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    boolean status = resultSet.getBoolean("status");
                    topics.add(new Topic(id,name,description,status));
                }
                topics.stream().forEach(System.out::println);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}