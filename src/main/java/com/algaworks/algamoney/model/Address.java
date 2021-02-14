package com.algaworks.algamoney.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class Address {
  @Column(name = "address")
  private String name;
  private String number;
  private String complement;
  private String district;
  private String zipCode;

  @ManyToOne(fetch = FetchType.LAZY)
  private City city;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getComplement() {
    return complement;
  }

  public void setComplement(String complement) {
    this.complement = complement;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Address)) return false;

    Address address = (Address) o;

    if (getName() != null ? !getName().equals(address.getName()) : address.getName() != null) return false;
    if (getNumber() != null ? !getNumber().equals(address.getNumber()) : address.getNumber() != null) return false;
    if (getComplement() != null ? !getComplement().equals(address.getComplement()) : address.getComplement() != null)
      return false;
    if (getDistrict() != null ? !getDistrict().equals(address.getDistrict()) : address.getDistrict() != null)
      return false;
    if (getZipCode() != null ? !getZipCode().equals(address.getZipCode()) : address.getZipCode() != null) return false;
    return getCity() != null ? getCity().equals(address.getCity()) : address.getCity() == null;
  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
    result = 31 * result + (getComplement() != null ? getComplement().hashCode() : 0);
    result = 31 * result + (getDistrict() != null ? getDistrict().hashCode() : 0);
    result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
    result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
    return result;
  }
}
