package com.example.namo2.category;

import com.example.namo2.category.dto.CategoryDto;
import com.example.namo2.category.dto.CategoryIdRes;
import com.example.namo2.category.dto.PostCategoryReq;
import com.example.namo2.config.exception.BaseException;
import com.example.namo2.entity.Category;
import com.example.namo2.entity.Palette;
import com.example.namo2.entity.User;
import com.example.namo2.entity.enumerate.CategoryStatus;
import com.example.namo2.palette.PaletteDao;
import com.example.namo2.schedule.ScheduleDao;
import com.example.namo2.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.namo2.config.response.BaseResponseStatus.JPA_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_CATEGORY_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_PALETTE_FAILURE;
import static com.example.namo2.config.response.BaseResponseStatus.NOT_FOUND_USER_FAILURE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryDao categoryDao;
    private final PaletteDao paletteDao;
    private final ScheduleDao scheduleDao;
    private final UserDao userDao;

    @Transactional(readOnly = false)
    public CategoryIdRes create(Long userId, PostCategoryReq postcategoryReq) throws BaseException {
        User user = userDao.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
        Palette palette = paletteDao.findById(postcategoryReq.getPalletId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        Category category = Category.builder()
                .name(postcategoryReq.getName())
                .user(user)
                .palette(palette)
                .share(postcategoryReq.isShare())
                .build();
        Category savedCategory = categoryDao.save(category);
        return new CategoryIdRes(savedCategory.getId());
    }

    public List<CategoryDto> findAll(Long userId) throws BaseException {
        List<Category> categories = categoryDao.findCategoriesByUserIdAndStatusEquals(userId, CategoryStatus.ACTIVE);
        return categories.stream()
                .map((category) -> new CategoryDto(category.getId(), category.getName(),
                        category.getPalette().getId(), category.getShare()))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = false)
    public CategoryIdRes update(Long categoryId, PostCategoryReq postcategoryReq) throws BaseException {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        Palette palette = paletteDao.findById(postcategoryReq.getPalletId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PALETTE_FAILURE));
        category.update(postcategoryReq.getName(), postcategoryReq.isShare(), palette);
        return new CategoryIdRes(category.getId());
    }

    @Transactional(readOnly = false)
    public void delete(Long categoryId) throws BaseException {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_CATEGORY_FAILURE));
        category.delete();
    }
}
