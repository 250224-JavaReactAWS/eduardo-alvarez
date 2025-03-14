package com.revature.repos;
import com.revature.models.*;

public interface UserDAO {
    User createUser(User user);

    User getUserByID(int userID);

    User updateUser(User user);
}
