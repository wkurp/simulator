package com.qrp.agent.simulator.main;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.qrp.agent.simulator.model.Policy;
import com.qrp.agent.simulator.model.PolicyStatisticGeneral;
import com.qrp.agent.simulator.util.FilelUtils;

/**
 * Application based on input Branded Factor 
 * group Agents in Breed and simply display result in static HTML template output.
 *
 * @author Witold Kurp
 * @version 1.0
 * @since 2016-05-09
 */
public class AgentSimulator {
	private static final Logger LOGGER = Logger.getLogger(AgentSimulator.class.getName());
	// default range values
	private static int defaultRoundScale = 2;
	private static final BigDecimal BRAND_FACTOR_MINIMUM_RANGE = new BigDecimal("0.1").setScale(defaultRoundScale, BigDecimal.ROUND_CEILING);
	private static final BigDecimal BRAND_FACTOR_MAXIMUM_RANGE = new BigDecimal("2.9").setScale(defaultRoundScale, BigDecimal.ROUND_CEILING);
	// input variable
	private static int yearsMaximum = 15;
	
	private Set<Policy> policySet = new LinkedHashSet<Policy>();
	private List<Policy> agentLostList = new LinkedList<Policy>();
	private List<Policy> agentGainedList = new LinkedList<Policy>();
	private List<Policy> agentRegainedList = new LinkedList<Policy>();

	public static void main(String[] args) {
		BigDecimal brandFactor = new BigDecimal(args[0]).setScale(2, BigDecimal.ROUND_CEILING);
		String readFilePath = args[1];
		AgentSimulator simulator = new AgentSimulator();
		if (!simulator.isBrandFactorOutOfRange(brandFactor)) {
			simulator.simulate(yearsMaximum, brandFactor, readFilePath);
		}
	}

	private void simulate(int yearsRange, BigDecimal brandFactor, String readFilePath) {
		try {
			policySet = new FilelUtils().getPolicySetFromExcelFile(readFilePath);
		} catch (IOException e) {
			LOGGER.warning(e.getMessage() + " , " +readFilePath);
		}
		
		List<PolicyStatisticGeneral> policyStatisticGeneralList = new ArrayList<PolicyStatisticGeneral>();
		
		// For every year you go through the list of agents and increase age, 
		for (int currentYear = 1; currentYear <= yearsRange; currentYear++) {
			LOGGER.info("YEAR " +currentYear);
			for (Policy policy : policySet) {
				policy.incrementAge();
				LOGGER.finest(policy.toString());
				BigDecimal affinity = null;
				if (!isPolicyAutoRenewDisabled(policy)) {
					continue;
				} 
				affinity = calculateAffinity(policy);
				BigDecimal switchRatio = calculateSwitchRatio(policy,brandFactor);
				if (policyIsEqualBreedC(policy) && isSwitchRatioGreatherThanAffinity(affinity, switchRatio) && policyNotExistInRegainedList(policy)) {
					agentLostList.add(policy);
					policy.setAgentBreed("Breed_NC");
				} else if (policyIsEqualBreedNc(policy) && isSwitchRatioGreatherThanAffinity(affinity, switchRatio)) {
					if (agentLostList.contains(policy)) {
						agentLostList.remove(policy);
						agentRegainedList.add(policy);
					} else {
						agentGainedList.add(policy);
					}
					policy.setAgentBreed("Breed_C");
				} 
			}
			PolicyStatisticGeneral policyStatisticGeneral = new PolicyStatisticGeneral(currentYear, agentLostList.size(), agentGainedList.size(), agentRegainedList.size());
			policyStatisticGeneralList.add(policyStatisticGeneral);
		}
		
		try {
			String writeFilePath  = readFilePath.split("\\.")[0] +"Out.xlsx";
			FilelUtils.writePolicyStatisticGeneralExcel(policyStatisticGeneralList, writeFilePath );
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
	}

	private boolean policyNotExistInRegainedList(Policy policy) {
		return !agentRegainedList.contains(policy);
	}
	
	private boolean isPolicyAutoRenewDisabled(Policy policy) {
		return policy.getAutoRenew()==0;
	}

	private boolean policyIsEqualBreedNc(Policy policy) {
		return policy.getAgentBreed().equals("Breed_NC");
	}

	private boolean policyIsEqualBreedC(Policy policy) {
		return policy.getAgentBreed().equals("Breed_C");
	}
	/**
	 * This method calculate affinity threshold switch based on Agent Breed name
	 * if Breed_C (Social_Grade * Attribute_Brand)
	 * if Breed_NC (Social_Grade * Attribute_Brand * Brand_Factor)						
	 * @param single Policy model
	 * @return calculated BigDecimal switchRatio.
	 */
	private BigDecimal calculateSwitchRatio(Policy policy, BigDecimal brandFactor) {
		BigDecimal switchRatio = null;
		if (policy.getAgentBreed().equals("Breed_C")) {
			switchRatio = new BigDecimal(policy.getSocialGrade()).multiply(policy.getAtributeBrand());
		} else if (policy.getAgentBreed().equals("Breed_NC")) {
			switchRatio = new BigDecimal(policy.getSocialGrade()).multiply(policy.getAtributeBrand().multiply(brandFactor));
		} 
		return switchRatio;
	}
	
	/**
	 * This method calculate affinity based on below equation 
	 * Payment_at_Purchase/Attribute_Price + (2 * Attribute_Promotions * Inertia_for_Switch)
	 * @param single Policy model
	 * @return calculated BigDecimal affinity.
	 */
	private BigDecimal calculateAffinity(Policy policy) {
		 //Payment_at_Purchase/Attribute_Price + (2 * Attribute_Promotions * Inertia_for_Switch)						
		BigDecimal left = policy.getPaymentAtPurchase().divide(policy.getAtributePrice(),2,BigDecimal.ROUND_CEILING);
		BigDecimal right = policy.getAtributePromotion().multiply(new BigDecimal(policy.getInertiaForSwitch())).multiply(new BigDecimal("2"));
		BigDecimal affinity = left.add(right);
		LOGGER.finest("Affinity " +affinity);
		return affinity;
	}

	/**
	 * @param BigDecimal brandFactor
	 * @return boolean outOfRange
	 */
	private boolean isBrandFactorOutOfRange(BigDecimal brandFactor) {
		if (brandFactor.compareTo(BRAND_FACTOR_MINIMUM_RANGE)==-1 || brandFactor.compareTo(BRAND_FACTOR_MAXIMUM_RANGE )==1) {
			LOGGER.warning(brandFactor + " brand factor value is out of range");
			return true;
		}
		return false;
	}
	
	@Test
	public void testBrandFactorOutOfRange() {
		assertEquals(false, isBrandFactorOutOfRange(new BigDecimal("2.4").setScale(2, BigDecimal.ROUND_CEILING)));
		assertEquals(false, isBrandFactorOutOfRange(new BigDecimal("0.9").setScale(2, BigDecimal.ROUND_CEILING)));
		assertEquals(false, isBrandFactorOutOfRange(new BigDecimal("2.9").setScale(2, BigDecimal.ROUND_CEILING)));
		assertEquals(true, isBrandFactorOutOfRange(new BigDecimal("3.1").setScale(2, BigDecimal.ROUND_CEILING)));
		assertEquals(true, isBrandFactorOutOfRange(new BigDecimal("0.02").setScale(2, BigDecimal.ROUND_CEILING)));
	}
}
