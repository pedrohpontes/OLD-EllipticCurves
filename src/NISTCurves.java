package ellcurves;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;

public class NISTCurves {
	
	public static EllCurvePoint nistPPoint(String name) throws FileNotFoundException {
		
		File inFile = new File("files/nistcurves.txt");
		
		Scanner inScn = new Scanner(inFile);
		
		EllCurvePoint result = null;
		
		BigInteger minThree = new BigInteger("-3");
		BigInteger zero = BigInteger.ZERO;
		
		while(inScn.hasNextLine() && result == null) {
			String line = inScn.nextLine();
			
			if(line.equals(name)) {
				
				BigInteger mod = null;
				BigInteger b = null;
				BigInteger r = null;
				BigInteger Gx = null;
				BigInteger Gy = null;
				
				for(int i = 0; i < 7; i++) {
					line = inScn.nextLine();
					
					String[] splitLine = line.split("[ ]+");
					
					line = "";
					
					for(int j = 0; j < splitLine.length; j++)
						line = line + splitLine[j];
					
					splitLine = line.split("=");
					
					switch(splitLine[0]) {
						case "p":
							mod = new BigInteger(splitLine[1]);
							break;
						case "b":
							b = new BigInteger(splitLine[1], 16);
							break;
						case "r":
							r = new BigInteger(splitLine[1]);
							break;
						case "Gx":
							Gx = new BigInteger(splitLine[1], 16);
							break;
						case "Gy":
							Gy = new BigInteger(splitLine[1], 16);
							break;
					}
				}
				
				EllCurve curve = new EllCurve(zero,zero,zero,minThree,b,mod);
				
				result = new EllCurvePoint(Gx,Gy,curve);
				result.setOrder(r);
			}
		}
		
		if(result == null)
			System.out.println("Error: NIST curve not found.");
		
		inScn.close();
		return result;
	}
	
}
