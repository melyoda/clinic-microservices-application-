package com.clinic.appointment_service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


@Component
@org.springframework.web.context.annotation.RequestScope(
        proxyMode = org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS
)
public class CurrentUser {
    private String username;
    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }
}

//@Component
//@RequestScope // This creates a new instance for each HTTP request
//public class CurrentUser {
//    private String username;
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//}