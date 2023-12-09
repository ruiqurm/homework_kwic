package cn.edu.bupt.sa.kwic.source;

import cn.edu.bupt.sa.kwic.Pipe;

import java.sql.*;


public class DataBaseSource extends Source<String>{
    private String jdbcUrl;
    private String username;
    private String password;
    public DataBaseSource(Pipe<String> input, String jdbcUrl, String username, String password) {
        super(input);
        if(jdbcUrl == null || jdbcUrl.isEmpty()) {
            throw new IllegalArgumentException("jdbcUrl is null or empty.");
        }
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("username is null or empty.");
        }
        if(password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password is null or empty.");
        }

        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    protected void handleInput() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 注册JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 创建连接
            connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.password);

            // 编写SQL查询语句
            String sqlQuery = "SELECT * FROM line_talbe";

            // 创建PreparedStatement对象
            preparedStatement = connection.prepareStatement(sqlQuery);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            // 处理查询结果
            String line;
            while (resultSet.next()) {
                // 从结果集中获取数据
                line = resultSet.getString("line");
                // 处理获取的数据，例如打印到控制台
                System.out.println(line);
                this.outPipe.put(line);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
