package com.sys.pp.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sys.pp.model.BdsNew;

@Repository("BDSNewRepository")
public interface BDSNewRepository extends JpaRepository<BdsNew, Integer> {

	@Query(value = "SELECT count(*) FROM `bds_ news` n inner join detail_news d on n.news_id = d.news_id WHERE d.district_id = :districtId", nativeQuery = true)
	int coutnNewsByDistrictId(int districtId);

	@Query(value = "SELECT count(*) FROM `bds_ news` n inner join detail_news d on n.news_id = d.news_id WHERE d.district_id = :districtId and DATE_FORMAT(n.create_at, '%Y-%m-01') = DATE_FORMAT(SYSDATE(), '%Y-%m-01')", nativeQuery = true)
	int coutnNewsByDistrictIdOnMonth(int districtId);

	@Query(value = "select * from `bds_ news` where status_flg = 1 and delete_flg = 0 and SYSDATE() <= end_date and  SYSDATE() >= start_date order by level desc, create_at desc limit 12", nativeQuery = true)
	List<BdsNew> findHighlightPost();

	@Query(value = "select * from `bds_ news` where status_flg = 0 and delete_flg = 0 order by level desc", nativeQuery = true)
	Page<BdsNew> findNotApproved(Pageable pageable);

	@Query(value = "select * from `bds_ news` where create_by = :userId order by create_at desc, status_flg desc", nativeQuery = true)
	Page<BdsNew> findByUserId(Pageable pageable, String userId);

	@Query(value = "select * from `bds_ news` where delete_flg = 0 and status_flg = 1 and SYSDATE() <= end_date and  SYSDATE() >= start_date order by level desc", nativeQuery = true)
	Page<BdsNew> findAllAndCheckSysdate(Pageable pageable);
	
	@Query(value = "select * from `bds_ news` order by news_id desc", nativeQuery = true)
	Page<BdsNew> finAllByPage(Pageable pageable);
	
	@Query(value = "select b.* from `favourite` f inner join `bds_ news` b on f.news_id = b.news_id where user_id = :userId order by b.create_at desc", nativeQuery = true)
	List<BdsNew> findFarivoteByUserId(String userId);
}