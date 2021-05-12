package com.sys.pp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sys.pp.model.BdsNew;
import com.sys.pp.service.BDSNewService;

@Controller
@RequestMapping("/news")
public class NewsController {
	
	@Autowired
	BDSNewService newService;

	/**
	 * Get by page number
	 * @param page int
	 * @return JSON Array
	 */
	@ResponseBody
	@RequestMapping(path = "/get/{page}", produces = "application/json; charset=UTF-8", method = RequestMethod.GET)
	public List<BdsNew> getNews(@PathVariable Integer page) {
		if (null == page) {
			return new ArrayList<>();
		}
		return newService.findByPageNumber(page);
	}
}
