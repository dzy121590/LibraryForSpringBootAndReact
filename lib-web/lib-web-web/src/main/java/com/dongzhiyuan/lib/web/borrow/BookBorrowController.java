package com.dongzhiyuan.lib.web.borrow;
import com.alibaba.fastjson.JSONObject;
import com.dongzhiyuan.lib.api.constants.ResultDTO;
import com.dongzhiyuan.lib.api.enums.HttpCode;
import com.dongzhiyuan.lib.api.service.borrow.BookBorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("/borrow")
@RestController
public class BookBorrowController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private BookBorrowService bookBorrowService;



    @RequestMapping("/doBorrow")
    public ResultDTO doBorrow(@RequestBody JSONObject jsonObject) {

        //1.非法校验
        try {
            int bookId = Integer.parseInt(String.valueOf(jsonObject.getOrDefault("bookId", 0)));
            Date startDate = jsonObject.getDate("startDate");
            Date endDate = jsonObject.getDate("endDate");
            int borrowCount = jsonObject.getInteger("borrowCount");
            int userId = jsonObject.getInteger("userId");
            String userName = jsonObject.getString("userName");
            boolean vipFlag = jsonObject.getBoolean("vipFlag");

            return bookBorrowService.doBookBorrow(bookId,startDate,endDate,borrowCount,userId,userName,vipFlag);
        } catch (Exception e) {
            logger.error("系统异常" + e);
            return new ResultDTO(HttpCode.EXCEPTION.getCode(), "系统异常");
        }

    }
}
