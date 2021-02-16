package com.khovaylo.surf.dto.converter.impl;

import com.khovaylo.surf.dto.ExpressionDto;
import com.khovaylo.surf.dto.converter.Converter;
import com.khovaylo.surf.exception.NotFoundException;
import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.model.User;
import com.khovaylo.surf.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Pavel Khovaylo
 */
@Component
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExpressionDtoConverter implements Converter<ExpressionDto, Expression> {

    UserRepository userRepository;

    @Override
    public Expression toModel(ExpressionDto dto) {
        if (dto == null) return null;

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User doesn't exist"));

        return new Expression(dto.getId(), dto.getValue(), null, null, user);
    }

    @Override
    public ExpressionDto toDto(Expression model) {
        if (model == null) return null;

        return new ExpressionDto(model.getId(), model.getValue(), model.getResult(), model.getCreated(), model.getUser().getId());
    }
}