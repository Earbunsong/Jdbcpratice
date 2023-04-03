package model;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class Jdbclmpl {
    public DataSource dataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("391738");
        dataSource.setDatabaseName("postgres");
//        dataSource.setServerNames(new String[]{"127.0.01"});
//        dataSource.setPortNumbers(new int[]{6666});
        return dataSource;
    }
}
