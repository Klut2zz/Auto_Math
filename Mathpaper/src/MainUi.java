import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class MainUi extends Application{
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("loginUi.fxml"));
            //BorderPane root = new BorderPane();

            Button loginButton = (Button)root.lookup("#loginButton");
            Button registerButton = (Button)root.lookup("#registerButton");
            TextField textId = (TextField)root.lookup("#textId");
            PasswordField textPsw = (PasswordField)root.lookup("#textPsw");

            //login
            Login l = new Login();
            l.userInit();

            //登陆按键事件
            loginButton.setOnAction(e ->{
                String name = textId.getText();

                String psd = textPsw.getText();
                User u = new User(name,psd,"");
                if (!"None".equals(l.userCheck(u).getType())) {
                    System.out.println("登陆成功！");
                    UserStage userStage = new UserStage();
                    primaryStage.close();
                } else {
                    System.out.println("登录失败！");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "账号或密码错误！");
                    alert.showAndWait();
                }
            });

            registerButton.setOnAction(e -> {
                try {
                    RegStage regStage = new RegStage();
                    System.out.println("1");
                    l.userInit();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);//设置不能窗口改变大小
            primaryStage.setTitle("HNU-试卷生成软件");//设置标题
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

class RegStage {
    private final Stage stage = new Stage();
    private final Parent pr = FXMLLoader.load(getClass().getResource("registerUi.fxml"));

    private final TextField textId = (TextField) pr.lookup("#textId");
    private final PasswordField textPsw1 = (PasswordField) pr.lookup("#textPsw1");
    private final PasswordField textPsw2 = (PasswordField) pr.lookup("#textPsw2");
    private final TextField textEmail = (TextField) pr.lookup("#textEmail");
    private final TextField textCap = (TextField) pr.lookup("#textCap");
    private final Button capButton = (Button) pr.lookup("#capButton");
    private final Button regButton = (Button) pr.lookup("#regButton");

    private String cap;
    public RegStage() throws IOException {
        stage.setTitle("注册界面");
        stage.setScene(new Scene(pr));
        String id = textId.getText();
        String emailAddr = textEmail.getText();
        capButton.setOnAction(e -> {
            //邮箱地址非空
            if (!textEmail.getText().trim().equals("")) {
                try {
                    cap = sendCap(textEmail.getText(), textId.getText());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        regButton.setOnAction(e -> {
            if(check()){
                if(textCap.getText().equals(cap)){
                    String path = Objects.requireNonNull(this.getClass().getResource("")).getPath() + "\\user.txt";
                    try {
                        FileWriter fw = new FileWriter(path,true);
                        fw.write("\n"+textId.getText()+" "+textPsw1.getText()+" 小学 "+textEmail.getText());
                        fw.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "注册成功！");
                    alert.showAndWait();
                    stage.close();
                }
            }
        });

        stage.show();
    }

    public boolean check(){
        boolean empty = true;
        boolean pswConfirmed = false;
        boolean idEmp = textId.getText().trim().equals("");
        boolean pswEmp = textPsw1.getText().trim().equals("") || textPsw2.getText().trim().equals("");
        boolean emailEmp = textEmail.getText().trim().equals("");
        boolean capEmp = textCap.getText().trim().equals("");
        empty = idEmp && pswEmp && emailEmp && capEmp;
        pswConfirmed = textPsw1.getText().trim().equals(textPsw2.getText());
        return !empty && pswConfirmed;
    }

    public String sendCap(String recAddress,String id) throws Exception {
        Email mail = new Email();
        String cap = mail.getRandom();
        mail.sendQQEmail(mail.emailAccount,mail.authCode,recAddress,cap,id);
        return cap;
    }
}

class UserStage {
    private final Stage stage = new Stage();

    public Stage getStage() {
        return stage;
    }

    public UserStage(){

    }
}
