package com.khovaylo.surf.service.impl;

import com.khovaylo.surf.exception.NotFoundException;
import com.khovaylo.surf.model.User;
import com.khovaylo.surf.repository.UserRepository;
import com.khovaylo.surf.service.CreateService;
import com.khovaylo.surf.service.GetListService;
import com.khovaylo.surf.service.GetService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Khovaylo
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements CreateService<User>, GetService<Long, User>, GetListService<User> {

    UserRepository userRepository;

    @Transactional
    @Override
    public void create(User model) {
        model.setExpressions(new ArrayList<>());
        userRepository.save(model);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
