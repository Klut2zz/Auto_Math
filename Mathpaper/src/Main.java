import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Button b3 = new Button("b3");
        Button b4 = new Button("b4");

        Label l_name = new Label("名字：");
        l_name.setFont(new Font(20));//字体大小
        l_name.setTooltip(new Tooltip("请输入名字"));
        Label l_psd = new Label("密码：");
        l_psd.setTooltip(new Tooltip("请输入密码"));
        l_psd.setFont(new Font(20));

        TextField t_name = new TextField();
        t_name.setUserData("a");//给一个测试数据
        PasswordField p_psd = new PasswordField();
        p_psd.setUserData("aa");//给一个测试数据
        Button login = new Button("登录");
        Button clear = new Button("清除");

        GridPane gr = new GridPane();

        gr.setStyle("-fx-background-color: #efead0");
        gr.add(l_name, 0, 0);
        gr.add(t_name, 1, 0);
        gr.add(l_psd, 0, 1);
        gr.add(p_psd, 1, 1);
        gr.add(clear, 0, 2);
        gr.add(login, 1, 2);

        gr.setAlignment(Pos.CENTER);
        gr.setHgap(10);//设置水平间距
        gr.setVgap(17);//设置垂直间距
        GridPane.setMargin(login, new Insets(0, 0, 0, 120));

        //login
        Login l = new Login();
        l.userInit();

        //清除事件
        clear.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                t_name.setText("");
                p_psd.setText("");//变空或者p.clear
            }
        });

        //登录事件
        login.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {

                String name = t_name.getText();

                String psd = p_psd.getText();
                User u = new User(name,psd,"");
                if (!"None".equals(l.userCheck(u).getType())) {
                    System.out.println("登陆成功！");
                    loginStage MWD = new loginStage(u);
                    primaryStage.close();
                } else {
                    System.out.println("登录失败！");
                    l_name.setTextFill(Color.CORAL);
                    FadeTransition tst = new FadeTransition();
                    tst.setDuration(Duration.seconds(0.2));
                    tst.setNode(gr);
                    tst.setFromValue(0);
                    tst.setToValue(1);
                    tst.play();

                }

            }

        });


        Scene scene = new Scene(gr);


        primaryStage.setScene(scene);
        primaryStage.setTitle("Java FX - 登录页面 ");
        primaryStage.setWidth(500);
        primaryStage.setHeight(300);
        primaryStage.setResizable(false); //登录窗口的大小不允许改变
        primaryStage.show();


    }

}



class loginStage {

    private final Stage stage = new Stage();

    public loginStage(User u) {
        Text text = new Text("账号：" + u.getId() + "     密码" + u.getType());
        BorderPane bor = new BorderPane();
        bor.setStyle("-fx-background-color: cadetblue");
        bor.setCenter(text);
        Scene scene = new Scene(bor);


        stage.setScene(scene);
        stage.setTitle("登陆成功 ");
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setResizable(false); //登录窗口的大小不允许改变
        stage.show();
    }
}

