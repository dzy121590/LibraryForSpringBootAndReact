package com.dongzhiyuan.lib.api.service.borrow;

import com.dongzhiyuan.lib.api.constants.ResultDTO;

import java.util.Date;

public interface BookBorrowService {


    ResultDTO doBookBorrow(int bookId, Date startDate, Date endDate, int borrowCount, int userId, String userName, boolean vipFlag);
}
