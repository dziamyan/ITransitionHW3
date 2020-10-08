import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class StoneScissorsPaper {
	public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeyException{
		
		if(isArgumentsNotValid(args)) {
			System.out.println("Error: arguments are not valid");
			return; 
		}
		
		SecureRandom random = new SecureRandom();
		Integer compStep = random.nextInt(args.length) + 1;
		byte[] values = new byte[32];
        random.nextBytes(values);
        
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(values, "HmacSHA256");
        mac.init(key);
        byte[] data = mac.doFinal(Charset.forName("US-ASCII").encode(args[compStep-1]).array());
        String result = "";
        for (byte element : data)
        {
            result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }
        System.out.println("HMAC: " + result);
        
		int myStep;
		do {
			myStep = getMyStep(args);
		} while (myStep > args.length);
		
		if (myStep==0)return;

		System.out.println("Your move: " + args[myStep-1]);
		System.out.println("Computer move: " + args[compStep-1]);
		
		if (compStep == myStep) {
			System.out.println("DRAW!");
		} else if (isILost(compStep, getLostForMeNumbers((args.length - 1) / 2, myStep))) {
			System.out.println("Computer Win!");
		} else {System.out.println("You Win!");}
		
		StringBuilder sb = new StringBuilder();
		for (byte b : values) {
			sb.append(String.format("%02x", b));
		}
		System.out.print("HMAC Key: ");
		System.out.println(sb.toString());

	}
	
	public static boolean isArgumentsNotValid(String[] args) {
		HashSet<String> states = new HashSet<String>(Arrays.asList(args));
		if (args.length < 3 || states.size() != args.length || args.length % 2 == 0) {
			return true;
		} else
			return false;
	}
	
	public static int getMyStep(String[] args){
		System.out.println("Available moves:");
		for (int i = 1; i <= args.length; i++) {
			System.out.println(i + ": " + args[i - 1]);
		}
		System.out.println("0: Exit");
		Scanner in = new Scanner(System.in);
		System.out.print("Enter your move:");
		int myStep = in.nextInt();
		return myStep;
	}

	public static int[] getLostForMeNumbers(int length, int myStep) {
		int[] lostForMeNumbs = new int[length];
		int i = 0, temp = myStep;
		while (i < length) {
			if (temp == length*2+1) {
				temp = 0;}
			temp++;
			lostForMeNumbs[i] = temp;
			i++;
		}
		return lostForMeNumbs;
	}

	public static boolean isILost(int step, int[] lostNums) {
		boolean result = false;
		for (int i : lostNums) {
			if (i == step) {
				result = true;
			}
		}
		return result;
	}

}
