import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * @author 30347
 */
public class Login {
  private final List<User> users = new ArrayList<>();
  /**
   * @Description:主函数
   */
  public static void main(String[] args) throws IOException {
    Login l = new Login();
    l.userInit();
    l.userLogin();
  }
  /**
   * @Description:初始化，加载用户列表
   */
  public void userInit() {
    File userInfo = new File(Objects.requireNonNull(this.getClass().getResource("")).getPath() + "\\user.txt");
    try{
      BufferedReader br = new BufferedReader(new FileReader(userInfo));
      String account;
      while((account = br.readLine()) != null){
        String[] tmp = account.split(" ");
        User tmpUser = new User();
        tmpUser.setId(tmp[0]);
        tmpUser.setPsw(tmp[1]);
        tmpUser.setType(tmp[2]);
        users.add(tmpUser);
      }
      br.close();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * @Description:登陆
   */
  public void userLogin() throws IOException {
    System.out.println("请输入用户名以及密码：");
    Scanner scan = new Scanner(System.in);
    boolean online = false;
    while(!online){
      String[] tmp = scan.nextLine().split(" ");
      User uTmp=new User(tmp[0],tmp.length<2?"":tmp[1],"None");
      uTmp=userCheck(uTmp);
      switch (uTmp.getType()) {
        case "小学", "初中", "高中" -> {
          //生成试卷
          prodExam(uTmp);
          if (uTmp.isConvert()) {
            System.out.println("正重新登录，请输入用户名以及密码：");
            continue;
          }
          online = true;
        }
        case "None" -> System.out.println("请输入正确的用户名、密码：");
        default -> System.out.println("请按要求输入：");
      }
    }
  }


  public User userCheck(User u){
    /*
      验证账户id与密码是否正确
     */
    for(User i : users){
      if(i.equals(u)){
        return i;
      }
    }
    return new User();
  }

  private void prodExam(User u) throws IOException {
    /*
      生成试卷
     */
    Scanner scan = new Scanner(System.in);
    while(true){
      System.out.println("准备生成"+u.getType()+"数学题目，请输入生成题目数量（输入-1将退出当前用户，重新登录）：");
      String tmp = scan.nextLine();
      //判断是否为数字
      if(tmp.matches("-?[0-9]+.?[0-9]*")){
        int cnt=Integer.parseInt(tmp);
        if(cnt<=30 && cnt>=10){
          //生成测试题
          paper(cnt,u);
          continue;
        } else if(cnt == -1){
          u.setConvert(true);
        } else {
          System.out.println("范围不合法，请重新输入!");
          continue;
        }
      }
      //切换类型
      if("切换为小学".equals(tmp)){
        u.setType("小学");
        continue;
      }
      if("切换为初中".equals(tmp)){
        u.setType("初中");
        continue;
      }
      if("切换为高中".equals(tmp)){
        u.setType("高中");
        continue;
      }
      return ;
    }
  }

  public ArrayList<String> paper(int cnt,User u) throws IOException {
    Random r = new Random();
    String[] specialSigns = {"²","√","sin","cos","tan"};
    ArrayList<String> infixQues = new ArrayList<>();
    HashSet<String> hisQ = historyPaper(u);

    String path = Objects.requireNonNull(this.getClass().getResource("")).getPath() + u.getId() + "/";
    Calendar date = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    String time = formatter.format(date.getTime());
    path = path + time + ".txt";
    //生成题目
    FileWriter fw = new FileWriter(path,true);
    for(int i = 0;i < cnt;i++){
      String question;
      //生成单个题目
      question = singleQuestion(u, r, specialSigns);
      //查询是否与历史题目重复
      if(hisQ.contains(question)){
        i--;
        continue;
      }
      infixQues.add(question);
      question = "第" + (i + 1) + "题: " + question + "=";
      fw.write(question+"\r\n");
    }
    fw.close();
    return infixQues;
  }

  private String singleQuestion(User u, Random r, String[] specialSigns) {
    String[] signs = {"+","-","×","÷"};
    String question = null;
    String[] opQ = null;
    //StringBuilder infixStr = new StringBuilder();
    String type= u.getType();
    switch (type){
      case "小学" -> {
        int opNum = r.nextInt(4) + 2;
        opQ = new String[opNum];
        for(int i = 0;i < opNum;i++){
          opQ[i] = String.valueOf(r.nextInt(100) + 1);
        }
        //括号
        addBracket(r, opQ, opNum);
      }
      case "初中" -> {
        int opNum = r.nextInt(5) + 1;
        opQ = new String[opNum];
        for(int i = 0;i < opNum;i++){
          opQ[i] = String.valueOf(r.nextInt(100) + 1);
        }
        //特殊符号数
        int sopNum = r.nextInt(opNum) + 1;
        while((sopNum--) != 0){
          int rPos = r.nextInt(opNum);
          if(!opQ[rPos].contains("²") && !opQ[rPos].contains("√")){
            opQ[rPos]= r.nextBoolean()? opQ[rPos]+"²": "√"+opQ[rPos];
          }
        }
        addBracket(r, opQ, opNum);
      }
      case "高中" -> {
        int opNum = r.nextInt(5) + 1;
        opQ = new String[opNum];
        for(int i = 0;i < opNum;i++){
          opQ[i] = String.valueOf(r.nextInt(100) + 1);
        }
        //特殊符号数
        int sopNum = r.nextInt(opNum) + 1;
        boolean[] speOpFlag = new boolean[opNum];
        //确保至少有一个三角函数
        int tri = 0;
        while((sopNum--) != 0){
          int rPos = r.nextInt(opNum);
          if(tri == 0 && !opQ[rPos].contains("sin") && !opQ[rPos].contains("cos") && !opQ[rPos].contains("tan")){
            opQ[rPos] = specialSigns[r.nextInt(3)+2] + opQ[rPos];
            tri++;
            speOpFlag[rPos] = true;
          }
          if(!speOpFlag[rPos]){
            int rOp = r.nextInt(5);
            opQ[rPos] = rOp < 1 ? opQ[rPos] + specialSigns[rOp] : specialSigns[rOp] + opQ[rPos];
            speOpFlag[rPos] = true;
          }
        }
        addBracket(r, opQ, opNum);
      }
      default -> System.out.println("error");
    }
    assert opQ != null;

    for(int i = 0;i < opQ.length ;i++){
      int opType = r.nextInt(4);
      question = (question == null?"":question) + (i!=0?signs[opType]:"") + opQ[i];
    }
    return question;
  }

  private void addBracket(Random r, String[] opQ, int opNum) {
    boolean bracket = r.nextBoolean() && opNum > 2;
    if(bracket){
      int range = r.nextInt(opNum - 2) + 1;
      int left = r.nextInt(opNum - range);
      opQ[left] = "(" + opQ[left];
      opQ[left+range] =  opQ[left+range] + ")";
    }
  }

  private HashSet<String> historyPaper(User u) throws IOException {
    HashSet<String> hisQuestions = new HashSet<>();
    String path = Objects.requireNonNull(this.getClass().getResource("")).getPath() + "\\" + u.getId();
    File folder = new File(path);
    if(!folder.exists()){
      folder.mkdir();
    }
    File[] fList = folder.listFiles();
    assert fList != null;
    for(File f : fList){
      if (f.isFile() && f.getName().endsWith(".txt")){
        BufferedReader br = new BufferedReader(new FileReader(f));
        String s;
        while((s = br.readLine()) != null){
          hisQuestions.add(s);
        }
        br.close();
      }
    }
    return hisQuestions;
  }
}
