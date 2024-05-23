package com.framework.base_set.domain.user.model.refresh;

import lombok.Data;

@Data
public class RefreshRS {

    private String accessToken;

    private String refreshToken;
}
