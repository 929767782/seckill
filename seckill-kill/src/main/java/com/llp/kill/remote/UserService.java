package com.llp.kill.remote;

import com.llp.common.entity.User;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    User getByToken(HttpServletResponse response, String token);
}
