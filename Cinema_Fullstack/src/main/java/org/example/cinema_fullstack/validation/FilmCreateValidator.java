package org.example.cinema_fullstack.validation;

import org.example.cinema_fullstack.models.dto.film.CreateFilmDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class FilmCreateValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CreateFilmDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        CreateFilmDTO film = (CreateFilmDTO) object;

        // Name validation
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            errors.rejectValue("name", "name.null", "Tên phim không được để trống");
        } else if (film.getName().length() > 300) {
            errors.rejectValue("name", "maxlength", "Tên phim dài tối đa 300 ký tự");
        } else if (!film.getName().matches("^[\\p{L}0-9\\s-_]+$")) {
            errors.rejectValue("name", "name.pattern", "Tên phim không bao gồm ký tự đặc biệt");
        }

        // Image validation
        if (film.getImageFile() == null || film.getImageFile().isEmpty()) {
            errors.rejectValue("imageFile", "image.null", "Poster không được để trống");
        } else {
            String fileName = film.getImageFile().getOriginalFilename();
            if (fileName != null && !fileName.matches(".*\\.(jpg|jpeg|png)$")) {
                errors.rejectValue("imageFile", "image.pattern", "Chỉ chấp nhận file jpg, png, jpeg");
            }
        }

        // Start Date validation
        if (film.getStartDate() == null) {
            errors.rejectValue("startDate", "startDate.null", "Ngày khởi chiếu không được để trống");
        } else if (film.getStartDate().isBefore(LocalDate.now())) {
            errors.rejectValue("startDate", "startDate.future", "Ngày khởi chiếu phải là ngày trong tương lai");
        }

        // End Date validation
        if (film.getEndDate() == null) {
            errors.rejectValue("endDate", "endDate.null", "Ngày kết thúc không được để trống");
        } else if (film.getStartDate() != null && film.getEndDate().isBefore(film.getStartDate())) {
            errors.rejectValue("endDate", "endDate.current", "Ngày kết thúc phải sau ngày khởi chiếu");
        }

        // Actors validation
        if (film.getActors() == null || film.getActors().trim().isEmpty()) {
            errors.rejectValue("actors", "actors.null", "Diễn viên không được để trống");
        } else if (film.getActors().length() > 300) {
            errors.rejectValue("actors", "maxlength", "Diễn viên dài tối đa 300 ký tự");
        }

        // Studio validation
        if (film.getStudio() == null || film.getStudio().trim().isEmpty()) {
            errors.rejectValue("studio", "studio.null", "Hãng phim không được để trống");
        } else if (film.getStudio().length() > 300) {
            errors.rejectValue("studio", "maxlength", "Hãng phim dài tối đa 300 ký tự");
        }

        // Directors validation
        if (film.getDirectors() == null || film.getDirectors().trim().isEmpty()) {
            errors.rejectValue("directors", "directors.null", "Đạo diễn không được để trống");
        } else if (film.getDirectors().length() > 300) {
            errors.rejectValue("directors", "maxlength", "Đạo diễn dài tối đa 300 ký tự");
        }

        // Duration validation
        if (film.getDuration() <= 0) {
            errors.rejectValue("duration", "duration.null", "Thời lượng không được để trống và phải lớn hơn 0");
        }

        // Trailers validation
        if (film.getTrailers() == null || film.getTrailers().trim().isEmpty()) {
            errors.rejectValue("trailers", "trailers.null", "Trailer không được để trống");
        }

        // Category validation
        String categoryString = film.getCategoryAsString();
        if (categoryString == null || categoryString.trim().isEmpty()) {
            errors.rejectValue("category", "category.null", "Thể loại không được để trống");
        }

        // Description validation
        if (film.getDescription() == null || film.getDescription().trim().isEmpty()) {
            errors.rejectValue("description", "description.null", "Nội dung không được để trống");
        }

        // Age validation
        if (film.getAge() == null || film.getAge().trim().isEmpty() || film.getAge().equals("-Chọn-")) {
            errors.rejectValue("age", "age.null", "Tuổi giới hạn không được để trống");
        }

    }
}
