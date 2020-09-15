package wang.yeting.wtp.admin.service;

/**
 * @author : weipeng
 * @date : 2020-08-05 10:24
 */

public interface EmailService {

    Boolean sendMail(String to, String text, String title);
}
