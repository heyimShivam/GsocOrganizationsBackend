package com.organization.gsoc.DTO;

public record OrganizationContactDTO(
    String ircChannel,
    String contactEmail,
    String mailingList,
    String twitterUrl,
    String blogUrl,
    String facebookUrl
) {}
