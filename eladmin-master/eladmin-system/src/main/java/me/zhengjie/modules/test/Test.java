package me.zhengjie.modules.test;

import org.hibernate.SessionFactory;
import org.springframework.boot.web.servlet.server.Session;
import sun.misc.Launcher;

public class Test {
    public static String apiKey = "XXXXXX";
    public static String urlWithRecaptcha = "http://XXXXXX:8080/index3.html";
    public static void main(String[] args) {

        Launcher launcher = new Launcher();
        try (SessionFactory factory = launcher.launch();
             Session session = factory.create(new SessionSettings())) {
            session.navigate(urlWithRecaptcha);
            session.waitDocumentReady(60000);
            String content = session.getContent();

            System.out.println(content);
            String sitekey = getSitekey(content);
            System.out.println("sitekey = " + sitekey);

            // 2
            String url = "https://2captcha.com/in.php?key="+ apiKey +"&method=userrecaptcha&googlekey="+ sitekey +"&pageurl=" + urlWithRecaptcha;

            Document document = Jsoup.connect(url).get();
            System.out.println(document);

            String captchaId = StringUtils.substringAfter(document.text(), "OK|");
            System.out.println("captchaId = " + captchaId);


            // 3
            String documentText;
            do {
                Thread.sleep(5000);
                document = Jsoup.connect("http://2captcha.com/res.php?key="+ apiKey +"&action=get&id=" + captchaId).get();
                documentText = document.text();
            } while (!documentText.contains("OK"));
            String captchaResponse = StringUtils.substringAfter(documentText, "OK|");
            System.out.println("captchaResponse = " + captchaResponse);
            // 4

            session.setAttribute("#g-recaptcha-response", "style", "");

            session.setValue("#g-recaptcha-response", captchaResponse);

            Thread.sleep(5000);


　　　　　　　// 我自己注册了 google captcha, 前端页面我可以自己控制，这里abc，是页面 submit 元素的ID。
            session.click("#abc");

            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            launcher.kill();
        }

    }

    /**
     * 获取 data-sitekey 值
     * @param content
     * @return
     */
    public static String getSitekey(String content) {
        if(content == null) {
            return null;
        }

        Element element = Jsoup.parse(content).selectFirst(".g-recaptcha");
        if(element == null) {
            return null;
        }

        return element.attr("data-sitekey");
    }

}
