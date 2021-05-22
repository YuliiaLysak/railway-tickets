package config;

import model.Role;

import java.util.*;

public class SecurityConfig {

    public static final Role ADMIN = Role.ADMIN;
    public static final Role USER = Role.USER;

    private static final Map<Role, List<String>> mapConfig = new HashMap<>();

    static {
        init();
    }

    //    TODO - check if other pages needed to be secured with authorization
    private static void init() {

        // Configure for Role.USER
        List<String> userUrlPatterns = new ArrayList<>();
        userUrlPatterns.add("/api/tickets");
        mapConfig.put(USER, userUrlPatterns);

        // Configure For Role.ADMIN
        List<String> adminUrlPatterns = new ArrayList<>();
        adminUrlPatterns.add("/admin/*");
        adminUrlPatterns.add("/api/routes/*");
        adminUrlPatterns.add("/api/stations/*");
        mapConfig.put(ADMIN, adminUrlPatterns);
    }

    public static Set<Role> getAllAppRoles() {
        return mapConfig.keySet();
    }

    public static List<String> getUrlPatternsForRole(Role role) {
        return mapConfig.get(role);
    }
}
