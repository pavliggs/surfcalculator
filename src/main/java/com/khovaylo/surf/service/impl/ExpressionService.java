package com.khovaylo.surf.service.impl;

import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.repository.ExpressionRepository;
import com.khovaylo.surf.service.CreateService;
import com.khovaylo.surf.service.GetListService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Pavel Khovaylo
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_={@Autowired, @NonNull})
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExpressionService implements CreateService<Expression>, GetListService<Expression> {

    ExpressionRepository expressionRepository;

    @Transactional
    @Override
    public void create(Expression model) {
        model.setCreated(ZonedDateTime.now());
        expressionRepository.save(model);
    }

    @Override
    public List<Expression> getAll() {
        return expressionRepository.findAll();
    }
}
