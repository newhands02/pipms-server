package com.pipms.exception;

/**
 * @ClassName UserException
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/2414:18
 * @Version 1.0
 **/
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
