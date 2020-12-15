package com.dongzhiyuan.lib.service.borrow;

import com.dongzhiyuan.lib.api.constants.ResultDTO;
import com.dongzhiyuan.lib.api.enums.HttpCode;
import com.dongzhiyuan.lib.api.enums.ValidFlagEnum;
import com.dongzhiyuan.lib.api.model.book.BookDTO;
import com.dongzhiyuan.lib.api.model.borrow.BookBorrowDTO;
import com.dongzhiyuan.lib.api.service.borrow.BookBorrowService;
import com.dongzhiyuan.lib.dao.mapper.book.BookMapper;
import com.dongzhiyuan.lib.dao.mapper.borrow.BookBorrowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class BookBorrowServiceImpl implements BookBorrowService {

    @Autowired
    private BookBorrowMapper bookBorrowMapper;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public ResultDTO doBookBorrow(int bookId, Date startDate, Date endDate, int borrowCount, int userId, String userName, boolean vipFlag) {
        if (0 >= bookId) {

            return new ResultDTO(HttpCode.FAIL.getCode(), "图书不存在");
        }

        if (startDate.after(endDate)) {
            return new ResultDTO(HttpCode.FAIL.getCode(), "起始日期不能大于终止日期");
        }
        //2.查找书籍
        if (borrowCount <= 0) {
            return new ResultDTO(HttpCode.FAIL.getCode(), "借阅数量需要大于0");
        }
        BookDTO bookDTO = bookMapper.findById(bookId);
        if (null == bookDTO) {
            return new ResultDTO(HttpCode.FAIL.getCode(), "书籍不存在");
        }

        int bookCount = bookDTO.getBookCount();
        if (bookCount <= 0) {
            // TODO 在查找书籍的时候如果返回的书籍数量为0，前端直接禁用租借按钮，同时加一个效果：当前书籍已租借光了~
            return new ResultDTO(HttpCode.FAIL.getCode(), "当前书籍已被租借光了，请看看别的书吧~");
        }

        if (bookCount < borrowCount) {
            return new ResultDTO(HttpCode.FAIL.getCode(), "借书数量不能大于剩余书籍数量，当前书籍数量：" + bookCount);
        }
        int result = doInsertBorrowRecord(bookId, bookDTO, borrowCount, startDate, endDate, userId, userName, vipFlag);
        if (result <= 0) {
            //新增失败
            return new ResultDTO(HttpCode.FAIL.getCode(), "借书失败，联系管理员");
        }

        //4.减少书籍数量
        bookDTO.setBookCount(bookCount - borrowCount);
        bookDTO.setUpdateDate(new Date());
        bookMapper.update(bookDTO);
        //借书成功
        return new ResultDTO(HttpCode.SUCCESS.getCode(), "借书成功");
    }

    private int doInsertBorrowRecord(int bookId, BookDTO bookDTO, int borrowCount, Date startDate, Date endDate, int userId, String userName, boolean vipFlag) {
        //3.借书
        BookBorrowDTO bookBorrowDTO = new BookBorrowDTO();
        bookBorrowDTO.setBookId(bookId);
        bookBorrowDTO.setBookName(bookDTO.getBookName());
        bookBorrowDTO.setCount(borrowCount);
        bookBorrowDTO.setStartDate(startDate);
        bookBorrowDTO.setEndDate(endDate);
        bookBorrowDTO.setUserId(userId);
        bookBorrowDTO.setUserName(userName);
        //书籍价格
        BigDecimal bookPrice = bookDTO.getBookPrice();
        //借阅天数
        long days = (endDate.getTime() - startDate.getTime());
        long totalPrice = bookPrice.intValue() * days;
        bookBorrowDTO.setPrice(new BigDecimal(totalPrice));
        bookBorrowDTO.setTradeFee(new BigDecimal(totalPrice));
        if (vipFlag) {
            bookBorrowDTO.setTradeFee(new BigDecimal(0.0));
        }
        bookBorrowDTO.setCreateDate(new Date());
        bookBorrowDTO.setValidFlag(ValidFlagEnum.ENABLE);

        int result = bookBorrowMapper.insertBorrow(bookBorrowDTO);
        return result;
    }


}
