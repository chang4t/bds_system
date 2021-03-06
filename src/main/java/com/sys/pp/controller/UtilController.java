package com.sys.pp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sys.pp.constant.GemRealtyConst;
import com.sys.pp.constant.GemRealtyService;
import com.sys.pp.controller.custommodel.KeyValue;
import com.sys.pp.controller.custommodel.LabelValue;
import com.sys.pp.controller.custommodel.PostInfomation;
import com.sys.pp.model.BdsNew;
import com.sys.pp.model.Category;
import com.sys.pp.model.District;
import com.sys.pp.model.Favourite;
import com.sys.pp.model.FavouritePK;
import com.sys.pp.model.Project;
import com.sys.pp.model.Street;
import com.sys.pp.model.Users;
import com.sys.pp.model.Ward;
import com.sys.pp.repo.BDSNewRepository;
import com.sys.pp.repo.CategoryRepository;
import com.sys.pp.repo.DistrictRepository;
import com.sys.pp.repo.FavouriteRepository;
import com.sys.pp.repo.ProjectRepository;
import com.sys.pp.repo.ProvinceRepository;
import com.sys.pp.repo.StreetRepository;
import com.sys.pp.repo.UserRepository;
import com.sys.pp.repo.WardRepository;
import com.sys.pp.util.NumberUtils;
import com.sys.pp.util.StringUtils;

@Controller
@RequestMapping("util")
public class UtilController {
	@Autowired
	private WardRepository wardRepository;
	@Autowired
	private DistrictRepository districtRepository;
	@Autowired
	private StreetRepository streetRepository;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	ProvinceRepository provinceRepository;
	@Autowired
	FavouriteRepository favouriteRepository;
	@Autowired
	BDSNewRepository bdsNewRepository;

	/**
	 * L???y danh s??ch qu???n huy???n theo ID c???a t???nh th??nh
	 * 
	 * @return List<District> danh s??ch qu???n huy???n
	 */
	@ResponseBody
	@RequestMapping(path = "/get-district/{provinceId}")
	public List<District> loadDistrictsByProvinceId(@PathVariable Integer provinceId) {
		return districtRepository.findByProvinceId(provinceId);
	}

	/**
	 * L???y danh s??ch x?? ph?????ng theo ID c???a qu???n huy???n
	 * 
	 * @return List<Ward> danh s??ch x?? ph?????ng
	 */
	@ResponseBody
	@RequestMapping(path = "/get-ward/{district}")
	public List<Ward> loadWardsByDistrictId(@PathVariable Integer district) {
		return wardRepository.findByDistrictId(district);
	}

	/**
	 * L???y danh s??ch ???????ng theo ID c???a qu???n huy???n
	 * 
	 * @return List<Street> danh s??ch ???????ng
	 */
	@ResponseBody
	@RequestMapping(path = "/get-street/{district}")
	public List<Street> loadStreetByDistrictId(@PathVariable Integer district) {
		return streetRepository.findStreetByDistrictId(district);
	}

	/**
	 * L???y danh s??ch c??c d??? ??n trong khu v???c theo ID c???a qu???n huy???n
	 * 
	 * @return List<Project> danh d??? ??n
	 */
	@ResponseBody
	@RequestMapping(path = "/get-project/{district}")
	public List<Project> loadProjectByDistrictId(@PathVariable Integer district) {
		return projectRepository.findProjectByDistrictId(district);
	}

	/**
	 * L???y danh s??ch h??nh th???c b??i ????ng
	 * 
	 * @return List<Project> danh d??? ??n lable - value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-formality-la_va")
	public List<LabelValue> loadFormalityLabelValue() {
		List<KeyValue> items = GemRealtyConst.createFormalityList();
		List<LabelValue> results = items.stream().map(LabelValue::new).collect(Collectors.toList());

		return results;
	}

	/**
	 * L???y danh s??ch lo???i h??nh b???t ?????ng s???n
	 * 
	 * @return List<Project> danh lo???i h??nh b???t ?????ng s???n lable - value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-category-la_va")
	public List<LabelValue> loadCategoryListByLabelValue() {
		List<Category> items = categoryRepository.findAll();
		List<LabelValue> results = items.stream().map(LabelValue::new).collect(Collectors.toList());

		return results;
	}

	/**
	 * L???y danh s??ch gi?? ti???n theo m???c
	 * 
	 * @return List<Project> danh s??ch gi?? ti???n lable - value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-prices-scope-la_va")
	public List<LabelValue> loadPriceScope() {
		return GemRealtyConst.getPriceScope();
	}

	/**
	 * L???y danh di??n t??ch
	 * 
	 * @return List<Project> danh s??ch di???n t??ch label - value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-acreage-scope-la_va")
	public List<LabelValue> loadAcreageScope() {
		return GemRealtyConst.getAcreageScope();
	}

	/**
	 * L???y danh di??n t??ch m???t tr????sc
	 * 
	 * @return List<Project> danh s??ch di???n t??ch m???t tr?????c label - value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-front-width-scope-la_va")
	public List<LabelValue> loadFrontWidth() {
		return GemRealtyConst.getFrontWidth();
	}

	/**
	 * L???y danh s??ch t???nh
	 * 
	 * @return List<District> danh s??ch t???nh th??nh ph??? lable value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-province_la_va")
	public List<LabelValue> loadProvince() {
		return provinceRepository.findAll().stream().map(LabelValue::new).collect(Collectors.toList());
	}

	/**
	 * L???y danh s??ch qu???n huy???n
	 * 
	 * @return List<District> danh s??ch qu???n huy???n lable value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-district_la_va/{provinceId}")
	public List<LabelValue> loadDistrict(@PathVariable Integer provinceId) {
		return districtRepository.findByProvinceId(provinceId).stream().map(LabelValue::new)
				.collect(Collectors.toList());
	}

	/**
	 * L???y danh sort
	 * 
	 * @return List<District> danh s??ch sort
	 */
	@ResponseBody
	@RequestMapping(path = "/get-sort_la_va")
	public List<LabelValue> loadSortResult() {
		return GemRealtyConst.getSortResults();
	}

	/**
	 * L???y danh s??ch xax ph?????ng
	 * 
	 * @return List<District> danh s??ch x?? ph?????ng lable value
	 */
	@ResponseBody
	@RequestMapping(path = "/get-ward_la_va/{districts}")
	public List<LabelValue> loadWard(@PathVariable String districts) {
		List<Ward> results = new ArrayList<Ward>();
		String[] arr = districts.split(",");

		for (int i = 0; i < arr.length; i++) {
			List<Ward> tmp = wardRepository.findByDistrictId(Integer.valueOf(arr[i]));
			results.addAll(tmp);
		}

		return results.stream().map(LabelValue::new).collect(Collectors.toList());
	}

	/**
	 * L???y danh s??ch ???????ng theo ID c???a qu???n huy???n
	 * 
	 * @return List<Street> danh s??ch ???????ng
	 */
	@ResponseBody
	@RequestMapping(path = "/get-street_la_va/{districts}")
	public List<LabelValue> loadStreet(@PathVariable String districts) {
		List<Street> results = new ArrayList<>();
		String[] arr = districts.split(",");

		for (int i = 0; i < arr.length; i++) {
			List<Street> tmp = streetRepository.findStreetByDistrictId(Integer.valueOf(arr[i]));
			results.addAll(tmp);
		}

		return results.stream().map(LabelValue::new).collect(Collectors.toList());
	}

	/**
	 * L???y danh s??ch c??c d??? ??n trong khu v???c theo ID c???a qu???n huy???n
	 * 
	 * @return List<Project> danh d??? ??n
	 */
	@ResponseBody
	@RequestMapping(path = "/get-project_la_va/{districts}")
	public List<LabelValue> loadProject(@PathVariable String districts) {
		List<Project> results = new ArrayList<>();
		String[] arr = districts.split(",");

		for (int i = 0; i < arr.length; i++) {
			List<Project> tmp = projectRepository.findProjectByDistrictId(Integer.valueOf(arr[i]));
			results.addAll(tmp);
		}
		return results.stream().map(LabelValue::new).collect(Collectors.toList());
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(HomePageController.class);

	/**
	 * L??u b??i ????ng
	 * 
	 * @return String message if false;
	 */
	@ResponseBody
	@PostMapping(path = "/like")
	public String like(Principal principal, @RequestBody String url) {
		Users user = null;
		try {
			String email = principal.getName();
			user = userRepository.findByEmailAddress(email);

			if (StringUtils.isNullOrEmpty(email) || user == null)
				throw new Exception();
		} catch (Exception e) {
			LOGGER.warn("Ch??a ????ng nh???p");
			return "Ch??a ????ng nh???p";
		}

		// save
		try {
			String[] urls = url.split("%2F");
			String id = urls[3];
			if (!NumberUtils.isNumeric(id)) {
				LOGGER.warn("Id kh??ng h???p l???");
				return "Id kh??ng h???p l???";
			}

			FavouritePK pk = new FavouritePK();
			pk.setNewsId(Integer.valueOf(id));
			pk.setUserId(user.getUserId());

			favouriteRepository.save(new Favourite(pk));
		} catch (Exception e) {
			LOGGER.warn("L??u b??i kh??ng th??nh c??ng.");
			return "Kh??ng th??? l??u b??i l??n server.";
		}
		return StringUtils.EMPTY;
	}

	/**
	 * L??u b??i ????ng
	 * 
	 * @return String message if false;
	 */
	@ResponseBody
	@PostMapping(path = "/unlike")
	public String unLike(Principal principal, @RequestBody String url) {
		Users user = null;
		try {
			String email = principal.getName();
			user = userRepository.findByEmailAddress(email);

			if (StringUtils.isNullOrEmpty(email) || user == null)
				throw new Exception();
		} catch (Exception e) {
			LOGGER.warn("Ch??a ????ng nh???p");
			return "Ch??a ????ng nh???p";
		}

		// save
		try {
			String[] urls = url.split("%2F");
			String id = urls[3];
			if (!NumberUtils.isNumeric(id)) {
				LOGGER.warn("Id kh??ng h???p l???");
				return "Id kh??ng h???p l???";
			}

			FavouritePK pk = new FavouritePK();
			pk.setNewsId(Integer.valueOf(id));
			pk.setUserId(user.getUserId());

			favouriteRepository.delete(new Favourite(pk));
		} catch (Exception e) {
			LOGGER.warn("L??u b??i kh??ng th??nh c??ng.");
			return "Kh??ng th??? l??u b??i l??n server.";
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Laasy danh s??ch y??u th??ch
	 * 
	 * @return Post cart list;
	 */
	@ResponseBody
	@GetMapping(path = "/favorites")
	public List<PostInfomation> getFavoritePost(Principal principal) {
		Users user = null;
		try {
			String email = principal.getName();
			user = userRepository.findByEmailAddress(email);

			if (StringUtils.isNullOrEmpty(email) || user == null)
				throw new Exception();
		} catch (Exception e) {
			LOGGER.warn("Ch??a ????ng nh???p");
			return new ArrayList<PostInfomation>();
		}

		try {
			List<BdsNew> bdsList = bdsNewRepository.findFarivoteByUserId(user.getUserId());
			return GemRealtyService.makePostCardList(user.getUserId(), favouriteRepository, bdsList, districtRepository,
					provinceRepository, categoryRepository);
		} catch (Exception e) {
			LOGGER.error("L???y danh s??ch y??u th??ch kh??ng th??nh c??ng.", e);
			return new ArrayList<PostInfomation>();
		}
	}

	/**
	 * Load th??ng tin user ????ng nh???p
	 * 
	 * @return String message if false;
	 */
	@ResponseBody
	@GetMapping(path = "/user-infomation")
	public Users getUserLogin(Principal principal) {
		Users user = null;
		try {
			String email = principal.getName();
			user = userRepository.findByEmailAddress(email);

			if (StringUtils.isNullOrEmpty(email) || user == null)
				throw new Exception();
		} catch (Exception e) {
			LOGGER.warn("Ch??a ????ng nh???p");
			return null;
		}
		
		return user;
	}
}
