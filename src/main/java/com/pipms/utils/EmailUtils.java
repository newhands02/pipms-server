package com.pipms.utils;
import com.pipms.entity.ProjectInfo;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
/**
 * @ClassName EmailUtils
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/211:33
 * @Version 1.0
 **/
@Component
public class EmailUtils {
    private String from="661595@gree.com.cn";
    private String host="mail.gree.com.cn";
    private String password="Yujie*0909";

    private Session getSession(){
        Properties properties = System.getProperties();
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.host", host);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.startttls.enable", "true");
        Session session = Session.getInstance(properties);
        return session;
    }
    public boolean sendEmail(String receiverAddress,String subject,String content){
        Session session = this.getSession();
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverAddress));
            msg.setSubject(subject);
            msg.setContent(this.setContent(content));
            Transport transport = session.getTransport();
            transport.connect(from, password);
            transport.sendMessage(msg, new Address[] { new InternetAddress(receiverAddress) });
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private Multipart setContent(String content){
        Multipart multipart = new MimeMultipart();

        MimeBodyPart text = new MimeBodyPart();

        try {
            text.setContent(content,"text/html;charset=UTF-8");
            multipart.addBodyPart(text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return multipart;
    }
    public String drawTable(String receiverName, ProjectInfo projectInfo,String url,String comment){
        StringBuffer buffer=new StringBuffer("<html>" +
                "<head>" +
                "<style>" +
                "table,table tr td {border:1px solid black;}" +
                "table {text-align:center;border-collapse:collapse;padding:2px;}" +
                "</style></head><body>");
        buffer.append("<div>" +
                "<span>"+receiverName+"您好：<br>  以下业务已到您处，请尽快处理<span>" +
                "</div><br>");
        buffer.append("<table>")
                .append("<tr>" +
                        "<td></td><td>所属版块</td><td>单位</td><td>项目编号</td><td>项目提出人</td><td>结题时间</td><td>当前状态</td><td>审核意见</td><td></td>" +
                        "</tr>")
                .append("<tr>" +
                        "<td>1</td>" +
                        "<td>"+projectInfo.getSection()+"</td><td>"+projectInfo.getUnit()+"</td><td>"+projectInfo.getProjectNumber()+"</td>" +
                        "<td>"+projectInfo.getProjectSponsor()+"</td><td>"+projectInfo.getFinishTime()+"</td><td>"+projectInfo.getCurrentState()+"</td>" +
                        "<td>"+comment+"</td>"+"<td><a href='"+url+"'>查看</a></td>" +
                        "</tr>");
        buffer.append("</table></body></html>");
        return buffer.toString();
    }
    public static String getApprovePageUrl(HttpServletRequest request,String routerPath){
        String[] referers = request.getHeader("Referer").replace("//","/").split("/");
        String url=referers[0]+"//"+referers[1]+routerPath;
        return url;
    }
}
