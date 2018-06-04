package com.ctbc.model.dao;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StaleStateException;

import com.ctbc.model.vo.PcmsFlowBranchVO;
import com.ctbc.model.vo.PcmsFlowMainVO;
import com.ctbc.util.HibernateUtil;

public class PcmsFlowServiceTestGetVersion {
	
	@Transactional
	public Integer insertCase(PcmsFlowMainVO pcmsFlowMainVO, PcmsFlowBranchVO pcmsFlowBranchVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Integer flowMainSeq = 0;
		try {
			session.beginTransaction();
			session.save(pcmsFlowMainVO);
			flowMainSeq = pcmsFlowMainVO.getSeq();
			pcmsFlowBranchVO.setFlowMainSeq(flowMainSeq);
			session.save(pcmsFlowBranchVO);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
		return flowMainSeq;
	}

	@Transactional
	public PcmsFlowBranchVO updateCase(String processUser, Integer flowBranchSeq) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		PcmsFlowBranchVO pcmsFlowBranchVO = null;
		try {
			session.beginTransaction();
			pcmsFlowBranchVO = session.get(PcmsFlowBranchVO.class, flowBranchSeq);
			pcmsFlowBranchVO.setProcessUser(processUser);
			session.saveOrUpdate(pcmsFlowBranchVO);
			session.getTransaction().commit();
		} catch (OptimisticLockException e) {
			throw new OptimisticLockException(pcmsFlowBranchVO);
//			session.getTransaction().rollback();
		}
		return pcmsFlowBranchVO;
	}

	public static void main(String[] args) {
		PcmsFlowServiceTestGetVersion flowService = new PcmsFlowServiceTestGetVersion();

		PcmsFlowMainVO pcmsFlowMainVO = new PcmsFlowMainVO();
		pcmsFlowMainVO.setCaseNo("0901-1070513-001-0001");

		PcmsFlowBranchVO pcmsFlowBranchVO = new PcmsFlowBranchVO();
		pcmsFlowBranchVO.setProcessUser("Z40150");
		
		Integer flowBranchSeq = flowService.insertCase(pcmsFlowMainVO, pcmsFlowBranchVO);
		try {
			PcmsFlowBranchVO pcmsFlowBranchVOResult = flowService.updateCase("Z40180", flowBranchSeq);
			System.out.println(">>>>>>>>>>> " + pcmsFlowBranchVOResult.getVersion());
		} catch (OptimisticLockException e) {
			PcmsFlowBranchVO pcmsFlowBranchVOResult = (PcmsFlowBranchVO) e.getEntity();
			System.out.println(pcmsFlowBranchVOResult.getModifyUser());
		}
		
		HibernateUtil.getSessionFactory().close();
	}

}
