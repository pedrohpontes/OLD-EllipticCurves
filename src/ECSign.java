package ellcurves;

import java.math.BigInteger;
import java.util.Random;

public class ECSign {
	
	private EllCurvePoint point;
	private EllCurvePoint publicKey;
	
	
	public ECSign(EllCurvePoint P) {
		point = P;
		publicKey = null;
	}
	
	
	public void setPublicKey(EllCurvePoint P) {
		publicKey = P;
	}
	
	public void setPublicKey(BigInteger privateKey) {
		publicKey = point.multiplyByInteger(privateKey);
	}
	
	
	public EllCurvePoint getPublicKey() {
		return publicKey;
	}
	
	
	public EllCurvePoint getPoint() {
		return point;
	}
	
	
	//note that doc must be an integer mod N already!
	public BigInteger[] signDoc(BigInteger privateKey, BigInteger document) {
		
		BigInteger N = point.getOrder();
		
		int nBits = N.bitLength();
		BigInteger k = BigInteger.ZERO;
		
		BigInteger[] signature = new BigInteger[2];
		
		signature[0] = BigInteger.ZERO;
		signature[1] = BigInteger.ZERO;
		
		while(k.compareTo(BigInteger.ZERO) == 0
				|| signature[0].compareTo(BigInteger.ZERO) == 0
				|| signature[1].compareTo(BigInteger.ZERO) == 0) {
			
			k = new BigInteger(nBits, new Random());
			k = k.mod(N);
			
			EllCurvePoint Q = point.multiplyByInteger(k);
			
			if(Q.getCoords()[0].compareTo(BigInteger.ZERO) != 0) {
				Q.deprojectify();
				
				signature[0] = Q.getCoords()[0].mod(N);
				
				signature[1] = document.add(privateKey.multiply(signature[0]));
				signature[1] = signature[1].multiply(k.modInverse(N));
				signature[1] = signature[1].mod(N);	
			}
		}
		
		return signature;
	}
	
	
	public boolean verifySign(BigInteger[] signature, BigInteger document) {
		
		if(publicKey == null) {
			System.out.println("Error: cannot verify document without a set public key!");
			return false;
		}
		
		BigInteger N = point.getOrder();
		
		BigInteger sign1Inv = signature[1].modInverse(N);
		
		BigInteger v1 = document.multiply(sign1Inv);
		v1 = v1.mod(N);
		
		BigInteger v2 = signature[0].multiply(sign1Inv);
		v2 = v2.mod(N);
		
		EllCurvePoint Q = (point.multiplyByInteger(v1)).addPoint(publicKey.multiplyByInteger(v2));
		Q.deprojectify();
		
		BigInteger x = Q.getCoords()[0].mod(N);
				
		return (x.compareTo(signature[0]) == 0);		
	}
	
	
}
