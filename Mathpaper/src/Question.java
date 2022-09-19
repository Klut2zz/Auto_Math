import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Question {
    private final Map<String, Integer> opPriority = new HashMap<>();
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        String res;
        ArrayList<String> suffix;
        Question q = new Question();
        q.init();
        suffix = q.infixToSuffix(s);
        res = q.calSuffix(suffix);
        System.out.println(res);
    }
    public void init(){
        opPriority.put("(",0);
        opPriority.put(")",0);
        opPriority.put("+",1);
        opPriority.put("-",1);
        opPriority.put("×",2);
        opPriority.put("÷",2);
    }
    public ArrayList<String> infixToSuffix(String infixStr){

        //定义一个操作符栈
        Stack <String> opStack = new Stack<>();
        //定义一个存后缀字符的线性表
        ArrayList<String> suffixList = new ArrayList<>();

        //格式化表达式
        infixStr = insertBlanks(infixStr);
        String[] infixList = infixStr.split(" ");
        infixList = procValue(infixList);
        System.out.println(Arrays.toString(infixList));

        for(String str : infixList){
            //若为数字，则加入数据集中
            Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$");
            if(pattern.matcher(str).matches()){
                suffixList.add(str);
            }else if("(".equals(str)){
                //左括号加入符号栈
                opStack.push(str);
            }else if(")".equals(str)){
                while (!"(".equals(opStack.peek())){
                    suffixList.add(opStack.pop());
                }
                opStack.pop();
            }else {
                while (!opStack.isEmpty() && opPriority.get(opStack.peek()) > opPriority.get(str)){
                    suffixList.add(opStack.pop());
                }
                opStack.push(str);
            }
        }
        while (!opStack.isEmpty()){
            suffixList.add(opStack.pop());
        }
        return suffixList;
    }

    public String calSuffix(ArrayList<String> suffix){
        Stack <String> cal = new Stack<>();
        String op = "+-×÷";
        for (String s : suffix) {
            Pattern pattern = Pattern.compile("^[-\\+]?\\d+(\\.\\d*)?|\\.\\d+$");
            if (pattern.matcher(s).matches()) {
                cal.push(s);
            } else if (op.contains(s)) {
                double right = Double.parseDouble(cal.pop());
                double left = Double.parseDouble(cal.pop());
                double res;
                switch (s) {
                    case "+" -> res = left + right;
                    case "-" -> res = left - right;
                    case "×" -> res = left * right;
                    case "÷" -> res = left / right;
                    default -> res = 0;
                }
                cal.push(String.valueOf(res));
            }
        }

        return cal.peek();
    }

    private String insertBlanks(String infixStr) {
        StringBuilder Str = new StringBuilder();
        String speOp = "()+-×÷²√";
        for(int i = 0 ; i < infixStr.length() ; i++){
            char c = infixStr.charAt(i);
            String s = Character.toString(c);
            if(speOp.contains(s)){
                Str.append(" ").append(c).append(" ");
            }else if (c == 's' || c == 'c' || c == 't' ){
                Str.append(" ").append(c).append(" ");
                i += 2;
            }else {
                Str.append(c);
            }
        }
        Str.delete(0,1);
        for(int i = 0;i < Str.length();i++){
            if(i+1<Str.length() && Str.charAt(i) == Str.charAt(i+1) && Str.charAt(i) ==' '){
                Str.delete(i,i+1);
                i++;
            }
        }
        return Str.toString();
    }

    private String[] procValue(String[] infixList) {
        StringBuilder strB = new StringBuilder();
        String speOp = "()+-×÷";
        for (int i = 0 ; i < infixList.length ; i++){
            if("s".equals(infixList[i])){
                strB.append(" ").append(Math.sin(Integer.parseInt(infixList[++i]))).append(" ");
            }else if("c".equals(infixList[i])){
                strB.append(" ").append(Math.cos(Integer.parseInt(infixList[++i]))).append(" ");
            }else if("t".equals(infixList[i])){
                int tmp = Integer.parseInt(infixList[++i]);
                double res = Math.tan(tmp);
                strB.append(" ").append(res).append(" ");
            }else if(i + 1 < infixList.length && "²".equals(infixList[i+1])){
                strB.append(" ").append(Math.pow(Integer.parseInt(infixList[i]), 2)).append(" ");
                i++;
            }else if("√".equals(infixList[i])){
                strB.append(" ").append(Math.sqrt(Integer.parseInt(infixList[++i]))).append(" ");
            }else {
                strB.append(" ").append(speOp.contains(infixList[i]) ? infixList[i]:Integer.parseInt(infixList[i])).append(" ");
            }
        }
        strB.delete(0,1);
        for(int i = 0;i < strB.length();i++){
            if(i+1<strB.length() && strB.charAt(i) == strB.charAt(i+1) && strB.charAt(i) ==' '){
                strB.delete(i,i+1);
                i++;
            }
        }
        return strB.toString().split(" ");
    }
}
