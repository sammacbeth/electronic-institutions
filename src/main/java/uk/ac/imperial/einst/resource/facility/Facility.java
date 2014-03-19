package uk.ac.imperial.einst.resource.facility;

import java.util.Set;

import uk.ac.imperial.einst.Institution;
import uk.ac.imperial.einst.resource.Pool;

public class Facility {
	final Institution inst;
	final Set<Pool> pools;
	final double sunkCost;
	final double fixedCost;
	final double marginalStorageCost;
	final double marginalTransactionCost;
	boolean active = true;

	public Facility(Institution inst, Set<Pool> pools, double sunkCost,
			double fixedCost, double marginalStorageCost,
			double marginalTransactionCost) {
		super();
		this.inst = inst;
		this.pools = pools;
		this.sunkCost = sunkCost;
		this.fixedCost = fixedCost;
		this.marginalStorageCost = marginalStorageCost;
		this.marginalTransactionCost = marginalTransactionCost;
	}

	public Institution getInst() {
		return inst;
	}

	public Set<Pool> getPools() {
		return pools;
	}

	public double getSunkCost() {
		return sunkCost;
	}

	public double getFixedCost() {
		return fixedCost;
	}

	public double getMarginalStorageCost() {
		return marginalStorageCost;
	}

	public double getMarginalTransactionCost() {
		return marginalTransactionCost;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "facility(" + inst + ", " + pools + ", " + sunkCost + ", "
				+ fixedCost + ", " + marginalStorageCost + ", "
				+ marginalTransactionCost + ")";
	}

}
