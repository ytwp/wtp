package wang.yeting.wtp.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weipeng
 */
@SpringBootApplication
@MapperScan("wang.yeting.wtp.admin.mapper")
public class WtpAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtpAdminApplication.class, args);
    }

}
