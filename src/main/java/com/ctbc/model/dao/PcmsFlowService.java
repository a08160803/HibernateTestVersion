package com.ctbc.model.dao;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;

import org.hibernate.Session;

import com.ctbc.model.vo.PcmsFlowBranchVO;
import com.ctbc.model.vo.PcmsFlowMainVO;
import com.ctbc.util.HibernateUtil;

public class PcmsFlowService {

	@Transactional
	public void insertCase(PcmsFlowMainVO pcmsFlowMainVO, PcmsFlowBranchVO pcmsFlowBranchVO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			
			session.save(pcmsFlowMainVO);
			
			pcmsFlowBranchVO.setFlowMainSeq(pcmsFlowMainVO.getSeq());
			session.save(pcmsFlowBranchVO);
			
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
		}
	}
	
	@Transactional
	public void updateCase(String processUser, Integer flowBranchSeq) throws InterruptedException {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			PcmsFlowBranchVO pcmsFlowBranchVO = session.get(PcmsFlowBranchVO.class, flowBranchSeq);
			pcmsFlowBranchVO.setProcessUser(processUser);
			System.out.println("1 >>>>>>" + pcmsFlowBranchVO.getVersion());
			Thread.sleep(5000);
			session.saveOrUpdate(pcmsFlowBranchVO);
			System.out.println("2 >>>>>>" + pcmsFlowBranchVO.getVersion());
			session.getTransaction().commit(); 
		} catch (OptimisticLockException e) {
			session.getTransaction().rollback();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		PcmsFlowService flowService = new PcmsFlowService();
		
		PcmsFlowMainVO pcmsFlowMainVO = new PcmsFlowMainVO();
		pcmsFlowMainVO.setCaseNo("0901-1070513-001-0001");
		
		PcmsFlowBranchVO pcmsFlowBranchVO = new PcmsFlowBranchVO();
		pcmsFlowBranchVO.setProcessUser("Z40150");
		
		flowService.insertCase(pcmsFlowMainVO, pcmsFlowBranchVO);
		
		flowService.updateCase("Z40180", 1);
	}
}


























