package com.khovaylo.surf.repository;

import com.khovaylo.surf.model.Expression;
import com.khovaylo.surf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Pavel Khovaylo
 */
@Repository
@Transactional(readOnly = true)
public interface ExpressionRepository extends JpaRepository<Expression, Long> {

    List<Expression> findAllByValue(String value);

    @Query("SELECT u FROM Expression u WHERE u.user.id = :id")
    List<Expression> findByUserId(Long id);

    List<Expression> findByCreatedIsAfterAndCreatedIsBefore(ZonedDateTime start, ZonedDateTime finish);
}
