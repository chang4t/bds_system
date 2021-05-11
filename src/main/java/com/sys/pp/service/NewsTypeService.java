package com.sys.pp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sys.pp.constant.Names;
import com.sys.pp.model.NewsType;
import com.sys.pp.repo.NewsTypeRepository;

@Service("NewsTypeService")
public class NewsTypeService {

	@Autowired
	private NewsTypeRepository newsTypeRes;

	public List<NewsType> findByPageNumber(int pageNumber) {
		Sort sortable = Sort.by("id").descending();
		PageRequest pageable = PageRequest.of(pageNumber, Names.DEFAULT_PAGE_NUMBER, sortable);
		Page<NewsType> page = newsTypeRes.findAll(pageable);
		return page.toList();
	}

	public void removeById(Integer categoryId) {
		if (null == categoryId) {
			return;
		}

		newsTypeRes.deleteById(categoryId);
	}

	public NewsType save(NewsType entity) {
		return newsTypeRes.save(entity);
	}

	public Optional<NewsType> findById(Integer id) {
		return newsTypeRes.findById(id);
	}
}
