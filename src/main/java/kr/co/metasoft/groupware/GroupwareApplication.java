package kr.co.metasoft.groupware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.metasoft.groupware.api.app.service.AccountService;
import kr.co.metasoft.groupware.api.common.service.UserService;

@SpringBootApplication
public class GroupwareApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(GroupwareApplication.class, args);
    }
}


@RestController
@RequestMapping (path = "api")
class ApiController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @GetMapping (path = "")
    public ResponseEntity<Object> main() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}