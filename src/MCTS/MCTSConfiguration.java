package MCTS;

public class MCTSConfiguration{

    // just in case somebody forgets to do the configuration
    private MCTSMode mode = MCTSMode.FIXED_ITERATIONS;
    private int iterations = 1000;
    private double secondsToThink;

    private int rolloutsPerLeaf = 1;
    private boolean leafParallelization = false;

    public void setFixedIterations(int iterations){
        mode = MCTSMode.FIXED_ITERATIONS;
        this.iterations = iterations;
    }

    public void setFixedTime(double secondsToThink){
        mode = MCTSMode.FIXED_TIME;
        this.secondsToThink = secondsToThink;
    }

    public void setRolloutsPerLeaf(int rolloutsPerLeaf){
        this.rolloutsPerLeaf = rolloutsPerLeaf;
    }

    public void setLeafParallelization(boolean leafParallelization){
        this.leafParallelization = leafParallelization;
    }

    public MCTSMode getMode() {
        return mode;
    }

    public int getIterations() {
        return iterations;
    }

    public double getSecondsToThink() {
        return secondsToThink;
    }

    public int getRolloutsPerLeaf() {
        return rolloutsPerLeaf;
    }

    public boolean isLeafParallelization() {
        return leafParallelization;
    }

    public String toString(){

        String rep = "";

        if(getMode() == MCTSMode.FIXED_ITERATIONS){
            rep += "The MCTS uses a FIXED number of " + getIterations() + " ITERATIONS\n";
        }else{
            rep += "The MCTS uses a FIXED TIME of " + getSecondsToThink() + "s to think\n";
        }

        rep += "Rollouts per leaf: " + getRolloutsPerLeaf() + "\n";
        rep += "Leaf parallelization: " + isLeafParallelization() + "\n";
        return rep;
    }
}
