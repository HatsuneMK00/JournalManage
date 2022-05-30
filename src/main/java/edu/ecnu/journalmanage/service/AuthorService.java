package edu.ecnu.journalmanage.service;

import edu.ecnu.journalmanage.mapper.UserMapper;
import edu.ecnu.journalmanage.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    final
    UserMapper userMapper;

    public AuthorService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


}
