package ai;

public abstract class AI {

	/**
	 * This method returns the name of the AI. Notice two things:
	 * -> The name of the AI may not be changed during the game.
	 * -> Two opposing AIs must have different names.
	 *
	 * @return Name of the AI.
	 */
	public abstract String getName();

	/**
	 * Most important method: Here you get informed about the opponent's moves and
	 * select your own move.
	 *
	 * @param enemyIndex
	 *            The index that refers to the field chosen by the enemy in the last action. If this value is -1, then you are the starting player and have to specify the first move.
	 * @return Move to be played.
	 */
	public abstract int getMove(int enemyIndex);
	private static int[] getState(int[] state,int k){
		int r=state[k];
		state[k]=0;
		boolean streak=true;
		for(int i=r;i>0;i--){
			if(i+k<=11){
				state[i+k]++;
				if((state[k+i]==2||state[k+i]==4||state[k+i]==6)&&streak) {
					if (k > 5) {
						state[13] += state[k + i];
						state[k + i] = 0;
					} else {
						state[12] += state[k + i];
						state[k + i] = 0;
					}
				} else{
						streak=false;
				}
			} else {
				state[i+k-11]++;
				if((state[k+i-11]==2||state[i+k-11]==4||state[i+k-11]==6)&&streak) {
					if (k > 5) {
						state[13] += state[i+k-11];
						state[i+k-11] = 0;
					} else {
						state[12] += state[i+k-11];
						state[i+k-11] = 0;
					}
				} else{
					streak=false;
				}
			}

		}

		return state;
	}
	private static int getHeuristik(int[] state){
		int points=0;
		boolean p1=false;
		boolean p2=false;
		for(int i=0;i<=5;i++){
			if(state[i]>0){
				points++;
				p1=true;
			}
			if(state[6+i]>0){
				points--;
				p2=true;
			}
		}
		if(!p1){
			points=0;
			for(int i=0;i<=11;i++){
				points-=state[i];
			}
			if(points+state[13]>state[12]){
			  points=-100;
      }
		}
		if(!p2){
			points=0;
			for(int i=0;i<=11;i++){
				points+=state[i];
			}
      if(points+state[12]>state[13]){
        points=100;
      }
		}
		return state[12]-state[13]+points;
	}
	public static void main(String[] args){
		int[] state={0,0,0,0,0,0,0,0,0,0,0,0,1,0};
		//int[] newstate=getState(state,10);

		System.out.println(""+getHeuristik(state));
	}
}
