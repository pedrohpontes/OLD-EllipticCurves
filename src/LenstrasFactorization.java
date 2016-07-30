package ellcurves;

import java.math.BigInteger;
import java.util.Random;

public class LenstrasFactorization {
	
	public static BigInteger lenstraFactorizStep(BigInteger N, int LoopBound, EllCurvePoint P) {
		
		if(P.getCurve().getMod().compareTo(N) != 0) {
			System.out.println("Curve in Lenstra's Factorization must have mod equal to N");
			return null;
		}
		
		EllCurvePoint Q = P;
		
		for(int i = 1; i < LoopBound; i++) {
			Q = Q.multiplyByInteger(i);
			
			BigInteger z = Q.getCoords()[2];
			z = z.gcd(N);
			
			if(z.compareTo(BigInteger.ONE) != 0)
				return z;
		}
		
		return null;
	}
	
	
	public static EllCurvePoint randomEllCurvePoint(BigInteger mod) {
		
		int nBits = mod.bitLength();
		
		BigInteger x = new BigInteger(nBits, new Random());
		x = x.mod(mod);
		
		BigInteger y = new BigInteger(nBits, new Random());
		y = y.mod(mod);
		
		BigInteger A = new BigInteger(nBits, new Random());
		A = A.mod(mod);
		
		BigInteger B = (y.multiply(y)).subtract(x.multiply(x).multiply(x)).subtract(A.multiply(x));
		B = B.mod(mod);
		
		BigInteger zero = BigInteger.ZERO;
		
		EllCurve curve = new EllCurve(zero,zero,zero,A,B,mod);
		
		return new EllCurvePoint(x,y,curve);
	}
	
	
	public static BigInteger lenstraFactoriz(BigInteger N, int loopBound, int nTries) {
		
		BigInteger factor = null;
		
		for(int n = 0; n < nTries; n++) {
		
			EllCurvePoint P = randomEllCurvePoint(N);
			
			factor = lenstraFactorizStep(N, loopBound, P);
			
			if(factor != null)
				return factor;		
		}
			
		return factor;
	}
	

	public static void main(String[] args) {
		
		
		BigInteger N = new BigInteger("92204427300571806403801");
				
		
		BigInteger factor1 = lenstraFactoriz(N, 1000, 1000000);			
			
		if(factor1 != null) {
			BigInteger factor2 = N.divide(factor1);
			
			System.out.print(N);
			System.out.print(" = ");
			System.out.print(factor1);
			System.out.print(" * ");
			System.out.print(factor2);
		}
		else
			System.out.println("Factor not found.");
		
	}

}
