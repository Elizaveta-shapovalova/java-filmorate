package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {

   Long id;
   @NotBlank
   @Email
   String email;
   @NotBlank
   String login;
   String name;
   @PastOrPresent
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   LocalDate birthday;

   public Map<String, Object> toMap() {
      Map<String, Object> values = new HashMap<>();
      values.put("name", name);
      values.put("email", email);
      values.put("login", login);
      values.put("birthday", birthday);
      return values;
   }

}
