package com.pipms.exception;

/**
 * @ClassName UserNotExistsException
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2414:20
 * @Version 1.0
 **/
public class UserNotExistsException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserNotExistsException()
    {
        super("user.not.exists", null);
    }
}
