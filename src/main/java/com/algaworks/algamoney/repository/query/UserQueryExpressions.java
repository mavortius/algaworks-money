package com.algaworks.algamoney.repository.query;

import com.algaworks.algamoney.model.QPermission;
import com.algaworks.algamoney.model.QUser;
import com.algaworks.algamoney.model.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;

public class UserQueryExpressions extends QuerydslExpressionSupport {
  public static final QUser USER = QUser.user;

  public static Predicate userByPermissionDescription(String description) {
    return USER.permissions.any().description.eq(description);
  }

  public static JPQLQuery<User> emailEquals(String email) {
    return selectFrom(USER)
            .leftJoin(USER.permissions).fetchJoin()
            .where(USER.email.eq(email));
  }
}
