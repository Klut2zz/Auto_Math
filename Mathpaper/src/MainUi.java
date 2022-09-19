import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainUi extends Application{
    private Login l = new Login();
    private ConnectDB con = new ConnectDB();
    private User currentUser = new User();

    public MainUi() throws SQLException {
    }

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
            l.userInit();

            //登陆按键事件
            loginButton.setOnAction(e ->{
                try {
                    con = new ConnectDB();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String name = textId.getText();

                String psd = textPsw.getText();
                User u = new User(name,psd,"");
                //if (!"None".equals(l.userCheck(u).getType())) {
                try {
                    if (con.login(name,psd)) {
                        System.out.println("登陆成功！");
                        currentUser.setType("小学");
                        ChooseStage chooseStage = new ChooseStage();
                        primaryStage.close();
                    } else {
                        System.out.println("登录失败！");
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "账号或密码错误！");
                        alert.showAndWait();
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

            registerButton.setOnAction(e -> {
                try {
                    RegStage regStage = new RegStage();
                    System.out.println("注册完成");
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
                            con.register(textId.getText(),textPsw1.getText(),"小学");
                            /*FileWriter fw = new FileWriter(path,true);
                            fw.write(textId.getText()+" "+textPsw1.getText()+" 小学 "+textEmail.getText()+"\n");
                            fw.close();
                            l.userInit();*/
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (ClassNotFoundException ex) {
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
    class ChooseStage {
        private final Stage stage = new Stage();
        private final Parent pr = FXMLLoader.load(getClass().getResource("chooseUi.fxml"));
        private final Label showStatus = (Label) pr.lookup("#showstatus");
        private final Button priButton = (Button) pr.lookup("#priButton");
        private final Button midButton = (Button) pr.lookup("#midButton");
        private final Button highButton = (Button) pr.lookup("#highButton");
        private final Button conButton = (Button) pr.lookup("#conButton");
        private final Button backButton = (Button) pr.lookup("#backButton");
        private final TextField textNumber = (TextField) pr.lookup("#textNumber");

        private String type = currentUser.getType();
        private ArrayList<String> question = new ArrayList<>();
        public Stage getStage() {
            return stage;
        }

        public ChooseStage() throws IOException {
            stage.setTitle("选择界面");
            stage.setScene(new Scene(pr));


            showStatus.setText(type);

            priButton.setOnAction(e -> {
                type = "小学";
                currentUser.setType("小学");
                showStatus.setText(type);
            });

            midButton.setOnAction(e -> {
                type = "初中";
                currentUser.setType("初中");
                showStatus.setText(type);
            });

            highButton.setOnAction(e -> {
                type = "高中";
                currentUser.setType("高中");
                showStatus.setText(type);
            });

            conButton.setOnAction(e -> {
                int quesNum = Integer.parseInt(textNumber.getText());
                try {
                    question = l.paper(quesNum,currentUser);
                    ExamStage eStage = new ExamStage(question);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            stage.show();
        }
    }

    class ExamStage{
        private final Stage stage = new Stage();
        private final Parent pr = FXMLLoader.load(getClass().getResource("paperUi.fxml"));

        private final Label questionLabel = (Label) pr.lookup("#questionLabel");
        private final Button finishButton = (Button) pr.lookup("#finishButton");
        private final Button nextButton = (Button) pr.lookup("#nextButton");
        private final RadioButton toggleA = (RadioButton) pr.lookup("#toggleA");
        private final RadioButton toggleB = (RadioButton) pr.lookup("#toggleB");
        private final RadioButton toggleC = (RadioButton) pr.lookup("#toggleC");
        private final RadioButton toggleD = (RadioButton) pr.lookup("#toggleD");
        //ques为生成的题目(中缀表达式)
        private ArrayList<String> ques;
        //answer 用于记录答案选项
        private ArrayList<Integer> answer = new ArrayList<>();
        //optionsContent 存储所有的选项内容
        private ArrayList<String[]> optionsContent = new ArrayList<>();
        //selection 记录每个题的选择
        private int[] selection;
        private int curQuestion;
        ExamStage(ArrayList<String> question) throws IOException {
            this.ques = question;
            selection = new int[ques.size()];
            stage.setTitle("正在测试");
            stage.setScene(new Scene(pr));
            questionLabel.setText("("+(curQuestion+1)+"): "+ques.get(curQuestion));
            optionContent();
            finishButton.setVisible(false);
            nextButton.setOnAction(e -> {
                int choice = recodeChoice();
                if(curQuestion<ques.size()-1 ){
                    selection[curQuestion] = choice;
                    curQuestion++;
                    questionLabel.setText("("+(curQuestion+1)+"): "+ques.get(curQuestion));
                    optionContent();
                }else {
                    finishButton.setVisible(true);
                    selection[curQuestion] = choice;
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "当前已为最后一题");
                    alert.showAndWait();
                }
            });

            finishButton.setOnAction(e -> {
                if(answer.size() == selection.length){
                    int correct = 0;
                    for(int i = 0;i < selection.length;i ++){
                        if(selection[i] == answer.get(i)){
                            correct++;
                        }
                    }
                    double score = correct/selection.length;
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "本次测试的成绩是："+correct+"/"+selection.length);
                    alert.showAndWait();
                    stage.close();
                }else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "请全部完成后再提交");
                    alert.showAndWait();
                }
            });

            stage.show();
        }
        public int recodeChoice(){
            if(toggleA.isSelected()){
                return 0;
            }else if(toggleB.isSelected()){
                return 1;
            }else if(toggleC.isSelected()){
                return 2;
            }else if(toggleD.isSelected()){
                return 3;
            }else return 4;
        }
        public void optionContent(){
            Question q = new Question();
            String ans = q.calSuffix(q.infixToSuffix(ques.get(curQuestion)));
            String[] randAns = new String[4];
            Random r = new Random();
            int ansIndex = r.nextInt(4);
            randAns[ansIndex] = ans;
            for(int i = 0;i < 4;i++){
                if(randAns[i] == null)
                    randAns[i] = String.valueOf(Math.random()*Double.parseDouble(ans)*(r.nextInt(5)-r.nextInt(10)));
            }
            answer.add(ansIndex);
            toggleA.setText("A."+randAns[0]);
            toggleB.setText("B."+randAns[1]);
            toggleC.setText("C."+randAns[2]);
            toggleD.setText("D."+randAns[3]);
        }
    }
}




