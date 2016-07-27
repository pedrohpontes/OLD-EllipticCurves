package ellcurves;

import java.math.BigInteger;

public class EllCurve {
	
	private BigInteger a1;
	private BigInteger a2;
	private BigInteger a3;
	private BigInteger a4;
	private BigInteger a6;
	private BigInteger mod;
	private BigInteger disc;
	
	
	public EllCurve(BigInteger c1, BigInteger c2, BigInteger c3, BigInteger c4, BigInteger c6, BigInteger p) {
		a1 = c1.mod(p);
		a2 = c2.mod(p);
		a3 = c3.mod(p);
		a4 = c4.mod(p);
		a6 = c6.mod(p);
		mod = p;
		
		if(p.compareTo(BigInteger.ONE) <= 0) {
			System.out.println("Invalid modulus for an elliptic curve!");
		}
		
		disc = discFromCoeffs(a1,a2,a3,a4,a6,mod);
		
		if( disc.compareTo(BigInteger.ZERO) == 0 )
			System.out.println("Warning: curve with zero discriminant!");
	}
	
	
	public static BigInteger discFromCoeffs(BigInteger c1, BigInteger c2, BigInteger c3, BigInteger c4, BigInteger c6, BigInteger p) {
		BigInteger two = new BigInteger("2");
		BigInteger four = new BigInteger("4");
		BigInteger eight = new BigInteger("8");
		BigInteger nine = new BigInteger("9");
		BigInteger twentyseven = new BigInteger("27");
		
		BigInteger b2 = ((c1.multiply(c1)).add(c2.multiply(four))).mod(p);
		BigInteger b4 = ((c4.multiply(two)).add(c1.multiply(c3))).mod(p);
		BigInteger b6 = ((c3.multiply(c3)).add(c6.multiply(four))).mod(p);
		BigInteger b8 = ((c1.multiply(c1).multiply(c6)).add(c2.multiply(c6).multiply(four))).mod(p);
		b8 = b8.subtract(c1.multiply(c3).multiply(c4)).add(c2.multiply(c3).multiply(c3));
		b8 = (b8.subtract(c4.multiply(c4))).mod(p);
		
		BigInteger disc = (b2.multiply(b2).multiply(b8)).negate();
		disc = disc.subtract(b4.multiply(b4).multiply(b4).multiply(eight));
		disc = disc.subtract(b6.multiply(b6).multiply(twentyseven));
		disc = disc.add(b2.multiply(b4).multiply(b6).multiply(nine));
		disc = disc.mod(p);
		
		return disc;
	}
	
	
	public BigInteger discriminant() {
		return disc;
	}
	
	
	public BigInteger[] getCoords() {
		
		BigInteger[] coords = new BigInteger[6];
		
		coords[0] = mod;
		coords[1] = a1;
		coords[2] = a2;
		coords[3] = a3;
		coords[4] = a4;
		coords[5] = a6;
		
		return coords;
	}
	
	
	public BigInteger getMod() {
		return mod;
	}
	
	
	public boolean isPointInCurve(BigInteger x, BigInteger y) {
		return isPointInCurve(x,y,BigInteger.ONE);
	}
	
	
	public boolean isPointInCurve(BigInteger x, BigInteger y, BigInteger z) {
		
		BigInteger yz = (y.multiply(z)).mod(mod);
		BigInteger xx = (x.multiply(x)).mod(mod);
		BigInteger zz = (z.multiply(z)).mod(mod);
		
		BigInteger yyz = (y.multiply(yz)).mod(mod);
		BigInteger xyz = (x.multiply(yz)).mod(mod);
		BigInteger yzz = (y.multiply(zz)).mod(mod);
		BigInteger xxx = (x.multiply(xx)).mod(mod);
		BigInteger xxz = (z.multiply(xx)).mod(mod);
		BigInteger xzz = (x.multiply(zz)).mod(mod);
		BigInteger zzz = (z.multiply(zz)).mod(mod);
		
		BigInteger t2 = (a1.multiply(xyz)).mod(mod);
		BigInteger t3 = (a3.multiply(yzz)).mod(mod);
		BigInteger t5 = (a2.multiply(xxz)).mod(mod);
		BigInteger t6 = (a4.multiply(xzz)).mod(mod);
		BigInteger t7 = (a6.multiply(zzz)).mod(mod);
		
		BigInteger calc = (yyz.add(t2).add(t3).subtract(xxx).subtract(t5).subtract(t6).subtract(t7)).mod(mod);
		
		if(calc.compareTo(BigInteger.ZERO) == 0)
			return true;
		
		return false;
	}
	

	public boolean isPointInCurve(EllCurvePoint P) {
		BigInteger[] coord = P.getCoords();
		
		BigInteger x = coord[0];
		BigInteger y = coord[1];
		BigInteger z = coord[2];
		
		return isPointInCurve(x,y,z);
	}
	
	
	public void printCurve() {
		System.out.print( "y^2 + " );
		System.out.print( a1 );
		System.out.print( "xy + " );
		System.out.print( a3 );
		System.out.print( "y = x^3 + " );
		System.out.print( a2 );
		System.out.print( "x^2 + " );
		System.out.print( a4 );
		System.out.print( "x + " );
		System.out.print( a6 );
		System.out.print( " mod " );
		System.out.println( mod );
				
		return;
	}
	
	
	public EllCurvePoint identity() {
		return new EllCurvePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO, this);
	}

}
