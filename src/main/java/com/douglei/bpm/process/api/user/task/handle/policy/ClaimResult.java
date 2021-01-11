package com.douglei.bpm.process.api.user.task.handle.policy;

/**
 * 
 * @author DougLei
 */
public class ClaimResult {
	private boolean canClaim;
	private int leftCount;
	
	public ClaimResult(boolean canClaim, int leftCount) {
		this.canClaim = canClaim;
		this.leftCount = leftCount;
	}

	/**
	 * 能否认领
	 * @return
	 */
	public boolean canClaim() {
		return canClaim;
	}
	/**
	 * 获取还能认领的次数(减去本次认领后)
	 * @return
	 */
	public int getLeftCount() {
		return leftCount;
	}
	
	/**
	 * 不能认领的实例常量
	 */
	public static final ClaimResult CAN_NOT_CLAIM = new ClaimResult(false, -1);
}
