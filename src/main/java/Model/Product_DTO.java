package Model;

//PRODUCTS 테이블에 SQL문을 사용해 해당 레코드 정보를 가져와 담을 DTO 클래스.
public class Product_DTO {
	private int pID;
	private String pNAME;
	private int pPrice;
	private String pTYPE;
	private String pMANUF;
	
	public Product_DTO(){
		super();
		this.pID = -1;
	}

	public Product_DTO(int pID, String pNAME, int pPrice, String pTYPE, String pMANUF) {
		super();
		this.pID = pID;
		this.pNAME = pNAME;
		this.pPrice = pPrice;
		this.pTYPE = pTYPE;
		this.pMANUF = pMANUF;
	}
	
	public int getpID() {
		return pID;
	}

	public void setpID(int pID) {
		this.pID = pID;
	}

	public String getpNAME() {
		return pNAME;
	}

	public void setpNAME(String pNAME) {
		this.pNAME = pNAME;
	}

	public int getpPrice() {
		return pPrice;
	}

	public void setpPrice(int pPrice) {
		this.pPrice = pPrice;
	}

	public String getpTYPE() {
		return pTYPE;
	}

	public void setpTYPE(String pTYPE) {
		this.pTYPE = pTYPE;
	}

	public String getpMANUF() {
		return pMANUF;
	}

	public void setpMANUF(String pMANUF) {
		this.pMANUF = pMANUF;
	}

	@Override
	public String toString() {
		return this.pNAME+" ( "+this.pPrice+" ) ";
	}
	
}
