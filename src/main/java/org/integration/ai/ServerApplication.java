package org.integration.ai;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Title ServerApplication$<br>  
 * Description ServerApplication$<br>  
 *  
 * @author senyang  
 * @date 2025-04-19
 * @since 0.0.1-SNAPSHOT
 */
@MapperScan("org.integration.ai.domain.**.mapper")
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
