package training;

import java.util.ArrayList;
import java.util.List;

public class InfoBase {

    private static List<User> users = new ArrayList<>();

    static { users.add(new User("first", "user", "ADMIN", "asdf@dfo.com")); }

    public static List<User> getUsers(){
        return users;
    }

    public static boolean addUser(User user) {
        return users.add(user);
    }

    public static User findUserByLogin(String login) {
        for (User user: users) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public static boolean deleteUserByLogin(String login) {
        return users.removeIf(user -> user.getLogin().equals(login));
    }

    public static boolean updateUser(User updating) {
        boolean updated = false;
        for (User user: users) {
            if (user.equals(updating)){
                user.setEmail(updating.getEmail());
                user.setName(updating.getName());
                user.setLastname(updating.getLastname());
                updated = true;
            }
        }
        return updated;
    }
}
