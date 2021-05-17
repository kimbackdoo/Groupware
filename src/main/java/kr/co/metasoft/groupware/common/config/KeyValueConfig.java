package kr.co.metasoft.groupware.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@Configuration
@EnableMapRepositories (basePackages = {"kr.co.metasoft.groupware.common.keyvalue"})
public class KeyValueConfig {

}