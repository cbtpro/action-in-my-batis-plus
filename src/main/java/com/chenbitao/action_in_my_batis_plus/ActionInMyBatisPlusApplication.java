package com.chenbitao.action_in_my_batis_plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chenbitao.action_in_my_batis_plus.mapper")
public class ActionInMyBatisPlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActionInMyBatisPlusApplication.class, args);
	}

}
