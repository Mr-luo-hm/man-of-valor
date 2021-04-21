package cn.ntshare.laboratory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.ntshare.laboratory.mapper")
@SpringBootApplication
public class SpringBootShiroApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootShiroApplication.class, args);
    }
}
