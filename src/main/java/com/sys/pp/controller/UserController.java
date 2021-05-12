package com.sys.pp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.sys.pp.constant.Names;
import com.sys.pp.model.RolePK;
import com.sys.pp.model.Roles;
import com.sys.pp.model.Users;
import com.sys.pp.repo.RoleRepository;
import com.sys.pp.service.UserService;
import com.sys.pp.util.DateUtil;
import com.sys.pp.util.StringUtils;

@Controller
@RequestMapping("/admin/user")
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleService;

	@GetMapping("")
	public String viewAll() {
		return "layouts/admin/user-list";
	}

	@ResponseBody
	@RequestMapping(path = "/get/one/{id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	public Optional<Users> getById(@PathVariable String id) {
		return userService.findById(id);
	}

	@ResponseBody
	@GetMapping(path = "/get/{page}", produces = "application/json; charset=UTF-8")
	public List<Users> loadData(@PathVariable Integer page) {
		if (null == page) {
			return new ArrayList<>();
		}
		return userService.findByPageNumber(page);
	}

	/**
	 * Lưu người dùng
	 * 
	 * @param Map<String, String>
	 * @return status, obj added
	 */
	@ResponseBody
	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
	public Object save(@RequestBody Map<String, String> paramater) {
		// validate data
		Map<String, String> errors = this.validate(paramater, false);
		Map<String, Object> result = new HashMap<>();
		result.put("status", true);

		if (!errors.isEmpty()) {
			result.put("status", false);
			result.put("data", errors);
			return result;
		}

		try {
			Users user = this.createObject(paramater);
			userService.save(user);
			List<Roles> roles = this.createRoles(paramater, user.getUserId());
			roleService.saveAll(roles);

			result.put("data", user);
			return result;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OBJECT_EXISTS");
		}
	}

	/**
	 * Cập nhật thông tin người dùng
	 * 
	 * @param Map<String, String>
	 * @return status, obj updated
	 */
	@ResponseBody
	@PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
	public Object updateCategory(@RequestBody Map<String, String> paramater, @PathVariable String id) {
		// validate data
		Map<String, String> errors = this.validate(paramater, true);
		Map<String, Object> result = new HashMap<>();
		result.put("status", true);

		if (!errors.isEmpty()) {
			result.put("status", false);
			result.put("data", errors);
			return result;
		}

		try {
			Users user = this.createObject(paramater);
			user.setUserId(id);

			userService.save(user);
			result.put("data", user);
			return result;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OBJECT_EXISTS");
		}
	}

	/**
	 * Xóa người dùng
	 * 
	 * @param String idUser
	 */
	@ResponseBody
	@DeleteMapping(path = "/delete/{id}", produces = "application/json; charset=UTF-8")
	public void deleteById(@PathVariable String id) {
		userService.removeById(id);
	}

	/**
	 * Tạo object chứa quyền truy cập cho người dùng
	 * 
	 * @param Map<String, String>
	 * @return List<Roles> danh sách quyền
	 */
	private List<Roles> createRoles(Map<String, String> paramater, String userId) {
		List<Roles> roles = new ArrayList<Roles>();
		if (!StringUtils.isNullOrEmpty(paramater.get("role_admin"))
				|| !StringUtils.isNullOrEmpty(paramater.get("role_user"))) {
			if (!StringUtils.isNullOrEmpty(paramater.get("role_admin")) && "true".equals(paramater.get("role_admin"))) {
				Roles item = new Roles();
				item.setId(new RolePK(userId, Names.ROLES.ROLE_ADMIN.toString()));
				roles.add(item);
			}

			if (!StringUtils.isNullOrEmpty(paramater.get("role_user")) && "true".equals(paramater.get("role_user"))) {
				Roles item = new Roles();
				item.setId(new RolePK(userId, Names.ROLES.ROLE_USER.toString()));
				roles.add(item);
			}
		}
		return roles;
	}

	/**
	 * Tạo object thông tin người dùng
	 * 
	 * @param Map<String, String>
	 * @return Users người dùng
	 */
	private Users createObject(Map<String, String> paramater) {
		// convert to object
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(paramater.get("pass"));

		Users user = new Users();
		user.setUserId(userService.registNewId());
		user.setUserName(paramater.get("userName"));
		user.setPhone(paramater.get("phone"));
		user.setBirthday(DateUtil.convertFromString(paramater.get("birthday"), DateUtil.DDMMYYYY_FORMAT));
		user.setPass(hashedPassword);
		user.setEmail(paramater.get("email"));
		user.setCredit(BigDecimal.ZERO);

		return user;
	}

	/**
	 * Kiểm tra thông tin người dùng
	 * 
	 * @param Map<String, String> thông tin người dùng
	 * @return Map<String, String> lỗi
	 */
	private Map<String, String> validate(Map<String, String> paramater, boolean isUpdate) {
		Map<String, String> errors = new HashMap<>();
		if (StringUtils.isNullOrEmpty(paramater.get("userName"))) {
			errors.put("validate_userName", "Tên người dùng không được để trống");
		}

		if (null != paramater.get("categoryName") && paramater.get("categoryName").length() < 5) {
			errors.put("validate_userName", "Tên người dùng phải lớn hơn 5 kí tự");
		}

		if (StringUtils.isNullOrEmpty(paramater.get("birthday"))) {
			errors.put("validate_birthday", "Ngày sinh không được để trống");
		}

		if (!StringUtils.isNullOrEmpty(paramater.get("birthday"))
				&& !DateUtil.validateDateDDMMYYYY(paramater.get("birthday"))) {
			errors.put("validate_birthday", "Ngày sinh không đúng định dạng");
		}

		if (!StringUtils.isNullOrEmpty(paramater.get("email")) && !StringUtils.isValidEmail(paramater.get("email"))) {
			errors.put("validate_email", "Email không hợp lệ");
		}

		if (!isUpdate && userService.findByEmailAddress(paramater.get("email")) != null) {
			errors.put("validate_email", "Email đã tồn tại");
		}

		if (StringUtils.isNullOrEmpty(paramater.get("phone"))) {
			errors.put("validate_phone", "Số điện thoại không được để trống");
		}

		if (!StringUtils.isNullOrEmpty(paramater.get("phone")) && !StringUtils.isValidPhone(paramater.get("phone"))) {
			errors.put("validate_phone", "Số điện thoại không đúng định dạng");
		}

		if (paramater.get("role_admin").equals("false") && paramater.get("role_user").equals("false")) {
			errors.put("validate_role", "Chọn ít nhất 1 quyền cho tài khoản");
		}
		return errors;
	}
}
