package co.empathy.academy.search.services;


import co.empathy.academy.search.entities.User;
import co.empathy.academy.search.exceptions.UserAlreadyExistsException;
import co.empathy.academy.search.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    User getUser(Long userId) throws UserNotFoundException;
    List<User> getUsers();
    void addUser(User user) throws UserAlreadyExistsException;
    void deleteUser(Long userId) throws UserNotFoundException;
    User updateUser(Long id, User user) throws UserNotFoundException;

}
