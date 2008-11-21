package lessons.welcome;

import jlm.lesson.Lesson;

public class Main extends Lesson {

	public Main() {
		super();
		name = "Premiers pas";
		about = "Cette première leçon va vous guider dans vos premiers pas en programmation Java. Elle est destinée aux débutants en Java." +
				"<p>Liste des notions abordées : " +
				"<ul>" +
				" <li>Variables et instructions</li>" +
				" <li>Constructions de syntaxiques de base: " +
				"  <ul>" +
				"   <li>conditionnelles (if et switch)</li>" +
				"   <li>boucles (for, while et do-whiles)</li>" +
				"  </ul></li>" +
				" <li>Méthodes (avec ou sans arguments, avec ou sans retour)</li>" +
				" <li>Tableaux</li>" +
				"</ul></p>";
		setSequential(true);
		addExercise(new Environment(this));
		addExercise(new Basics(this));
		addExercise(new Basics3Pas(this));
		addExercise(new BasicsDrawG(this));
		addExercise(new Conditions(this));
		addExercise(new LoopWhile(this));
		addExercise(new LoopDoWhile(this));
		addExercise(new Variables(this));
		addExercise(new LoopFor(this));
		addExercise(new Methods(this));
		addExercise(new MethodsDogHouse(this));
		addExercise(new MethodsReturning(this));
		addExercise(new MethodsArgs(this));
		addExercise(new MethodsPicture(this));
		addExercise(new MethodsPicture2(this));
		addExercise(new MethodsPicture3(this));
		addExercise(new Square(this));
		addExercise(new Circle(this));
		addExercise(new BDR(this));
		addExercise(new BDR2(this));
		addExercise(new SlugHunting(this));
		addExercise(new Snake(this));
		addExercise(new MethodsPicture4(this));
		addExercise(new Array(this));
		addExercise(new Array2(this));
		addExercise(new TraversalByColumn(this));
		addExercise(new TraversalByLine(this));
		addExercise(new TraversalZigZag(this));
		addExercise(new TraversalDiagonal(this));
	}
}
