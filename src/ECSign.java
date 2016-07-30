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
	
	
	public static void main(String[] args) {
		
		BigInteger a1 = new BigInteger("0");
		BigInteger a2 = new BigInteger("0");
		BigInteger a3 = new BigInteger("0");
		BigInteger a4 = new BigInteger("0");
		BigInteger a6 = new BigInteger("17");
		BigInteger mod = new BigInteger("757151");
		
		EllCurve curve = new EllCurve(a1, a2, a3, a4, a6, mod);
		
		BigInteger x1 = new BigInteger("-1");
		BigInteger y1 = new BigInteger("4");
		
		EllCurvePoint P = new EllCurvePoint(x1,y1, curve);
		
		P = P.multiplyByInteger((8*22));
		
		BigInteger N = new BigInteger("239");
		P.setOrder(N);
		
		BigInteger privateKey = new BigInteger("54");
		BigInteger document = new BigInteger("111");
		
		ECSign sign = new ECSign(P);
		
		sign.setPublicKey(privateKey);
		 
		BigInteger[] signature = sign.signDoc(privateKey, document);
		
		System.out.println( sign.verifySign(signature, document) );
		
		int nBits = N.bitLength();
		
		int count = 0;
		
		for(int i = 0; i < 10000; i++) {
			signature[0] = new BigInteger(nBits, new Random()).mod(N);
			signature[1] = new BigInteger(nBits, new Random()).mod(N);
			
			if(signature[0].compareTo(BigInteger.ZERO) != 0 && signature[1].compareTo(BigInteger.ZERO) != 0)
				if(sign.verifySign(signature, document))
					count++;
		}
		
		System.out.println(count);
	}	
	
	
}
