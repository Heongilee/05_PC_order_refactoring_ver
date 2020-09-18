package Model;

//ORDERS 테이블에 SQL문을 사용하여 해당 레코드들을 가져와 담을 DTO 클래스.
public class Orders_DTO {
	private String cNAME;
	private String pNAME;
	private int oCNT;
	
	public String getcNAME() {
		return cNAME;
	}

	public void setcNAME(String cNAME) {
		this.cNAME = cNAME;
	}

	public String getpNAME() {
		return pNAME;
	}

	public void setpNAME(String pNAME) {
		this.pNAME = pNAME;
	}

	public int getoCNT() {
		return oCNT;
	}

	public void setoCNT(int oCNT) {
		this.oCNT = oCNT;
	}

	public Orders_DTO(String cNAME, String pNAME, int oCNT) {
		super();
		this.cNAME = cNAME;
		this.pNAME = pNAME;
		this.oCNT = oCNT;
	}

	@Override
	public String toString() {
		return "Orders_DTO [cNAME=" + cNAME + ", pNAME=" + pNAME + ", oCNT=" + oCNT + "]";
	}
	
	
}
