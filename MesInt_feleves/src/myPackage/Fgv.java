package myPackage;

import java.util.*;

public class Fgv {

	// Városok random koordinátákkal
	public static HashMap<Integer, Hely> varosokMapre(int varosokSzama) {
		Random r = new Random();
		HashMap<Integer, Hely> map = new HashMap<Integer, Hely>();
		for (int i = 0; i < varosokSzama; i++) {
			map.put(i, new Hely(r.nextInt(1000), r.nextInt(1000)));
			System.out.println((i + 1) + ". varos elhelyezkedese: " + map.get(i));
		}
		return map;
	}

	// Egy furár hány db városba megy
	public static List<Integer> varosPerFutar(int varosDB, int futarDB) {
		List<Integer> varosPerFutarList = new ArrayList<Integer>();
		if (varosDB % futarDB == 0) {
			for (int i = 0; i < futarDB; i++) {
				varosPerFutarList.add(varosDB / futarDB);
			}
		} else {
			int szam = (int) Math.floor(varosDB / futarDB);
			int maradek = varosDB - (futarDB * szam);
			for (int i = 0; i < futarDB; i++) {
				varosPerFutarList.add(szam);
			}
			for (int i = futarDB - 1; i >= maradek; i--) {
				int ujszam = 0;
				ujszam = varosPerFutarList.get(i) + 1;
				varosPerFutarList.remove(i);
				varosPerFutarList.add(ujszam);
			}
		}
		return varosPerFutarList;
	}

	// Adott tömböt random sorrendbe
	public static Integer[] kever(Integer[] tomb) {
		List<Integer> tempList = Arrays.asList(tomb);
		Collections.shuffle(tempList);

		tempList.toArray(tomb);

		return tomb;
	}

	// Mutáció
	public static List<Integer[]> mutacio(List<Integer[]> elemek) {
		List<Integer[]> mutaltList = new ArrayList<Integer[]>();
		for (int i = 0; i < elemek.size(); i++) {
			Random r = new Random();
			if (r.nextBoolean()) {
				mutaltList.add(kever(elemek.get(i)));
			} else {
				mutaltList.add(elemek.get(i));
			}
		}
		return mutaltList;
	}

	// Rekombináció
	public static List<Integer[]> rekombinacio(List<Integer[]> mutaltList) {
		List<Integer[]> rekombList = new ArrayList<Integer[]>();
		for (Integer[] i : mutaltList) {
			rekombList.add(i);
		}
		int ennyiszer = mutaltList.size() / 4;
		for (int i = 0; i < ennyiszer; i++) {
			Random r = new Random();
			int randIndex1 = r.nextInt(mutaltList.size() - 1);
			int randIndex2 = r.nextInt(mutaltList.size() - 1);
			Integer[] elemek1 = mutaltList.get(randIndex1);
			Integer[] elemek2 = mutaltList.get(randIndex2);
			rekombList.remove(randIndex1);
			rekombList.remove(randIndex1);

			for (int i1 = 0; i1 < ennyiszer; i1++) {
				int temp1 = elemek1[0];
				int temp2 = elemek2[0];
				int rIndex1 = r.nextInt(elemek1.length - 1);
				int rIndex2 = r.nextInt(elemek2.length - 1);
				temp1 = elemek1[rIndex1];
				temp2 = elemek2[rIndex2];
				int talaltIndex1 = 0;
				int talaltIndex2 = 0;
				for (int j = 0; j < elemek1.length; j++) {
					if (elemek1[j] == temp2)
						talaltIndex1 = j;
					if (elemek2[j] == temp1)
						talaltIndex2 = j;
				}
				elemek1[rIndex1] = elemek1[talaltIndex2];
				elemek1[talaltIndex2] = temp1;
				elemek2[rIndex2] = elemek2[talaltIndex1];
				elemek2[talaltIndex1] = temp2;
			}

			rekombList.add(elemek1);
			rekombList.add(elemek2);
		}
		return rekombList;

	}

	// Két város távolsága
	private static int tavolsag(Hely x, Hely y) {
		int kord1 = Math.abs(x.getA() - y.getA());
		int kord2 = Math.abs(x.getB() - y.getB());
		return kord1 + kord2;
	}

	// Fitness fgv
	public static int fitness(HashMap<Integer, Hely> terkep, Integer[] bejarTomb) {
		List<Hely> helyList = new ArrayList<Hely>();
		for (Map.Entry<Integer, Hely> i : terkep.entrySet()) {
			helyList.add(i.getValue());
		}

		Hely bazis = helyList.get(0);
		helyList.remove(0);

		int osszTav = 0;
		int tempIndex = 0;
		for (int i = 0; i < bejarTomb.length; i++) {
			int temp = bejarTomb[i];
			for (int j = 0; j < temp; j++) {
				int futarTav = 0;
				if (j == 0) {
					futarTav += tavolsag(bazis, helyList.get(tempIndex));
					tempIndex++;
				} else {
					if ((j + 1) < bejarTomb[i]) {
						futarTav += tavolsag(helyList.get(tempIndex - 1), helyList.get(tempIndex));
						tempIndex++;
					} else {
						futarTav += tavolsag(helyList.get(tempIndex), bazis);
						System.out.println((i + 1) + ". futar tav: " + futarTav);
						osszTav += futarTav;
					}
				}

			}
		}
		return osszTav;

	}
	
	//Valószínûség
	public static List<Integer[]> valoszinuseg(Integer[] bejarTomb, List<Integer[]> tomb,  HashMap<Integer, Hely> terkep, boolean isLast) {
		List<Integer[]> doneList = new ArrayList<Integer[]>();
		for (Integer[] integers : tomb) {
			doneList.add(integers);
		}
		
		HashMap<Integer[], Integer> utakTav = new HashMap<Integer[], Integer>();
		for (int i = 0; i < doneList.size(); i++) {
			utakTav.put(doneList.get(i), fitness(terkep, bejarTomb));
		}
		
		Integer max = Integer.MIN_VALUE;
		Integer[] segedTomb = new Integer[doneList.get(0).length];
		for (Map.Entry<Integer[], Integer> entry : utakTav.entrySet()) {
			Integer val = entry.getValue();
			if (val > max) {
				max = val;
				segedTomb = entry.getKey();
			}
		}
		
		int maxSzamjegy = String.valueOf(utakTav.get(segedTomb)).length();
		HashMap<Integer[], Double> tulelesEsely = new HashMap<Integer[], Double>();
		for (int i = 0; i < tomb.size(); i++) {
			double tempSzam1 = -1 * (Math.pow(10, maxSzamjegy) / utakTav.get(tomb.get(i)));
			double tempSzam2 = Math.exp(tempSzam1);
			tulelesEsely.put(tomb.get(i), tempSzam2);
		}
		
		List<Double> segedList = new ArrayList<Double>();
		for (Map.Entry<Integer[], Double> entry : tulelesEsely.entrySet()) {
			segedList.add(entry.getValue());
		}
		Collections.sort(segedList);
		HashMap<Integer[], Double> rendezettTuleles = new HashMap<Integer[], Double>();
		for (int i = 0; i < segedList.size(); i++) {
			for (Map.Entry<Integer[], Double> entry : tulelesEsely.entrySet()) {
				if (entry.getValue() == segedList.get(i)) {
					rendezettTuleles.put(entry.getKey(), segedList.get(i));
				}
			}
		}
		
		Random r = new Random();
		int n = 0;
		double elso = 0;
		for (Map.Entry<Integer[], Double> entry : rendezettTuleles.entrySet()) {
			if (n == 0) {
				elso = entry.getValue();
			} else {
				double temp = Math.pow(1 - entry.getValue(), n - 1) * elso;
				if ((r.nextInt(100) + 1) <= temp) {
					doneList.remove(entry.getKey());
				}
			}
			n++;
		}
		
		if (isLast) {
			List<Integer[]> last = new ArrayList<Integer[]>();
			Optional<Integer[]> firstKey = rendezettTuleles.keySet().stream().findFirst();
			last.add(firstKey.get());
			return last;
		}
		return doneList;
	}

} // end class
