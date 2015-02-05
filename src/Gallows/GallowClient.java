package Gallows;

import java.util.Scanner;

public class GallowClient {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {

		boolean playAgain = false;
		Scanner in = new Scanner(System.in);
		System.out
				.println("\nWelcome to the Gallows!\nType (d) and press Enter to play with default code or Enter email code to start Execution : ");
		String s = in.nextLine();
		if (!s.equalsIgnoreCase("d")) {
			GallowSolverState.code = s;
		}

		do {

			GallowSolver GBot = new GallowSolver();
			GallowSolverState GBotState = GallowWSClient.initiateGallows();

			int attempts = GBotState.remaining_guesses;

			int errorsSoFar = 0;

			System.out.println("\nBegin HULU Gallow Solver. Code: " + GallowSolverState.code
					+ ". \n");

			while (GBotState.status == GallowSolverState.Status.ALIVE) {

				System.out.println("Current State: " + GBotState.state);
				char ch = GBot.SolveNextCharacter(GBotState.state);

				GallowSolverState newState = GallowWSClient.JSONRequest(GBotState.token, ch);

				if (!GBotState.state.equals(newState.state)) {
					GBot.AppendResult(ch, true);
					System.out.println("\nCharacter chosen: " + ch + ", " + "Result: Success");
				} else {
					GBot.AppendResult(ch, false);
					System.out.println("\nCharacter chosen: " + ch + ", " + "Result: Miss");
					++errorsSoFar;
				}
				GBotState = newState;
				System.out.println("---------------------------------------------");
			}

			switch (GBotState.status) {

			case FREE:
				System.out.println("\nExpected Phrase: " + GBotState.state);
				System.out.println("Successfully Solved with only " + errorsSoFar + " errors.");
				break;

			case DEAD:
				System.out.println("\nExpected Phrase: " + GBotState.state);
				System.out.println("Failed to solve Phrase in  " + attempts + " attempts.");
				break;

			default:
				break;
			}

			in = new Scanner(System.in);
			System.out.println("\nType (y) and press Enter to play again: ");
			s = in.nextLine();
			if (s.equalsIgnoreCase("y")) {
				playAgain = true;
			}
		} while (playAgain);
	}

}