package myPackage;

import java.util.*;

public class Vrp {

	public static void main(String[] args) {

		Scanner myInput = new Scanner(System.in);
		int varosokSzama;
		int futarokSzama;
		System.out.print("Varosok szama: ");
		varosokSzama = myInput.nextInt();
		System.out.print("Futarok szama: ");
		futarokSzama = myInput.nextInt();
		System.out.println("Vsz: " + varosokSzama);
		System.out.println("Fsz: " + futarokSzama);

		int sejtszam = 1;
		List<Integer[]> elemek = new ArrayList<Integer[]>();
		List<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < varosokSzama; i++) {
			tempList.add((i + 1));
		}
		
		for (int i = 0; i < sejtszam; i++) {
			Collections.shuffle(tempList);
			Integer[] tempTomb = new Integer[tempList.size()];
			for (int j = 0; j < tempList.size(); j++) {
				tempTomb[j] = tempList.get(j);
			}
			elemek.add(tempTomb);
		}
		
		Integer[] segedTomb = new Integer[futarokSzama];
		for (int j = 0; j < segedTomb.length; j++) {
			segedTomb[j] = Fgv.varosPerFutar(varosokSzama, futarokSzama).get(j);
			System.out.println((j + 1) + ". futar / varos db: " + segedTomb[j]);
		}
		
		boolean isLast = false;
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				isLast = true;
			}
			elemek = Fgv.valoszinuseg(segedTomb, Fgv.rekombinacio(Fgv.mutacio(elemek)), Fgv.varosokMapre(varosokSzama), isLast);
		}
		
	} // end main

} // end class
