package com.sys.pp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.sys.pp.model.NewsType;
import com.sys.pp.service.NewsTypeService;
import com.sys.pp.util.NumberUtils;

@Controller
@RequestMapping("/admin/newstype")
public class NewsTypeController {

	@Autowired
	NewsTypeService newsTypeService;

	@GetMapping("")
	public String viewAll() {
		return "layouts/admin/newtype-list";
	}

	/**
	 * Read by page
	 * 
	 * @return JSON List<NewsType>
	 */
	@ResponseBody
	@RequestMapping(path = "/get/{page}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	public List<NewsType> loadData(@PathVariable Integer page) {
		if (null == page) {
			return new ArrayList<>();
		}
		return newsTypeService.findByPageNumber(page);
	}
	
	/**
	 * Read one
	 * 
	 * @return JSON NewsType
	 */
	@ResponseBody
	@RequestMapping(path = "/get/one/{id}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	public Optional<NewsType> getById(@PathVariable Integer id) {
		return newsTypeService.findById(id);
	}

	/**
	 * Delete by key
	 * @param Integer id
	 * 
	 * @return none
	 */
	@ResponseBody
	@RequestMapping(path = "/delete/{id}", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	public void deleteById(@PathVariable Integer id) {
		newsTypeService.removeById(id);
	}

	
	/**
	 * Post a newsType
	 * @param Map<String, String> paramater
	 * Key map:
	 * * name
	 * * price
	 * * level
	 * 
	 * @return status, obj added
	 */
	@ResponseBody
	@PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
	public Object saveNewsType(@RequestBody Map<String, String> paramater) {
		// validate data
		Map<String, String> errors = this.validate(paramater);
		Map<String, Object> result = new HashMap<>();
		result.put("status", true);

		if (!errors.isEmpty()) {
			result.put("status", false);
			result.put("data", errors);
			return result;
		}

		try {
			NewsType newsType = this.createObject(paramater);

			newsTypeService.save(newsType);
			result.put("data", newsType);
			return result;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OBJECT_EXISTS");
		}
	}

	/**
	 * Update a newsType
	 * @param Map<String, String> paramater
	 * Key map:
	 * * name
	 * * price
	 * * level
	 * 
	 * @return status, obj added
	 */
	@ResponseBody
	@PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
	public Object updateNewsType(@RequestBody Map<String, String> paramater, @PathVariable Integer id) {
		// validate data
		Map<String, String> errors = this.validate(paramater);
		Map<String, Object> result = new HashMap<>();
		result.put("status", true);

		if (!errors.isEmpty()) {
			result.put("status", false);
			result.put("data", errors);
			return result;
		}

		try {
			NewsType newsType = this.createObject(paramater);
			newsType.setId(id);

			newsTypeService.save(newsType);
			result.put("data", newsType);
			return result;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OBJECT_EXISTS");
		}
	}

	private NewsType createObject(Map<String, String> paramater) {
		// convert to object
		NewsType newsType = new NewsType();
		newsType.setName(paramater.get("name"));
		newsType.setPrice(Double.parseDouble(paramater.get("price")));
		newsType.setLevel(Integer.parseInt(paramater.get("level")));

		return newsType;
	}

	private Map<String, String> validate(Map<String, String> paramater) {
		Map<String, String> errors = new HashMap<>();
		if (null == paramater.get("name")) {
			errors.put("validate_name", "Tên loại tin không được để trống");
		}

		if (null != paramater.get("name") && paramater.get("name").length() < 5) {
			errors.put("validate_name", "Tên loại tin phải lớn hơn 5 kí tự");
		}
		
		if (null != paramater.get("price") && !NumberUtils.isNumeric(paramater.get("price"))) {
			errors.put("validate_price", "Gía tiền không hợp lệ");
		}
		
		if (null != paramater.get("level") && !NumberUtils.isNumeric(paramater.get("level"))) {
			errors.put("validate_level", "Độ ưu tiên không hợp lệ");
		}
		
		return errors;
	}
}
