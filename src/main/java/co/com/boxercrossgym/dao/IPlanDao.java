package co.com.boxercrossgym.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.boxercrossgym.entity.Plan;

@Repository
public interface IPlanDao extends JpaRepository<Plan, Integer>{

}
