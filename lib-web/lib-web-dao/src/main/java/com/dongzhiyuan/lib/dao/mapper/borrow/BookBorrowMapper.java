package com.dongzhiyuan.lib.dao.mapper.borrow;

import com.dongzhiyuan.lib.api.model.borrow.BookBorrowDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 油炸小波
 * @version 1.0
 * @date 2020/10/11 7:43
 */
@Mapper
public interface BookBorrowMapper {

    int insertBorrow(BookBorrowDTO bookBorrowDTO);
}
