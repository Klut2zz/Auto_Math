import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    String url = "jdbc:mysql://rm-wz925oethy46b4p036o.mysql.rds.aliyuncs.com:3306/mydatebase";
    String user = "test01";
    String psw = "Tmp123456";

    Connection con = DriverManager.getConnection(url,user,psw);

    public ConnectDB() throws SQLException {
    }

    public void register(String id, String psw, String type) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Statement statement = con.createStatement();
        String sql1 = "select id from userteacher where id=?";
        //select 列名 from 表名 where 列名=?
        PreparedStatement PS = con.prepareStatement(sql1);
        PS.setString(1,id);//给sql语句的第1个问号赋值
        ResultSet res = PS.executeQuery();
        if (res.next()){
            System.out.println("该账号已存在，请重新输入！");
        }else{
            String sql2="insert into userteacher (id,psw,type) values(?,?,?);";
            //insert into 表名 (列名1,列名2) values(?,?);
            PS=con.prepareStatement(sql2);
            PS.setString(1,id);//给第1个问号赋值
            PS.setString(2,psw);//给第2个问号赋值
            PS.setString(3,type);//给第3个问号赋值
            PS.executeUpdate();
            System.out.println("注册成功");
        }
        //关闭所有接口
        res.close();
        PS.close();
        statement.close();
        con.close();
    }

    public boolean login(String id,String password) throws Exception{
        String sql1 = " select id from userteacher where id=? ";
        PreparedStatement PS = con.prepareStatement(sql1);
        PS.setString(1,id);
        ResultSet res = PS.executeQuery();

        boolean loginIn = false;
        if(res.next()){
            String sql2 = "select * from userteacher where id= ? and psw= ?";
            PreparedStatement PS1 = con.prepareStatement(sql2);
            PS1.setString(1,id);
            PS1.setString(2,password);
            ResultSet res1 = PS1.executeQuery();
            if (res1.next()){
                //System.out.println("登录成功！");
                loginIn = true;
            } else {
                System.out.println();
                System.out.println(res1.next());
                System.out.println("密码错误");
            }
        }else {
            System.out.println("此账号还未注册，请先注册再登录！");
        }
        //con.close();
        PS.close();
        res.close();
        return loginIn;
    }

    public boolean checkaccount(String id) throws Exception{
        String sql1 = " select id from userteacher where id=? ";
        PreparedStatement PS = con.prepareStatement(sql1);
        PS.setString(1,id);
        ResultSet res = PS.executeQuery();
        if(res.next()){
            PS.close();
            res.close();
            //con.close();
            return false;
        } else {
            PS.close();
            res.close();
            //con.close();
            return true;
        }
    }

    public static void changePassword(Connection con,String id,String oldPassword,String newPassword) throws Exception{
        String sql1 = " select id from userteacher where id=? ";
        System.out.println("当前用户："+id);
        PreparedStatement ps = con.prepareStatement(sql1);
        ps.setString(1,id);
        ResultSet res = ps.executeQuery();
        if(res.next()){
            String sql2 = " select * from userteacher where id= ? and psw= ?";
            PreparedStatement PS1 = con.prepareStatement(sql2);
            PS1.setString(1,id);
            PS1.setString(2,oldPassword);
            ResultSet res1 = PS1.executeQuery();
            if (res1.next()){
                String updateSql = "update userteacher set psw = ? where id = ?";
                PreparedStatement PS2=con.prepareStatement(updateSql);
                PS2.setString(1,newPassword);
                PS2.setString(2,id);
                PS2.executeUpdate();
                System.out.println("修改成功！");
            } else {
                System.out.println();
                System.out.println("密码错误");
            }
        }else {
            System.out.println("此用户名还未注册，请先注册再登录！");
        }
        //con.close();
        ps.close();
        res.close();
    }

}
