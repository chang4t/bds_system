package com.sys.pp.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sys.pp.controller.custommodel.PostInfomation;
import com.sys.pp.model.BdsNew;
import com.sys.pp.model.DetailNew;
import com.sys.pp.repo.UserRepository;
import com.sys.pp.service.BDSNewService;
import com.sys.pp.service.StatisticalService;
import com.sys.pp.util.DateUtil;

@Controller
@RequestMapping("admin")
public class AdminHomeController {
	@Autowired
	StatisticalService statisticalService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private BDSNewService bdsNewService;

	@GetMapping("")
	public String home(Model model, Principal principal) {
		DecimalFormat formatter = new DecimalFormat("###,###,###");

		model.addAttribute("userByMonth", formatter.format(statisticalService.countUser(false)));
		model.addAttribute("allUser", formatter.format(statisticalService.countUser(false)));
		if (bdsNewService.findAllByPageNumber(0).size() == 0) {
			model.addAttribute("priceByMonth", "0 VND");
			model.addAttribute("allPrice", "0 VND");
			return "layouts/admin/home";
		}

		BigDecimal countByMonth = statisticalService.countPrice(true);
		if (null != countByMonth) {
			model.addAttribute("priceByMonth", formatter.format(countByMonth) + " VND");
		}

		BigDecimal countAll = statisticalService.countPrice(false);
		if (null != countAll) {
			model.addAttribute("allPrice", formatter.format(countAll) + " VND");
		}

		model.addAttribute("new_regist", loadNewsNewRegist());
		return "layouts/admin/home";
	}

	private List<PostInfomation> loadNewsNewRegist() {
		List<BdsNew> bdsNewList = bdsNewService.findNotApprovedByPageNumber(0);
		List<PostInfomation> result = this.makeAPostInfomation(bdsNewList);
		return result;
	}

	private List<PostInfomation> makeAPostInfomation(List<BdsNew> bdsNewList) {
		List<PostInfomation> list = new ArrayList<>();
		for (BdsNew item : bdsNewList) {
			list.add(this.makeAnItem(item));
		}
		return list;
	}

	private PostInfomation makeAnItem(BdsNew item) {
		DetailNew detail = item.getDetailNew();
		DecimalFormat formatter = new DecimalFormat("###,###,###");

		PostInfomation post = new PostInfomation();
		post.setTitle(item.getTitle());
		post.setCreateAt(DateUtil.convertToString(item.getCreateAt()));
		post.setAcreage(detail.getAcreage() != 0 ? formatter.format(detail.getAcreage()) + "m²" : "--");
		post.setCreateBy(userRepository.findById(item.getCreateBy()).get().getUserName());
		return post;
	}

	@ResponseBody
	@GetMapping("chart-data")
	private Map<String, Object> makeDataList() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		List<String> labelList = new ArrayList<String>();
		for (int i = 6; i > -1; i--) {
			Date d = DateUtil.getMoveDay(-i);
			BigDecimal tmp = statisticalService.getPriceOfDay(d);
			if (tmp == null) {
				list.add(BigDecimal.ZERO);
			} else {
				list.add(tmp);
			}

			labelList.add(DateUtil.convertToString(d));
		}

		result.put("data", list);
		result.put("label", labelList);
		return result;
	}
}
