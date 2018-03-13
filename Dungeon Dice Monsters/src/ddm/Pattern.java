package ddm;

public class Pattern {
	
	private int[] R, C;
	
	public Pattern (int r1, int c1, int r2, int c2, int r3, int c3, int r4, int c4, int r5, int c5, int r6, int c6) {
		R = new int[]{r1, r2, r3, r4, r5, r6};
		C = new int[]{c1, c2, c3, c4, c5, c6};
	}
	
	public Pattern (int r1, int c1, int r2, int c2, int r3, int c3, int r4, int c4, String hi) {
		R = new int[]{r1, r2, r3, r4};
		C = new int[]{c1, c2, c3, c4};
	}
		
	public int[] rArray() {
		return R;
	}
	public int[] cArray() {
		return C;
	}
	
	public int r (int arr) {
		return R[arr];
	}
	public int c (int see) {
		return C[see];
	}
}
