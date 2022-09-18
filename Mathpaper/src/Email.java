

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class Email {
    final String emailAccount = "303478145@qq.com" ;
    //private final String emailAccount = "1823560089@qq.com" ;//mmk
    final String authCode = "xuggadkckzjwbgde";
    //private final String authCode = "weczargowizsbibd";//mmk
    private String recEmailAddr;
    private String code;

    public static void main(String[] args) throws Exception {
        Email mail = new Email();
        mail.sendQQEmail(mail.emailAccount,mail.authCode,"303478145@qq.com",getRandom(),"admin");
    }
    /**
     * 发送邮件(参数自己根据自己的需求来修改，发送短信验证码可以直接套用这个模板)
     *
     * @param from_email 发送人的邮箱
     * @param pwd        发送人的授权码
     * @param recEmailAddr  接收人的邮箱
     * @param code       验证码
     * @param name       收件人的姓名
     * @return
     */
    public String sendQQEmail(String from_email, String pwd, String recEmailAddr, String code, String name)throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");     //使用smpt的邮件传输协议
        props.setProperty("mail.smtp.host", "smtp.qq.com");       //主机地址
        props.setProperty("mail.smtp.auth", "true");      //授权通过

        Session session = Session.getInstance(props);     //通过我们的这些配置，得到一个会话程序

        try {

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from_email));     //设置发件人
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recEmailAddr,"用户","utf-8"));      //设置收件人
            message.setSubject("HNU AutoExam注册验证码","utf-8");      //设置主题
            message.setSentDate(new Date());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String str = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body><p style='font-size: 20px;font-weight:bold;'>尊敬的："+name+"，您好！</p>"
                    + "<p style='text-indent:2em; font-size: 20px;'>欢迎注册，您本次的验证码是 "
                    + "<span style='font-size:30px;font-weight:bold;color:red'>" + code + "</span>，1分钟之内有效，请尽快使用！</p>"
                    + "<p style='text-align:right; padding-right: 20px;'"
                    + "<a href='https://www.hnu.edu.cn/' style='font-size: 18px'>湖南大学</a></p>"
                    + "<span style='font-size: 18px; float:right; margin-right: 60px;'>" + sdf.format(new Date()) + "</span></body></html>";

            Multipart mul=new MimeMultipart();  //新建一个MimeMultipart对象来存放多个BodyPart对象
            BodyPart mdp=new MimeBodyPart();  //新建一个存放信件内容的BodyPart对象
            mdp.setContent(str, "text/html;charset=utf-8");
            mul.addBodyPart(mdp);  //将含有信件内容的BodyPart加入到MimeMultipart对象中
            message.setContent(mul); //把mul作为消息内容


            message.saveChanges();

            //创建一个传输对象
            Transport transport=session.getTransport("smtp");

            //建立与服务器的链接  465端口是 SSL传输
            transport.connect("smtp.qq.com", 587, from_email, pwd);

            //发送邮件
            transport.sendMessage(message,message.getAllRecipients());

            //关闭邮件传输
            transport.close();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return "注册成功";
    }
    public static String getRandom() {
        String num = "";
        for (int i = 0 ; i < 6 ; i ++) {
            num = num + (int) Math.floor(Math.random() * 9 + 1);
        }
        return num;
    }

}
