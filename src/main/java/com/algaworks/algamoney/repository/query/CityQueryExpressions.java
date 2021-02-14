package com.algaworks.algamoney.repository.query;

import com.algaworks.algamoney.model.City;
import com.algaworks.algamoney.model.QCity;
import com.querydsl.jpa.JPQLQuery;

public class CityQueryExpressions extends QuerydslExpressionSupport {
  public static final QCity CITY = QCity.city;

  public static JPQLQuery<City> cityByStateId(Long stateId) {
    return selectFrom(CITY).where(CITY.state.id.eq(stateId)).orderBy(CITY.name.asc());
  }
}
