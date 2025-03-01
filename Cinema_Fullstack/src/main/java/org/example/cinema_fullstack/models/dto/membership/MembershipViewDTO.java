package org.example.cinema_fullstack.models.dto.membership;

public interface MembershipViewDTO {
    Long getId();
    String getName();
    String getMemberCode();
    String getCard();
    String getPhone();
    String getEmail();
    String getBirthday();
    String getGender();
    String getImageURL();
    Long getWardId();
}
