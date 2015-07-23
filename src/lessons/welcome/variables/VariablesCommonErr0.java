package lessons.welcome.variables;

import plm.core.model.Game;

public class VariablesCommonErr0 extends plm.universe.bugglequest.SimpleBuggle {
	@Override
	public void forward(int i)  { 
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead."));
	}

	@Override
	public void backward(int i) {
		throw new RuntimeException(Game.i18n.tr("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead."));
	}


	@Override
	public void run() {
		int nbPas = 0;
		int reculePas = 0;
			
		while(!estSurBiscuit())
		{
			nbPas = 0;
			reculePas = 0;

			avance();
			nbPas = nbPas + 1;

			if(estSurBiscuit())
			{		
				prendBiscuit();
				while(reculePas != nbPas)
				{
					recule();
					reculePas = reculePas + 1;
				}
				poseBiscuit();
			}
		}
	}
}
