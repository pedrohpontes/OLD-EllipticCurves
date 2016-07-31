package ellcurves;

import java.math.BigInteger;

public class EllCurvePoint {
	
	private BigInteger x;
	private BigInteger y;
	private BigInteger z;
	private final EllCurve curve;
	private BigInteger order;
	
	
	public EllCurvePoint(BigInteger a, BigInteger b, BigInteger c, EllCurve tocurve) {
		
		if(!tocurve.isPointInCurve(a, b, c))
			System.out.println("Error: point not in curve!");
		
		x = a;
		y = b;
		z = c;
		curve = tocurve;
	}
	
	
	public EllCurvePoint(BigInteger a, BigInteger b, EllCurve toCurve) {
		
		if(!toCurve.isPointInCurve(a, b))
			System.out.println("Error: point not in curve!");
		
		x = a;
		y = b;
		z = BigInteger.ONE;
		curve = toCurve;
	}
	
	
	public BigInteger[] getCoords() {
		BigInteger[] coord = new BigInteger[3];
		
		coord[0] = x;
		coord[1] = y;
		coord[2] = z;
		
		return coord;
	}
	
	
	public EllCurve getCurve() {
		return curve;
	}
	
	
	public void setOrder(BigInteger N) {
		order = N;
	}
	
	
	//substitute to: if(order != null) return order;
	//else calculate the order of the point
	public BigInteger getOrder() {
		return order;
	}
	
	
	public EllCurvePoint add(EllCurvePoint Q) {
		
		if(!curve.isPointInCurve(Q)) {
			System.out.println("Adding point not in the curve!");
			return Q;
		}
		
		BigInteger[] coordQ = Q.getCoords();
		
		BigInteger x2 = coordQ[0];
		BigInteger y2 = coordQ[1];
		BigInteger z2 = coordQ[2];
		
		BigInteger[] coordCurve = curve.getCoords();
		
		BigInteger mod = coordCurve[0];
		BigInteger a1 = coordCurve[1];
		BigInteger a2 = coordCurve[2];
		BigInteger a3 = coordCurve[3];
		BigInteger a4 = coordCurve[4];
		BigInteger a6 = coordCurve[5];
		
		
		if( (z2.mod(mod)).compareTo(BigInteger.ZERO) == 0 )
			return new EllCurvePoint(x,y,z,curve);
		
		if( (z.mod(mod)).compareTo(BigInteger.ZERO) == 0 )
			return new EllCurvePoint(x2,y2,z2,curve);
		
		
		BigInteger lambda;
		BigInteger nu;
		BigInteger d;
		
		BigInteger x3;
		BigInteger y3;
		BigInteger z3;
		
		BigInteger test = (x2.multiply(z)).mod(mod);
		test = (test.subtract(x.multiply(z2).mod(mod))).mod(mod);
		
		BigInteger yz2 = (y.multiply(z2)).mod(mod);
		BigInteger zx2 = (z.multiply(x2)).mod(mod);
		BigInteger zy2 = (z.multiply(y2)).mod(mod);
		BigInteger zz2 = (z.multiply(z2)).mod(mod);
		
		BigInteger test2 = (yz2.add(zy2).add(a1.multiply(zx2)).add(a3.multiply(zz2))).mod(mod);
		
		if( test.compareTo(BigInteger.ZERO) != 0  )
		{			
			BigInteger xy2 = (x.multiply(y2)).mod(mod);
			BigInteger xz2 = (x.multiply(z2)).mod(mod);
			BigInteger yx2 = (y.multiply(x2)).mod(mod);
			
			lambda = (zy2.subtract(yz2)).mod(mod);
			nu = (yx2.subtract(xy2)).mod(mod);
			d = (zx2.subtract(xz2)).mod(mod);
			
			BigInteger llzz2 = (lambda.multiply(lambda).multiply(zz2)).mod(mod);
			BigInteger a1ldzz2 = (a1.multiply(lambda).multiply(d).multiply(zz2)).mod(mod);
			BigInteger x3last = ((a2.multiply(zz2)).add(xz2).add(zx2)).mod(mod);
			x3last = x3last.multiply(d).multiply(d);
			
			x3 = (llzz2.add(a1ldzz2).subtract(x3last)).mod(mod); 
			
			BigInteger y31 = lambda.add(a1.multiply(d));
			y31 = (y31.multiply(x3)).negate();
			y31 = y31.mod(mod);
			
			BigInteger y32 = (nu.multiply(d).multiply(d).multiply(zz2)).mod(mod);
			y32 = y32.negate();
			
			BigInteger y33 = a3.multiply(d).multiply(d).multiply(d).multiply(zz2);
			y33 = y33.negate();
			
			y3 = (y31.add(y32).add(y33)).mod(mod);
			
			x3 = (x3.multiply(d)).mod(mod);
			
			z3 = d.multiply(d).multiply(d).multiply(zz2);
			z3 = z3.mod(mod);
		}
		else if( test2.compareTo(BigInteger.ZERO) == 0) {
				return curve.identity();
		}
		else {
			BigInteger xx = (x.multiply(x)).mod(mod);
			BigInteger yz = (y.multiply(z)).mod(mod);
			BigInteger xz = (x.multiply(z)).mod(mod);
			BigInteger xz2 = (x.multiply(z2)).mod(mod);
			BigInteger zz = (z.multiply(z)).mod(mod);
			BigInteger xxx = (x.multiply(xx)).mod(mod);
			BigInteger xzz = (x.multiply(zz)).mod(mod);
			BigInteger yzz = (y.multiply(zz)).mod(mod);
			BigInteger zzz = (z.multiply(zz)).mod(mod);
			
			BigInteger threexx = (xx.multiply(new BigInteger("3"))).mod(mod);
			BigInteger twoa2xz = (a2.multiply(xz).multiply(new BigInteger("2"))).mod(mod);
			BigInteger a4zz = (zz.multiply(a4)).mod(mod);
			BigInteger a1yz = (yz.multiply(a1)).mod(mod);
			
			lambda =  (threexx.add(twoa2xz).add(a4zz).subtract(a1yz)).mod(mod);
			
			BigInteger twoa6zzz = (zzz.multiply(a6).multiply(new BigInteger("2"))).mod(mod);
			
			nu = (xxx.negate()).add(a4.multiply(xzz).add(twoa6zzz).subtract(yzz.multiply(a3)));
			nu = nu.mod(mod);
					
			BigInteger twoyz = yz.multiply(new BigInteger("2"));
			
			d = (twoyz.add(xz.multiply(a1)).add(zz.multiply(a3))).mod(mod);
			
			BigInteger x31 = (lambda.multiply(lambda).multiply(zz2)).mod(mod);
			BigInteger x32 = (a1.multiply(lambda).multiply(d).multiply(zz2)).mod(mod);
			BigInteger x33 = (a2.multiply(zz2)).add(xz2).add(zx2);
			x33 = (x33.multiply(d).multiply(d)).mod(mod);
			
			x3 = (x31.add(x32).subtract(x33)).mod(mod);
			
			BigInteger y31 = (a1.multiply(d)).add(lambda);
			y31 = (y31.multiply(x3)).mod(mod);
			
			BigInteger y32 = nu.multiply(d).multiply(d).multiply(z2);
			y32 = y32.mod(mod);
			
			BigInteger y33 = a3.multiply(d).multiply(d).multiply(d).multiply(zz2);
			y33 = y33.mod(mod);
			
			y3 = y31.add(y32).add(y33);
			y3 = (y3.negate()).mod(mod);
			
			x3 = (x3.multiply(d)).mod(mod);
			
			
			z3 = d.multiply(d).multiply(d).multiply(zz2);
			z3 = z3.mod(mod);
		}
		
		return new EllCurvePoint(x3,y3,z3,curve);		
	}
	
	
	public EllCurvePoint doublePt() {
		return this.add(this);
	}
	
	
	public EllCurvePoint negatePt() {
		
		BigInteger[] coordCurve = curve.getCoords();
		
		BigInteger mod = coordCurve[0];
		BigInteger a1 = coordCurve[1];
		BigInteger a3 = coordCurve[3];
		
		BigInteger a1x = (a1.multiply(x)).mod(mod);
		BigInteger a3z = (a3.multiply(z)).mod(mod);
		
		BigInteger y0 = (y.negate().subtract(a1x).subtract(a3z)).mod(mod);
		
		return new EllCurvePoint(x,y0,z,curve);
	}
	
	
	public EllCurvePoint multiply(int n) {
		
		EllCurvePoint expP = this;
		EllCurvePoint nP = curve.identity();

		if(n < 0) {
			expP = expP.negatePt();
			n = -n;
		}
				
		if(n == 0) {
			return nP;
		}
		
		if(n == 1)
			return expP;
		
		while(n > 0) {
			
			if(n%2 != 0) {
				nP = nP.add(expP);
			}
			
			expP = expP.doublePt();
			
			n /= 2;
		}
		
		return nP;
	}
	
	
	public EllCurvePoint multiply(BigInteger n) {
		
		EllCurvePoint expP = this;
		EllCurvePoint nP = curve.identity();
		
		BigInteger zero = BigInteger.ZERO;
		BigInteger two = new BigInteger("2");

		if(n.compareTo(zero) < 0) {
			expP = expP.negatePt();
			n = n.negate();
		}
				
		if(n.compareTo(zero) == 0) {
			return nP;
		}
		
		if(n.compareTo(BigInteger.ONE) == 0)
			return expP;
		
		while(n.compareTo(zero) > 0) {
			
			if( (n.mod(two)).compareTo(zero) != 0) {
				nP = nP.add(expP);
			}
			
			expP = expP.doublePt();
			
			n = n.divide(two);
		}
		
		return nP;
	}
	
	
	public boolean isEqualTo(EllCurvePoint P) {
		
		BigInteger[] coords = P.getCoords();
		
		BigInteger x1 = coords[0];
		BigInteger y1 = coords[1];
		BigInteger z1 = coords[2];
		

		boolean isZzero = (z.compareTo(BigInteger.ZERO) == 0);
		boolean isZ1zero = (z.compareTo(BigInteger.ZERO) == 0);

		if(isZzero || isZ1zero) {
			if(isZzero && isZ1zero)
				return true;
			else
				return false;
		}
			
		
		BigInteger gcd = z1.gcd(z);
		BigInteger mod = curve.getMod();
		
		BigInteger mult = z.divide(gcd);
		BigInteger mult1 = z1.divide(gcd);
		
		x1 = (x1.multiply(mult)).mod(mod);
		y1 = (y1.multiply(mult)).mod(mod);
		
		BigInteger x2 = (x.multiply(mult1)).mod(mod);
		BigInteger y2 = (y.multiply(mult1)).mod(mod);
		
		if(x2.compareTo(x1) == 0 && y2.compareTo(y1) == 0)
			return true;
		else
			return false;
	}	
	
	
	public void deprojectify() {
		BigInteger mod = curve.getMod();
		z = z.mod(mod);
		
		if(z.compareTo(BigInteger.ZERO) == 0) {
			x = BigInteger.ZERO;
			y = BigInteger.ONE;
			return;
		}
		
		if(z.gcd(mod).compareTo(BigInteger.ONE) != 0) {
			System.out.println("Error: cannot deprojectify point!");
			return;
		}
		
		BigInteger zInv = z.modInverse(mod);
		
		x = x.multiply(zInv).mod(mod);
		y = y.multiply(zInv).mod(mod);
		z = BigInteger.ONE;
	}
	
	
	public void printPoint() {
		System.out.print("[");
		System.out.print(x);
		System.out.print(", ");
		System.out.print(y);
		System.out.print(", ");
		System.out.print(z);
		System.out.print("]");		
	}
	
	
	public EllCurvePoint clone() {
		return new EllCurvePoint(x,y,z,curve);
	}
	
	
	public static void main(String[] args) {
		
		BigInteger a1 = new BigInteger("0");
		BigInteger a2 = new BigInteger("0");
		BigInteger a3 = new BigInteger("0");
		BigInteger a4 = new BigInteger("0");
		BigInteger a6 = new BigInteger("17");
		BigInteger mod = new BigInteger("757151");
		
		EllCurve curve = new EllCurve(a1, a2, a3, a4, a6, mod);
		
		curve.printCurve();
		
		BigInteger x1 = new BigInteger("-1");
		BigInteger y1 = new BigInteger("4");
		
		EllCurvePoint P = new EllCurvePoint(x1,y1, curve);
		
		System.out.print("P = ");
		P.printPoint();
		System.out.println();
		
		EllCurvePoint Q = P;
		int n = 1;
		
		while(!Q.isEqualTo(curve.identity())) {
			Q = Q.add(P);
			n++;
		}
		
		System.out.print("[");
		System.out.print(n);
		System.out.print("] * P = O");		
	}

}
