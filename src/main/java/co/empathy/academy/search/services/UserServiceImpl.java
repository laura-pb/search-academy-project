package co.empathy.academy.search.services;

import co.empathy.academy.search.entities.User;
import co.empathy.academy.search.exceptions.UserAlreadyExistsException;
import co.empathy.academy.search.exceptions.UserNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {
    private ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    private void populateWithExampleUsers() {
        User user1 = new User(1L,"Name1", "Surname1");
        users.put(user1.getId(), user1);
        User user2 = new User(2L,"Name2", "Surname2");
        users.put(user2.getId(), user2);
    }

    @Override
    public User getUser(Long userId) throws UserNotFoundException {
        if (!users.containsKey(userId))
            throw new UserNotFoundException();
        else
            return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void addUser(User user) throws UserAlreadyExistsException {
        if (users.containsKey(user.getId()))
            throw new UserAlreadyExistsException();
        else
            users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        if (!users.containsKey(userId))
            throw new UserNotFoundException();
        else
            users.remove(userId);
    }

    @Override
    public User updateUser(Long id, User user) throws UserNotFoundException {
        User updatedUser = getUser(id);

        if (user.getName() != null)
            updatedUser.setName(user.getName());
        if (user.getEmail() != null)
            updatedUser.setEmail(user.getEmail());

        users.replace(id, updatedUser);

        return updatedUser;
    }
}
